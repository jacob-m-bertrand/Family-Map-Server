package request;

import result.EventResult;

/**
 * Stores data which is passed to EventService  (/event/[eventID])
 */
public class EventRequest {
    /** Store eventID which will later be looked up in the database */
    private final String eventID;

    /** Take in the eventID while constructing this object*/
    public EventRequest(String eventID) {
        this.eventID = eventID;
    }

    public String getEventID() {
        return eventID;
    }
}
