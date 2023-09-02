package request;

/**
 * Stores event data for the event retrieval process in {@link service.EventService}.
 */
public class EventRequest extends AuthenticatedRequest {
    /** The eventID of the requested event. */
    private final String eventID;

    /**
     * Constucts the EventRequest object with the specified eventID and the user's authtoken.
     * @param eventID   The eventID of the requested event.
     * @param authtoken The provided authtoken.
     */
    public EventRequest(String eventID, String authtoken) {
        super(authtoken);
        this.eventID = eventID;
    }

    public String getEventID() {
        return eventID;
    }
}
