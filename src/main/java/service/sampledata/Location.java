package service.sampledata;

/**
 * Stores data about a specific location which is parsed from the sample locations json.
 */
public class Location {
    /** The country where the location is found. */
    private final String country;

    /** The city where the location is found. */
    private final String city;

    /** The latitude of the location. */
    private final float latitude;

    /** The longitude of the location. */
    private final float longitude;

    /**
     * Constructs a new Location object from the provided information.
     * @param country       The country where the location is found.
     * @param city          The city where the location is found.
     * @param latitude      The latitude of the location.
     * @param longitude     The longitude of the location.
     */
    public Location(String country, String city, float latitude, float longitude) {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
