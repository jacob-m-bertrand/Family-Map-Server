import com.sun.net.httpserver.HttpServer;
import handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * The main class which runs the server which hosts the API.
 */
public class Server {
    /** The maximum number of waiting connections. */
    private static final int MAX_WAITING_CONNECTIONS = 12;

    /**
     * Runs the server, and sets up handlers for all api endpoints.
     * @param portNumber The number of the port to run on.
     */
    private void run(int portNumber) {
        // Create the server
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(portNumber), MAX_WAITING_CONNECTIONS);

        }
        catch(IOException e) {
            e.printStackTrace();
            return;
        }

        // Set up handlers for each api endpoint
        server.setExecutor(null);

        server.createContext("/user/register", new RegisterHandler());

        server.createContext("/user/login", new LoginHandler());

        server.createContext("/clear", new ClearHandler());

        server.createContext("/fill/", new FillHandler());

        server.createContext("/load", new LoadHandler());

        server.createContext("/person/", new PersonHandler());

        server.createContext("/person", new ListPersonsHandler());

        server.createContext("/event/", new EventHandler());

        server.createContext("/event", new ListEventsHandler());

        // Serve the testing webpage
        server.createContext("/", new FileHandler());

        // Start the serer
        server.start();
    }

    /**
     * Entrypoint for running the API. Takes a parameter for port number.
     * @param args Commandline arguments.
     */
    public static void main(String[] args) {
        // Get the port number from command line arguments
        int portNumber = Integer.parseInt(args[0]);

        // Run the server on the specified port
        new Server().run(portNumber);
    }
}
