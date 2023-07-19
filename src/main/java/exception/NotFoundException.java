package exception;

/**
 * Exception thrown when something is not found in the database
 */
public class NotFoundException extends Exception {
    /** Provided to guarantee that all exceptions have a message.
     * @return The message of "Not found."
     */
    @Override
    public String getMessage() {
        return "Not found.";
    }
}
