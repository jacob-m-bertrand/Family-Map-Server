package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handler for the /login api endpoint.
 */
public class LoginHandler extends Handler {
    /**
     * Handles the login operation and returns the result.
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request.
     * @return              A {@link LoginResult} object containing the result of the login operation.
     * @throws IOException
     */
    @Override
    public LoginResult processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not POST, return a Result indicating an invalid request
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            return new LoginResult(false, "Invalid request (HTTP request method should be POST).");
        }

        // Get the contents of the request body
        InputStream requestBody = exchange.getRequestBody();
        String requestData = getRequestData(requestBody);

        // Use GSON to read the request body into a LoginRequest object
        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(requestData, LoginRequest.class);

        // Build the service, then execute it. Return the result.
        LoginService loginService = new LoginService(request);
        return loginService.login();
    }
}

