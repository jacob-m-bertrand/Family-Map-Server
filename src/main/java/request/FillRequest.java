package request;

/**
 * Stores data which is passed to FillService (/fill/[username]/{generations})
 */
public class FillRequest {
    /** Store the username to generate for, and number of generations to fill */
    private final String username;
    private final int generations;

    /** Take in fields while constructing the object */
    public FillRequest(String username, int generations) {
        this.username = username;
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public int getGenerations() {
        return generations;
    }
}
