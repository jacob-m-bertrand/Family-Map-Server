package result;

import model.Event;

import java.util.ArrayList;

/**
 * Stores the result of the event listing process in {@link service.ListEventsService}.
 */
public class ListEventsResult extends Result {
    /** The list of {@link Event} objects returned by the service. */
    private final ArrayList<Event> data;

    /**
     * Constructs the ListEventsResult with a message.
     * @param success The success status of the service.
     * @param message The message returned by the service.
     */
    public ListEventsResult(boolean success, String message) {
        super(success, message);
        data = null;
    }

    /**
     * Constructs the ListEventsResult with the requested list of people.
     * @param success    The success status of the service.
     * @param data The requested list of {@link Event} objects.
     */
    public ListEventsResult(boolean success, ArrayList<Event> data) {
        super(success);
        this.data = data;
    }

    public ArrayList<Event> getData() {
        return data;
    }
}
