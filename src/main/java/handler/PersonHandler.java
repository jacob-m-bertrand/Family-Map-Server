package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import request.PersonRequest;
import result.PersonResult;
import service.PersonService;

import java.io.IOException;

/**
 * Handler for the /person/[personID] api endpoint.
 */
public class PersonHandler extends Handler {
    /**
     * Executes finding of the specified person, and returns the result.
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request.
     * @return              An {@link PersonResult} object containing the result of finding the person.
     * @throws IOException  In the case that a stream fails.
     */
    @Override
    protected PersonResult processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not GET, return an PersonResult indicating an invalid request
        if (!requestMethod.equalsIgnoreCase("get")) {
            return new PersonResult(false, "Invalid request (HTTP request method should be GET)");
        }

        /* Get the request headers, and store a default value for the authtoken for if the headers don't contain
           the authtoken. */
        Headers requestHeaders = exchange.getRequestHeaders();
        String authtoken = "";

        // Get the authtoken from the headers if it exists
        if (requestHeaders.containsKey("Authorization")) {
            authtoken = requestHeaders.getFirst("Authorization");
        }

        // Get the personID from the URI
        String[] uriParameters = getUriParameters(exchange.getRequestURI());
        String personID = uriParameters[2]; // 0: "", 1: "person", 2: personID

        // Build the request and service objects
        PersonRequest request = new PersonRequest(personID, authtoken);
        PersonService personService = new PersonService(request);

        // Execute the find and return the results
        return personService.findPerson();
    }
}
