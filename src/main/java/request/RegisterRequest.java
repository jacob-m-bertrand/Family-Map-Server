package request;

/**
 * Stores data which will be passed to RegisterService (/user/register)
 */
public class RegisterRequest {
    /** Store the information for the new user */
    private final String username;
    private final String password;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String gender;

    /** Take in the fields while constructing this object */
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
