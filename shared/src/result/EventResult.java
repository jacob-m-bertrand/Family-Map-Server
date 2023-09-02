package result;

import model.Event;

/**
 * Stores the result the event retrieval process in {@link service.EventService}.
 */
public class EventResult extends Result {
    /** The ID of the event from the database. Must be unique. */
    private final String eventID;

    /** The username associated with this event. */
    private final String associatedUsername;

    /** The person this event happened to. */
    private final String personID;

    /** The latitude of the location where the event occurred. */
    private final Float latitude;

    /** The longitude of the location where the event occurred. */
    private final Float longitude;

    /** The country where the event occurred. */
    private final String country;

    /** The city where the event occurred. */
    private final String city;

    /** The type of event. */
    private final String eventType;

    /** The year in which the event occurred. */
    private final Integer year;

    /**
     * Constructs a new EventResult with the retrieved event.
     * @param success The success status of the service.
     * @param event   The retrieved event.
     */
    public EventResult(boolean success, Event event) {
        super(success);
        eventID = event.getEventID();
        associatedUsername = event.getAssociatedUsername();
        personID = event.getPersonID();
        latitude = event.getLatitude();
        longitude = event.getLongitude();
        country = event.getCountry();
        city = event.getCity();
        eventType = event.getEventType();
        year = event.getYear();
    }

    /**
     * Constructs a new EventResult with a message.
     * @param success The success status of the service.
     * @param message The returned message.
     */
    public EventResult(boolean success, String message) {
        super(success, message);
        eventID = null;
        associatedUsername = null;
        personID = null;
        latitude = null;
        longitude = null;
        country = null;
        city = null;
        eventType = null;
        year = null;
    }

    /**
     * Turns this result into an event object, for testing purposes.
     * @return An event with the data contained in this result.
     */
    public Event toEvent() {
        if (eventID == null) {
            return null;
        } else {
            return new Event(
                    eventID,
                    associatedUsername,
                    personID,
                    latitude,
                    longitude,
                    country,
                    city,
                    eventType,
                    year
            );
        }
    }
}
