package handler;

import com.sun.net.httpserver.HttpExchange;
import result.LoginResult;
import result.Result;
import service.ClearService;

/**
 * Handler for the /clear api endpoint.
 */
public class ClearHandler extends Handler {

    /**
     * Implements the clearing functionality of the handler.
     * @param exchange The HttpExchange object representing the incoming HTTP request.
     * @return A Result object containing the result of the clearing operation.
     */
    @Override
    protected Result processRequest(HttpExchange exchange) {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not POST, return a LoginResult indicating an invalid request
        if (!requestMethod.equalsIgnoreCase("post")) {
            return new LoginResult(false, "Invalid request (HTTP request method should be POST).");
        }

        // Create an instance of the ClearService to perform the clearing operation
        ClearService clearService = new ClearService();

        // Call the clear method of ClearService to clear the data, and return the result of the clear
        return clearService.clear();
    }
}
