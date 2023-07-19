package result;

/**
 * Base class for Result objects, and used when no further information is returned in a service.
 */
public class Result {
    /** The success status of the service. */
    private final boolean success;

    /** The message returned by the service, used when a service fails. */
    private final String message;

    /**
     * Constructs a new Result object with a message.
     * @param success The provided success status.
     * @param message The provided message;
     */
    public Result(boolean success, String message) {
        this.message = message;
        this.success = success;
    }

    /**
     * Constructs a new Result object with success status only. Sets message to an empty string as it will not be used.
     * @param success The provided success status.
     */
    public Result(boolean success) {
        this.message = null;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
