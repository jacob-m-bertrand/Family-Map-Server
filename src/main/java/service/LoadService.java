package service;

import request.LoadRequest;

/**
 * Serves the /load endpoint
 * Clears the entire database, and reloads it with a new dataset
 */
public class LoadService extends Service {
    private LoadRequest request;

    /**
     * Take in the request in constructing the LoadService object
     */
    public LoadService(LoadRequest request) {
        this.request = request;
    }

    /**
     * Process the request using DAO and model classes
     * Saves the result to the result variable found in the service superclass
     */
    @Override
    public void process() {

    }
}
