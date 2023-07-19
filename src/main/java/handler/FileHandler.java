package handler;

import com.sun.net.httpserver.HttpExchange;
import result.Result;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handler for the / api endpoint (web files)
 */
public class FileHandler extends Handler {
    /**
     * Handle class - manages the receiving and sending of HTTP requests
     * @param exchange the exchange containing the request from the client and used to send the response
     */
    @Override
    public void handle(HttpExchange exchange) {
        try {
            // Get the result from running the service
            Result handlerResult = processRequest(exchange);

            // Store a string for the file path, which is stored in the result message (if successful)
            String filePath = handlerResult.getMessage();

            /* If the result indicated success, send a 200 code back. If not, send a 404. Prepare the response to be
               the correct length */
            if(handlerResult.isSuccess()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);

                // If 404, serve the 404 page
                filePath = "web/html/404.html";
            }

            // Send the appropriate file back to the requestor
            OutputStream responseBody = exchange.getResponseBody();
            Files.copy(Path.of(filePath), responseBody);
            responseBody.close();

        } catch (IOException e) {
            // Caused by an error in the built-in Http library
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Processes the file request.
     * @param exchange The {@link HttpExchange} object representing the incoming HTTP request.
     * @return A {@link Result} object containing the result of the process.
     * @throws IOException In the case that a stream fails.
     */
    @Override
    protected Result processRequest(HttpExchange exchange) throws IOException {
        // Get the HTTP request method
        String requestMethod = exchange.getRequestMethod();

        // If the request method is not POST, return a Result indicating an invalid request
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            return new Result(false, "Invalid request (HTTP request method should be GET).");
        }

        // Get the requested uri path
        String requestUri = exchange.getRequestURI().toString();
        String uriPath = requestUri.substring(requestUri.indexOf('/'));

        // Store a file path, which is always prepended by 'web' as this is the folder with the web files in it
        String filePath = "web";

        // If no file path is given (just a '/'), the index is requested
        if (uriPath.equals("/")) {
            filePath += "/index.html";

        } else {
            filePath += uriPath;
        }

        // Make a file object for the requested File
        File requestedFile = new File(filePath);

        // If the file exists, return a successful result and the file path in the message.
        if (requestedFile.exists()) {
            return new Result(true, filePath);

        } else {
            return new Result(false);
        }
    }
}
