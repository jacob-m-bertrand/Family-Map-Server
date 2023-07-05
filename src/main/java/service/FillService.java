package service;

import request.FillRequest;

/**
 * Serves the /fill/[username]/{generations} endpoint
 * fills the database with family history data
 */
public class FillService extends Service {
    private FillRequest request;

    /**
     * Take in the request while constructing the FillService
     * @param request request object with information relevant to processing the request
     */
    public FillService(FillRequest request) {
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
