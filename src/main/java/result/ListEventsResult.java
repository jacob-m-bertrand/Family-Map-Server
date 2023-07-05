package result;

import model.Event;

import java.util.ArrayList;

/**
 * Stores the result of the ListEventsService (/event)
 */
public class ListEventsResult extends Result {
    /** Store an ArrayList of all the events in the database */
    private final ArrayList<Event> data;

    /**
     * Take in result data while constructing the ListEventsResult
     * */
    public ListEventsResult(ArrayList<Event> data, String message, boolean success) {
        super(message, success);
        this.data = data;
    }

    public ArrayList<Event> getData() {
        return data;
    }
}
