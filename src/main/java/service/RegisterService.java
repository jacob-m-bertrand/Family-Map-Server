package service;

import request.RegisterRequest;

/**
 * Serves the /user/register endpoint
 * Registers a user into the database, and generates family history data for them
 */
public class RegisterService extends Service {
    private RegisterRequest request;

    /**
     * Take in the request while constructing the RegisterService object
     */
    public RegisterService(RegisterRequest request) {
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
