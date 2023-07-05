package service;


import request.LoginRequest;

/**
 * Serves the /user/login endpoint
 * Logs the user in using their Authtoken
 */
public class LoginService extends Service {
    private LoginRequest request;

    /**
     * Take in the request while constructing the LoginRequest object
     */
    public LoginService(LoginRequest request) {
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
