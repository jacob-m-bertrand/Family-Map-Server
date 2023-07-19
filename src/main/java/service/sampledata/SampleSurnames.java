package service.sampledata;

import java.util.ArrayList;
import java.util.Random;

/**
 * Stores the sample list of surnames found in \json\snames.json, for use in the {@link service.FillService}.
 */
public class SampleSurnames {
    /** The list of male names */
    private final ArrayList<String> names;

    /**
     * Constructs a new SampleSurname object and populates the {@code names} list.
     */
    public SampleSurnames() {
        /* Pass the location of the JSON file into the NameParser class, which will return an {@code ArrayList<String>}
         of the names. */
        String jsonLocation = "json/fnames.json";
        names = NameParser.parseNames(jsonLocation);
    }

    /**
     * Retrieves a random name from the list.
     * @return A name randomly selected from the list of sample names.
     */
    public String getRandomName() {
        // Generate a random number which wll serve as the index of the name we will retrieve
        Random rand = new Random();
        int index = rand.nextInt(names.size());

        // Retrieve the name at that index and return it
        return names.get(index);
    }

    public ArrayList<String> getNames() {
        return names;
    }
}
