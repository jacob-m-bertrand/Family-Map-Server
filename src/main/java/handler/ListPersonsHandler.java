package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import request.AuthenticatedRequest;
import result.ListPersonsResult;
import service.ListPersonsService;

import java.io.IOException;

/**
 * Handler for the /person api endpoint.
 */
public class ListPersonsHandler extends Handler {
    /**
     * Handles the listing of user's persons and returns the result.
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request. 
     * @return              A {@link ListPersonsResult} containing the result of finding the person.
     * @throws IOException  In the case that a stream fails.
     */
    @Override
    protected ListPersonsResult processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not GET, return a ListPersonsResult indicating an invalid request
        if(!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            return new ListPersonsResult(false, "Invalid request (HTTP request method should be GET)");
        }

        
        /* Get the request headers, and store a default value for the authtoken for if the headers don't contain
           the authtoken. */
        Headers requestHeaders = exchange.getRequestHeaders();
        String authtoken = "";

        // Get the authtoken from the headers if it exists
        if (requestHeaders.containsKey("Authorization")) {
            authtoken = requestHeaders.getFirst("Authorization");
        }

        // Build the request and service objects
        AuthenticatedRequest request = new AuthenticatedRequest(authtoken);
        ListPersonsService service = new ListPersonsService(request);

        // Execute the process, and return the result
        return service.listPersons();
    }
}
