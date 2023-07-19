package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import request.EventRequest;
import result.EventResult;
import service.EventService;

import java.io.IOException;

/**
 * Handler for the /event/[eventID] api endpoint.
 */
public class EventHandler extends Handler {
    /**
     * Executes finding of the specified event, and returns the result.
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request.
     * @return              An {@link EventResult} object containing the result of finding the event.
     * @throws IOException  In the case that a stream fails.
     */
    @Override
    protected EventResult processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not GET, return an EventResult indicating an invalid request
        if (!requestMethod.equalsIgnoreCase("get")) {
            return new EventResult(false, "Invalid request (HTTP request method should be GET)");
        }

        /* Get the request headers, and store a default value for the authtoken for if the headers don't contain
           the authtoken. */
        Headers requestHeaders = exchange.getRequestHeaders();
        String authtoken = "";

        // Get the authtoken from the headers if it exists
        if (requestHeaders.containsKey("Authorization")) {
            authtoken = requestHeaders.getFirst("Authorization");
        }

        // Get the eventID from the URI
        String[] uriParameters = getUriParameters(exchange.getRequestURI());
        String eventID = uriParameters[2]; // 0: "", 1: "event", 2: eventID

        // Build the request and service objects
        EventRequest request = new EventRequest(eventID, authtoken);
        EventService eventService = new EventService(request);

        // Execute the find and return the results
        return eventService.findEvent();
    }
}
