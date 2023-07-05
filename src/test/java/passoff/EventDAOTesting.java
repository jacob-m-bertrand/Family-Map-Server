package passoff;

import dao.EventDAO;

import java.sql.*;
import java.util.ArrayList;

import model.Person;
import model.User;
import org.junit.jupiter.api.*;

import model.Event;

import static org.junit.jupiter.api.Assertions.fail;

public class EventDAOTesting {
    private EventDAO dao;
    private Connection db;
    private String url = "jdbc:sqlite:C:\\Users\\jacob\\OneDrive - Brigham Young University\\School\\Summer 2023\\C S 240\\FamilyMap SQL\\FMS.db";
    private User user;
    private User otherUser;
    private Person userPerson;
    private ArrayList<Event> events;

    @BeforeEach
    @DisplayName("Initializing DAO with sample data")
    void setup() {
        user = new User("jmbertrand", "cs240isgreat", "jacobbertrand22@gmail.com", "Jacob", "Bertrand", "m");
        otherUser = new User("BaguetteBird", "cs240isnotgreat", "baguette@bird.com", "Baguette", "Bird", "f");
        userPerson = new Person(user);

        events.add(new Event())

        try {
            db = DriverManager.getConnection(url);
            dao = new EventDAO(db);

            db.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    @DisplayName("Unwinding")
    void close() {
        try {
            db.rollback();
            db.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Insert Testing")
    void insertTesting() {

    }
}
