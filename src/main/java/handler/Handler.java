package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import result.Result;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Base class for all handlers.
 */
public abstract class Handler implements HttpHandler {
    /**
     * Main handle class - implements the common code of sending a response, and calls the base class functions to get
     * the results.
     * @param exchange the exchange containing the request from the client and used to send the response
     */
    @Override
    public void handle(HttpExchange exchange) {
        try {
            // Get the result from the specific handler which is involved
            Result handlerResult = processRequest(exchange);

            // Open a new instance of GSON, serialize the result to JSON, and store its length
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(handlerResult);
            byte[] responseBytes = jsonResponse.getBytes();

            /* If the result indicated success, send a 200 code back. If not, send a 400. Prepare the response to be
               the correct length */
            if(handlerResult.isSuccess()) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, responseBytes.length);
            } else {
              exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, responseBytes.length);
            }

            // Get the response body stream, write the response to it
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseBytes);
            responseBody.close();

        } catch (IOException e) {
            // Caused by an error in the built-in Http library
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Processes the request. Implemented differently in every handler.
     * @param exchange      The {@link HttpExchange} object representing the incoming HTTP request.
     * @return              A {@link Result} object containing the result of the process.
     * @throws IOException  In the case that a stream fails.
     */
    protected abstract Result processRequest(HttpExchange exchange) throws IOException;

    /**
     * Gets the data from the request body.
     * @param input         An {@link InputStream} containing the request body.
     * @return              A {@link String} containing the JSON of the request body.
     * @throws IOException  In the case that the InputStream fails.
     */
    protected String getRequestData(InputStream input) throws IOException {
        // Initialize a StringBuilder to store our output
        StringBuilder output = new StringBuilder();

        // Get a reader for the InputStream
        InputStreamReader streamReader = new InputStreamReader(input);

        char[] buffer = new char[1024];
        int length;

        // Read characters from the InputStream into the buffer for as long as there are characters in the InputStream
        while ((length = streamReader.read(buffer)) > 0) {

            // Append the buffer to the output
            output.append(buffer, 0, length);
        }

        return output.toString();
    }

    /**
     * Gets the parameters from a URI.
     * @param requestUri    The URI of the request.
     * @return              An array of String objects containing the URI parameters.
     */
    protected String[] getUriParameters(URI requestUri) {
        String path = requestUri.getPath();

        // Split the path along each /
        return path.split("/");
    }
}
