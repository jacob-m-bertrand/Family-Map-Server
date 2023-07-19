package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.LoadRequest;
import result.Result;
import service.LoadService;

import java.io.IOException;
import java.io.InputStream;

/**
 * Handler for the /load api endpoint.
 */
public class LoadHandler extends Handler {
    /**
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request.
     * @return              A {@link Result} object containing the result of loading the database.
     * @throws IOException  In the case that a stream fails.
     */
    @Override
    public Result processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not POST, return a Result indicating an invalid request
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            return new Result(false, "Invalid request (HTTP request method should be POST).");
        }

        // Get the contents of the request body
        InputStream requestBody = exchange.getRequestBody();
        String requestData = getRequestData(requestBody);

        // Use GSON to read the request body into a LoadRequest object
        Gson gson = new Gson();
        LoadRequest request = gson.fromJson(requestData, LoadRequest.class);

        // Build the service, then execute it. Return the result.
        LoadService loadService = new LoadService(request);
        return loadService.load();
    }
}
