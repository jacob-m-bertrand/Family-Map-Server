package model;

import java.util.UUID;

/**
 * Stores information about a person.
 */
public class Person {
    /** The ID of the person from the database. Must be unique. */
    private final String personID;

    /** The username associated with this person. */
    private final String associatedUsername;

    /** The first name of the person. */
    private final String firstName;

    /** The last name of the person. */
    private final String lastName;

    /** The gender of the person. */
    private final String gender;

    /** The personID of this person's father. May be null. */
    private final String fatherID;

    /** The personID of this person's mother. May be null. */
    private final String motherID;

    /** The personID of this person's spouse. May be null. */
    private final String spouseID;

    /**
     * Constructs a new Person object using entirely provided data.
     * @param personID              The ID of the person from the database. Must be unique.
     * @param associatedUsername    The username associated with this person.
     * @param firstName             The first name of the person.
     * @param lastName              The last name of the person.
     * @param gender                The gender of the person.
     * @param fatherID              The personID of this person's father. May be null.
     * @param motherID              The personID of this person's mother. May be null.
     * @param spouseID              The personID of this person's spouse. May be null.
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender,
                  String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }



    /**
     * Constructs a new Person object, randomly generating the personID, setting parents and spouse IDs to null, and
     * using provided data for other fields.
     * @param associatedUsername    The username associated with this person.
     * @param firstName             The first name of the person.
     * @param lastName              The last name of the person.
     * @param gender                The gender of the person.
     */
    public Person(String associatedUsername, String firstName, String lastName, String gender) {
        // Generate a random personID using UUID
        personID = UUID.randomUUID().toString();

        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;

        fatherID = "";
        motherID = "";
        spouseID = "";

    }

    /**
     * Constructs a new Person object using a user object.
     * @param user The user object of the user for whom we are creating a person.
     */
    public Person(User user) {
        personID = user.getPersonID();
        associatedUsername = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        gender = user.getGender();

        fatherID = "";
        motherID = "";
        spouseID = "";
    }

    /**
     * Returns a string containing all the data in the person.
     */
    @Override
    public String toString() {
        return String.format("Person - %s : %s : %s : %s : %s : %s : %s : %s",
                personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
    }

    /**
     * Determines if this Person is equal to another object.
     * @param o The object to compare.
     * @return  {@code true} if this Person and {@code o} are equal, {@code false} if they are not.
     */
    @Override
    public boolean equals(Object o) {
        // Check if o is a Person, if not, they are not equal
        if ( !(o instanceof Person) ) {
            return false;
        }

        // Create a Person object from o, since o is a Person
        Person other = (Person) o;

        // Compare their values
        return personID.equals(other.personID)
                && associatedUsername.equals(other.associatedUsername)
                && firstName.equals(other.firstName)
                && lastName.equals(other.lastName)
                && gender.equals(other.gender)
                && fatherID.equals(other.fatherID)
                && motherID.equals(other.motherID)
                && spouseID.equals(other.spouseID);
    }

    public String getPersonID() {
        return personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }
}
