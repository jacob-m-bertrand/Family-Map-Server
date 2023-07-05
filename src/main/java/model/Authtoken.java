package model;

import java.util.UUID;

/**
 * Stores an authorization token for a user
 */
public class Authtoken {
    private String authtoken;
    private String username;

    public Authtoken(String username) {
        this.username = username;
        authtoken = UUID.randomUUID().toString();
    }
}
