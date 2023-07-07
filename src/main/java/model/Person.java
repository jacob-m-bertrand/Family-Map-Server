package model;

import java.util.UUID;

public class Person {
    private final String personID;
    private final String associatedUsername;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final String fatherID;
    private final String motherID;
    private final String spouseID;

    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }


    public Person(String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = UUID.randomUUID().toString();
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public Person(String associatedUsername, String firstName, String lastName, String gender) {
        personID = UUID.randomUUID().toString();
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        fatherID = "";
        motherID = "";
        spouseID = "";

    }

    public Person(User user) {
        personID = user.getPersonID();
        associatedUsername = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        gender = user.getGender();
        fatherID = "";
        motherID = "";
        spouseID = "";
    }

    public String toString() {
        return String.format("Person - %s : %s : %s : %s : %s : %s : %s : %s",
                personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Person)) return false;
        Person other = (Person) o;

        return personID.equals(other.personID)
                && associatedUsername.equals(other.associatedUsername)
                && firstName.equals(other.firstName)
                && lastName.equals(other.lastName)
                && gender.equals(other.gender)
                && fatherID.equals(other.fatherID)
                && motherID.equals(other.motherID)
                && spouseID.equals(other.spouseID);
    }

    public String getPersonID() {
        return personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
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

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }
}
