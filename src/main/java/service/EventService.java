package service;

import request.EventRequest;

import java.util.ServiceConfigurationError;

/**
 * Serves the /event/[eventID] endpoint
 * Finds a specific event in the database
 */
public class EventService extends Service {
    private EventRequest request;


    /**
     * Take in requests on construction
     */
    public EventService(EventRequest request) {
        this.request = request;
    }

    /**
     * Processes by working with DAO and model classes
     * Saves the result to the result variable (in service class)
     */
    @Override
    public void process() {}
}
