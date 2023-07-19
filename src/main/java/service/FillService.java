package service;

import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import exception.DataAccessException;
import exception.InvalidRequestException;
import exception.NotFoundException;
import model.Event;
import model.Person;
import model.User;
import request.FillRequest;
import result.Result;
import service.requestvalidation.FillRequestValidator;
import service.sampledata.*;

import java.util.Random;

/**
 * Generates sample family history data for the user.
 */
public class FillService {
    /** The request containing information about the fill */
    private final FillRequest request;

    /** Data Access Object that is used to access information about the user */
    private final UserDAO userDAO;

    /** Data Access Object which is used to insert people into the Person table */
    private final PersonDAO personDAO;

    /** Data Access Object which is used to insert events into the Event table */
    private final EventDAO eventDAO;

    /** Stores the list of sample locations provided in /json/locations.json */
    private final SampleLocations locations;

    /** Stores the list of sample male names provided in /json/mNames.json */
    private final SampleMaleNames mNames;

    /** Stores the list of sample female names provided in /json/fnames.json */
    private final SampleFemaleNames fNames;

    /** Stores the list of sample surnames provided in /json/sNames.json */
    private final SampleSurnames sNames;

    private int insertedPersons;
    private int insertedEvents;

    /**
     * Constructs the FillService using the provided request, initializes DAOs, and builds the sample lists.
     * @param request The provided request.
     */
    public FillService(FillRequest request) {
        this.request = request;

        // Initialize the DAOs
        userDAO = new UserDAO();
        personDAO = new PersonDAO();
        eventDAO = new EventDAO();

        // Initialize the sample location and name lists
        locations = new SampleLocations();
        mNames = new SampleMaleNames();
        fNames = new SampleFemaleNames();
        sNames = new SampleSurnames();

        insertedPersons = 0;
        insertedEvents = 0;
    }

    /**
     * Generates family history data for the specified user and number of generations, then stores the results.
     */
    public Result generateFamilyHistoryData() {
        try {
            // Validate the request, and prepare the database to receive new information
            FillRequestValidator.validateRequest(request);
            prepareDatabase();

            // Get the user's person information from the database
            Person userPerson = getUserPerson();

            // Generate birth information for the user
            Location userBirthplace = locations.getRandomLoc();
            int userBirthYear = generateRandomNumber(2005, 1970);

            // Insert the user's birth into the database
            eventDAO.insert(new Event(request.getUsername(), userPerson.getPersonID(),
                    userBirthplace.getLatitude(), userBirthplace.getLongitude(),
                    userBirthplace.getCountry(), userBirthplace.getCity(), "birth", userBirthYear));
            ++insertedEvents;

            // If more generations are requested, fill the tree recursively
            if(request.getGenerations() > 0) generateEvents(userPerson, userBirthYear, 1);

            // If no errors have been thrown, return a successful result
            String successMessage = String.format("Successfully added %d persons and %d events to the database.", insertedPersons, insertedEvents);
            return new Result(true, successMessage);

        } catch (NotFoundException n) {
            // This means that at some point the user was not found.
            return new Result(false, "Error: Invalid request (user does not exist).");

        } catch (DataAccessException d) {
            // This error indicates that there was a problem while accessing the database
            return new Result(false, "Error: Internal server error (" + d.getMessage() + ").");

        } catch (InvalidRequestException i) {
            // This error means the request is invalid
            return new Result(false, "Error: Invalid request (" + i.getMessage() + ").");
        }
    }

    /**
     * Generates events for the request number of generations, starting with generation 1 (parents of user). It does
     * this by generating data for pairs of parents at a time, and uses information about their child who already
     * exists in the database to ensure that timelines match up.
     *
     * @param startPerson           The child of the couple being generated.
     * @param startYear             The year of the child's birth.
     * @param generation            The current generation number, starting at 1 (parents).
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private void generateEvents(Person startPerson, int startYear, int generation) throws DataAccessException {
        // Generate names randomly for the mother
        String motherName = fNames.getRandomName();
        String maidenName = sNames.getRandomName();

        // Generate a random first name for the father, but his surname must match his child's
        String fatherName = mNames.getRandomName();
        String familyName = startPerson.getLastName();

        // Create Person objects for the mother and father
        Person mother = new Person(startPerson.getAssociatedUsername(), motherName, maidenName, "f");
        Person father = new Person(startPerson.getAssociatedUsername(), fatherName, familyName, "m");

        // Set the child's mother and father id's in the database
        personDAO.setFatherId(startPerson.getPersonID(), father.getPersonID());
        personDAO.setMotherId(startPerson.getPersonID(), mother.getPersonID());
        personDAO.insert(mother);
        personDAO.insert(father);
        insertedPersons += 2;

        personDAO.setSpouseId(mother.getPersonID(), father.getPersonID());
        personDAO.setSpouseId(father.getPersonID(), mother.getPersonID());

        // Get events for the birth of both parents
        Event motherBirth = generateBirthEvent(mother, startYear, 50);
        Event fatherBirth = generateBirthEvent(father, startYear, 120);

        // Get a marriage event for the mother, then copy it for the father
        Event motherMarriage = generateMarriage(mother, startYear, motherBirth.getYear(), fatherBirth.getYear());
        Event fatherMarriage = motherMarriage.copyToPerson(father);

        // Get events for the deaths of the parents
        Event motherDeath = generateDeath(mother, motherBirth.getYear(), startYear);
        Event fatherDeath = generateDeath(father, fatherBirth.getYear(), startYear);

        // Insert all events into the table
        eventDAO.insert(motherBirth);
        eventDAO.insert(fatherBirth);
        eventDAO.insert(motherMarriage);
        eventDAO.insert(fatherMarriage);
        eventDAO.insert(motherDeath);
        eventDAO.insert(fatherDeath);
        insertedEvents += 6;

        // If we haven't hit the number of request generations yet, generate parent data recursively for both parents
        if (generation < request.getGenerations()) {
            generateEvents(mother, motherBirth.getYear(), generation + 1);
            generateEvents(father, fatherBirth.getYear(), generation + 1);
        }
    }

    /**
     * Prepares the database for the fill by clearing out all events and people associated with the user.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     */
    private void prepareDatabase() throws DataAccessException, NotFoundException {
        eventDAO.removeByUsername(request.getUsername());
        personDAO.removeByUsername(request.getUsername());

        personDAO.insert(getUserPerson());
        ++insertedPersons;
    }

    /**
     * Generates a birth event for a specified person.
     * @param person        The person for whom we are generating the birth.
     * @param startYear     The year they are allowed to be born (based on the childs birth year).
     * @param maximumAge    The last valid year they could be born.
     * @return              An {@link Event} object for the birth of {@code person}
     */
    private Event generateBirthEvent(Person person, int startYear, int maximumAge) {
        /* Get a random year between when the person is 13 (the youngest childbearing age), and the maximum childbearing
         * age (50 for women, 120 for men). */
        int birthyear = generateRandomNumber(startYear - 13, startYear - maximumAge);

        // Get a random location for the birthplace
        Location birthplace = generateRandomLocation();

        // Return an event object with those details
        return toEventObject(person, birthplace, "birth", birthyear);
    }

    /**
     * Generates a marriage event for a specified user
     * @param person            The person for whom we are generating the marriage.
     * @param childBirthyear    The birthyear of the couple's child.
     * @param motherBirthyear   The birthyear of the mother.
     * @param fatherBirthyear   The birthyear of the father.
     * @return                  An {@link Event} object for the marriage of {@code person}
     */
    private Event generateMarriage(Person person, int childBirthyear, int motherBirthyear, int fatherBirthyear) {
        // Initialize the marriage year
        int marriageYear = 0;

        /* If the father is older than the mother, get a random year between when the mother turns 13 and the child is
         * born, otherwise get a random year between when the father turns 13 and the child is born
         */
        if (motherBirthyear >= fatherBirthyear) {
            marriageYear = generateRandomNumber(childBirthyear, motherBirthyear + 13);
        } else {
            marriageYear = generateRandomNumber(childBirthyear, fatherBirthyear + 13);
        }

        // Get a random location for the marriage
        Location marriageLocation = generateRandomLocation();

        // Return an event object with those details
        return toEventObject(person, marriageLocation, "marriage", marriageYear);
    }

    /**
     * Generates a death event for a specified person.
     * @param person            The person for whom we are generating the death event.
     * @param birthyear         The birthyear of the person.
     * @param childBirthyear    The birthyear of their child.
     * @return                  An {@link Event} object for the death of {@code person}
     */
    private Event generateDeath(Person person, int birthyear, int childBirthyear) {
        // A person can die anytime between the year their child is born and when they turn 120
        int deathyear = generateRandomNumber(birthyear + 120, childBirthyear);

        // Get a random location for the death place
        Location deathplace = generateRandomLocation();

        // Return an event object with those details
        return toEventObject(person, deathplace, "death", deathyear);
    }

    /**
     * Generates a random number between the maximum and minimum values.
     * @param max The maximum value of the random number.
     * @param min The minimum value of the random number.
     * @return    A random number between {@code max} and {@code min}
     */
    private int generateRandomNumber(int max, int min) {
        // Initialize a new random object
        Random random = new Random();

        // Return a random number between max and min
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Creates an event object from the specified person, location, type, and year.
     * @param person    The person associated with this event.
     * @param location  The location of the event.
     * @param type      The type of the event.
     * @param year      The year the event happened.
     * @return          An {@link Event} object with the specified attributes.
     */
    private Event toEventObject(Person person, Location location, String type, int year) {
        return new Event(
                person.getAssociatedUsername(),
                person.getPersonID(),
                location.getLatitude(),
                location.getLongitude(),
                location.getCountry(),
                location.getCity(),
                type,
                year
        );
    }

    /**
     * Generates a random location.
     * @return A random location.
     */
    private Location generateRandomLocation() {
        return locations.getRandomLoc();
    }

    /**
     * Gets the user's {@link Person} object.
     * @return                      The user's {@link Person} object.
     * @throws DataAccessException  If there is an issue accessing the database or table.
     * @throws NotFoundException    If there is no user with the provided username.
     */
    private Person getUserPerson() throws DataAccessException, NotFoundException {
        User user = userDAO.find(request.getUsername());

        return new Person(user);
    }
}
