package service;

import request.PersonRequest;

/**
 * Serves the /person/[personID] endpoint
 * Finds a specific person in the database
 */
public class PersonService extends Service {
    private PersonRequest request;

    /**
     * Take in the request while contructing the PersonService object
     */
    public PersonService(PersonRequest request) {
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
