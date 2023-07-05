package result;

import model.Event;

/**
 * Stores the result of the EventService (/event/[eventID])
 */
public class EventResult extends Result {
    /** The event object requested by the user, and found in the EventService class */
    private final Event event;

    /**
     * Take in result information in constructing the EventResult object
     */
    public EventResult(String message, boolean success, Event event) {
        super(message, success);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
