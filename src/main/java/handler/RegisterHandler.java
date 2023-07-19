package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handler for the /register api endpoint.
 */
public class RegisterHandler extends Handler {
    /**
     * Handles the register operation and returns the result.
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request.
     * @return              A {@link RegisterResult} object containing the result of the register operation.
     * @throws IOException
     */
    @Override
    public RegisterResult processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not POST, return a Result indicating an invalid request
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            return new RegisterResult(false, "Invalid request (HTTP request method should be POST).");
        }

        // Get the contents of the request body
        InputStream requestBody = exchange.getRequestBody();
        String requestData = getRequestData(requestBody);

        // Use GSON to read the request body into a RegisterRequest object
        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(requestData, RegisterRequest.class);

        // Build the service, then execute it. Return the result.
        RegisterService registerService = new RegisterService(request);
        return registerService.register();
    }
}

