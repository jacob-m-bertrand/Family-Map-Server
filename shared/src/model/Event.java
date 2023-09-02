package model;

import java.util.UUID;

/**
 * Stores information about an Event.
 */
public class Event {
    /** The ID of the event from the database. Must be unique. */
    private final String eventID;

    /** The username associated with this event. */
    private final String associatedUsername;

    /** The person this event happened to. */
    private final String personID;

    /** The latitude of the location where the event occurred. */
    private final float latitude;

    /** The longitude of the location where the event occurred. */
    private final float longitude;

    /** The country where the event occurred. */
    private final String country;

    /** The city where the event occurred. */
    private final String city;

    /** The type of event. */
    private final String eventType;

    /** The year in which the event occurred. */
    private final int year;

    /**
     * Constructs a new Event object with a random eventID and provided data.
     * @param associatedUsername    The username associated with this event.
     * @param personID              The person this event happened to.
     * @param latitude              The latitude of the location where the event occurred
     * @param longitude             The longitude of the location where the event occurred.
     * @param country               The country where the event occurred.
     * @param city                  The city where the event occurred.
     * @param eventType             The type of event.
     * @param year                  The year in which the event occurred.
     */
    public Event(String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        // Generate a new eventID from a UUID
        this.eventID = UUID.randomUUID().toString();

        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * Constructs an event entirely using provided data.
     * @param eventID               The ID of the event from the database.
     * @param associatedUsername    The username associated with this event.
     * @param personID              The person this event happened to.
     * @param latitude              The latitude of the location where the event occurred
     * @param longitude             The longitude of the location where the event occurred.
     * @param country               The country where the event occurred.
     * @param city                  The city where the event occurred.
     * @param eventType             The type of event.
     * @param year                  The year in which the event occured.
     */
    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * Returns a string containing all the data in the event.
     */
    @Override
    public String toString() {
        return String.format("Event - %s : %s : %s : %f : %f : %s : %s : %s : %d", eventID, associatedUsername,
                personID, latitude, longitude, city, country, eventType, year).trim();
    }


    /**
     * Determines if this Event is equal to another object.
     * @param o The object to compare.
     * @return  {@code true} if this event and {@code o} are equal, {@code false} if they are not.
     */
    @Override
    public boolean equals(Object o) {
        // Check if o is an Event, if not, they are not equal
        if( !(o instanceof Event) ) {
            return false;
        }

        // Create an Event object from o, since o is an Event
        Event other = (Event) o;

        // Compare their values
        return eventID.equals(other.eventID)
                && associatedUsername.equals(other.associatedUsername)
                && personID.equals(other.personID)
                && latitude == other.latitude
                && longitude == other.longitude
                && country.equals(other.country)
                && city.equals(other.city)
                && eventType.equals(other.eventType)
                && year == other.year;
    }


    /**
     * Copies this event, but for another person.
     * @param person The person for whom we want this event to be created.
     * @return       An {@code Event} object which is a copy of this one, except with the personID of {@code person}.
     */
    public Event copyToPerson(Person person) {
        return new Event(associatedUsername, person.getPersonID(), latitude, longitude, country, city, eventType, year);
    }

    public String getEventID() {
        return eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }
}
