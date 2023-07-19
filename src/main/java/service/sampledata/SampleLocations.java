package service.sampledata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Stores the sample list of locations found in \json\locations.json, for use in the {@link service.FillService}.
 */
public class SampleLocations {
    /** The location of the JSON file we will be pulling from. */
    private static final String jsonLocation = "json/locations.json";

    /** The list of location stored as {@link Location} objects */
    private final ArrayList<Location> locations;

    /**
     * Constructs a new SampleLocations object and populates the {@code locations} list.
     */
    public SampleLocations() {
        // Create a file object for the sample location file
        File jsonFile = new File(jsonLocation);

        // Initialize the location list
        locations = new ArrayList<Location>();

        try {
            // Open a new FileReader to read the JSON file
            FileReader fileReader = new FileReader(jsonFile);

            // Get the root object of the json, then get an array of all the locations within the file
            JsonObject rootObject = (JsonObject) JsonParser.parseReader(fileReader);
            JsonArray locArr = (JsonArray) rootObject.get("data");

            // Iterate over the array, parse the json into a location object
            for (JsonElement e : locArr) {
                JsonObject object = (JsonObject) e;

                // Get the country, remove all double quotes, and double up single quotes (which escapes them in sql)
                String country = object.get("country").toString();
                country = country.replaceAll("^\"|\"$", "");
                country = country.replaceAll("'", "''");

                // Get the city, remove all double quotes, and double up single quotes (which escapes them in sql)
                String city = object.get("city").toString();
                city = city.replaceAll("^\"|\"$", "");
                city = city.replaceAll("'", "''");

                // Get latitude and longitude using the built-in parsing abilities of the Float object
                float latitude = Float.parseFloat(object.get("latitude").toString());
                float longitude = Float.parseFloat(object.get("longitude").toString());

                // Add the new location to the locations list
                locations.add(new Location(country, city, latitude, longitude));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Retrieves a random location from the list.
     * @return A {@code Location} object randomly selected from the list of sample locations.
     */
    public Location getRandomLoc() {
        // Generate a random number which will serve as the index of the location we will retrieve
        Random rand = new Random();
        int index = rand.nextInt(locations.size());

        // Retrieve the location at that index and return it
        return locations.get(index);
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }
}
