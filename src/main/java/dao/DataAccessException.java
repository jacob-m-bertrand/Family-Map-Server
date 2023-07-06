package dao;

public class DataAccessException extends Exception {
    private String message;

    public DataAccessException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
