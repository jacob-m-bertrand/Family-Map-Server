package model;

import java.util.UUID;
import model.Person;

public class Person {
    private final String personID;
    private final String associatedUsername;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final String fatherID;
    private final String motherID;
    private final String spouseID;

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

    public Person(User user) {
        personID = user.getPersonID();
        associatedUsername = user.getUsername();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        gender = user.getGender();
        fatherID = null;
        motherID = null;
        spouseID = null;
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
