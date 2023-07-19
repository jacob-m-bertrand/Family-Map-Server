package handler;

import com.sun.net.httpserver.HttpExchange;
import request.FillRequest;
import result.Result;
import service.FillService;

import java.io.IOException;

/**
 * Handler for the /fill/[username]/{generations} api endpoint.
 */
public class FillHandler extends Handler {
    /**
     * Handles the fill operation and returns the result.
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request.
     * @return              A {@link Result} object containing the result of the fill operation.
     * @throws IOException  In the case that a stream fails.
     */
    @Override
    protected Result processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not POST, return a Result indicating an invalid request
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            return new Result(false, "Invalid request (HTTP request method should be POST).");
        }

        // Get the 1-2 parts of the URI
        // 0: "", 1: "fill", 2: username, 3: generations (if applicable)
        String[] uriParameters = getUriParameters(exchange.getRequestURI());

        String username = uriParameters[2];

        // 4 is the default number of generations to fill, but if number of generations is given, then use that value
        int generationsToFill = 4;
        if (uriParameters.length != 3) {
            generationsToFill = Integer.parseInt(uriParameters[3]);
        }

        // Build the request and service objects
        FillRequest request = new FillRequest(username, generationsToFill);
        FillService fillService = new FillService(request);

        // Execute the fill, then return the result
        return fillService.generateFamilyHistoryData();
    }
}
