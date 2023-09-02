package model;

import java.util.UUID;

/**
 * Stores all the information for a user.
 */
public class User {
    /** A user's username. Must be unique. */
    private final String username;

    /** The user's password. */
    private final String password;

    /** The user's email address. */
    private final String email;

    /** The user's first name. */
    private final String firstName;

    /** The user's last name. */
    private final String lastName;

    /** The user's gender. */
    private final String gender;

    /** The user's personID. Must be unique. */
    private final String personID;

    /**
     * Constructs a new User object, randomly generating their personID, and using provided data for all other fields.
     * @param username  A user's username. Must be unique.
     * @param password  The user's password.
     * @param email     The user's email address.
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @param gender    The user's gender.
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;

        // Randomly generate a personID using UUID
        this.personID = UUID.randomUUID().toString();
    }

    /**
     * Constructs a new User object, randomly generating their personID, and using provided data for all other fields.
     * @param username  A user's username. Must be unique.
     * @param password  The user's password.
     * @param email     The user's email address.
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @param gender    The user's gender.
     * @param personID  The user's personID. Must be unique.
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * Returns a string containing all the information of the user.
     */
    @Override
    public String toString() {
        return String.format("User - %s : %s : %s : %s : %s : %s : %s",
                username, password, email, firstName, lastName, gender, personID);
    }

    /**
     * Determines if this User is equal to another object.
     * @param o The object to compare.
     * @return  {@code true} if this user and {@code o} are equal, {@code false} if they are not.
     */
    @Override
    public boolean equals(Object o) {
        if ( !(o instanceof User) ) {
            return false;
        }

        User other = (User) o;

        return username.equals(other.username)
                && password.equals(other.password)
                && email.equals(other.email)
                && firstName.equals(other.firstName)
                && lastName.equals(other.lastName)
                && gender.equals(other.gender)
                && personID.equals(other.personID);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

}
