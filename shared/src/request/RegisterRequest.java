package request;

/**
 * Stores user data for the registration process in the {@link service.RegisterService}.
 */
public class RegisterRequest {
    /** The user's chosen username. It must be unique. */
    private final String username;

    /** The user's chosen password. */
    private final String password;

    /** The user's email address */
    private final String email;

    /** The user's first name. It will be used later to create the user's Person in the database */
    private final String firstName;

    /** The user's last name. It will be used later to create the user's Person in the database */
    private final String lastName;

    /** The user's gender. It will be used later to create the user's Person in the database */
    private final String gender;

    /**
     * Constructs a new RegisterRequest object with the specified user details.
     *
     * @param username  The chosen username for the user. Must be unique.
     * @param password  The chosen password for the user.
     * @param email     The email address of the user.
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param gender    The gender of the user.
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
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
}
