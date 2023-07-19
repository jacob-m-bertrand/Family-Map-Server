package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import request.AuthenticatedRequest;
import result.ListEventsResult;
import service.ListEventsService;

import java.io.IOException;

/**
 * Handler for the /event api endpoint.
 */
public class ListEventsHandler extends Handler {
    /**
     * Handles the listing of user's events and returns the result.
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request.
     * @return              A {@link ListEventsResult} containing the result of finding the event.
     * @throws IOException  In the case that a stream fails.
     */
    @Override
    protected ListEventsResult processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not GET, return a ListEventsResult indicating an invalid request
        if(!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            return new ListEventsResult(false, "Invalid request (HTTP request method should be GET)");
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
        ListEventsService service = new ListEventsService(request);

        // Execute the process, and return the result
        return service.listEvents();
    }
}
