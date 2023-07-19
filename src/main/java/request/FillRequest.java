package request;

/**
 * Stores information for generating family history data in the FillService.
 */
public class FillRequest {
    /** The username to generate data for. */
    private final String username;

    /** The number of generations to generate. */
    private final int generations;


    /**
     * Constructs the FillRequest using data passed in.
     * @param username      The username for whom the fill will be performed.
     * @param generations   The number of generations to generate.
     */
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
