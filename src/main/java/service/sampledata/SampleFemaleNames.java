package service.sampledata;

import java.util.ArrayList;
import java.util.Random;

/**
 * Stores the sample list of female names found in \json\fnames.json, for use in the {@link service.FillService}.
 */
public class SampleFemaleNames {
    /** The list of female names */
    private final ArrayList<String> names;

    /**
     * Constructs a new SampleFemaleNames object and populates the {@code names} list.
     */
    public SampleFemaleNames() {
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
