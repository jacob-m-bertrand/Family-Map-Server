package model;

import java.util.UUID;

/**
 * Stores all the information relating to an event
 */
public class Event {
    private final String eventID;
    private final String associatedUsername;
    private final String personID;
    private final float latitude;
    private final float longitude;
    private final String country;
    private final String city;
    private final String eventType;
    private final int year;

    public Event(String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
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

    @Override
    public String toString() {
        return String.format("Event - %s : %s : %s : %f : %f : %s : %s : %s : %d", eventID, associatedUsername,
                personID, latitude, longitude, city, country, eventType, year).trim();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Event)) return false;

        Event e = (Event) o;

        if(!eventID.equals(e.eventID)) return false;
        if(!associatedUsername.equals(e.associatedUsername)) return false;
        if(!personID.equals(e.personID)) return false;
        if(latitude != e.latitude) return false;
        if(longitude != e.longitude) return false;
        if(!country.equals(e.country)) return false;
        if(!city.equals(e.city)) return false;
        if(!eventType.equals(e.eventType)) return false;
        if(year != e.year) return false;

        return true;
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
