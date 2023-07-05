package result;

/**
 * Base class for all result objects
 * Stores results directly for ClearService, FillService, and LoadService as these services output only success status and message
 */
public class Result {
    private final String message;
    private final boolean success;

    public Result(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
