package service.sampledata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Helper class which parses the names out of the sample name files.
 */
public class NameParser {
    /**
     * Parses the names out of the given sample name file.
     * @param fileName  The name of sample name file.
     * @return          An {@code ArrayList<String>} of the names parsed from the file.
     */
    public static ArrayList<String> parseNames(String fileName) {
        // Create a new ArrayList of String objects
        ArrayList<String> names = new ArrayList<String>();

        try {
            // Open the json file for reading
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            // Get the json root object, then store its contents int an array
            JsonObject rootObject = (JsonObject) JsonParser.parseReader(fileReader);
            JsonArray locArr = (JsonArray) rootObject.get("data");

            // Loop over the array, and add each name to the array
            for (JsonElement element : locArr) {
                names.add(element.getAsString());
            }

            // Return the array
            return names;
        }
        catch (Exception e) {
            // Caused by the given file being invalid
            throw new RuntimeException(e.getMessage());
        }
    }
}
