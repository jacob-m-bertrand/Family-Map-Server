package model;

import java.util.UUID;

/**
 * Stores an authorization token (authtoken).
 */
public class Authtoken {
    /** The authtoken */
    private final String authtoken;

    /** The username of the user who owns this authtoken */
    private final String username;

    /**
     * Constructs an Authtoken object by generating an authtoken on the spot.
     * @param username The username associated with the new authtoken.
     */
    public Authtoken(String username) {
        this.username = username;
        authtoken = UUID.randomUUID().toString();
    }

    /**
     * Constructs an Authtoken object with both fields given.
     * @param username  The username associated with the authtoken.
     * @param authtoken The authtoken.
     */
    public Authtoken(String username, String authtoken){
        this.username = username;
        this.authtoken = authtoken;
    }

    /**
     * Determines if this Authtoken is equal to another object.
     * @param o The object to compare.
     * @return  {@code true} if this authtoken and {@code o} are equal, {@code false} if they are not.
     */
    @Override
    public boolean equals(Object o) {
        // Check if o is an Authtoken, if not, they are not equal
        if( !(o instanceof Authtoken) ) {
            return false;
        }

        // Create an authtoken object from o, since o is an Authtoken
        Authtoken other = (Authtoken) o;

        // Compare their values
        return username.equals(other.username) && authtoken.equals(other.authtoken);
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }
}
