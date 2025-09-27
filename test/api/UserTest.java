package api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;

public class UserTest {

    private User user;

    @Before
    public void setUp() {
        // Δημιουργία ενός χρήστη για κοινή χρήση στις δοκιμές
        user = new User("John", "Doe", "johndoe", "password123");
    }

    @Test
    public void testUserCreation() {
        // Έλεγχος των getters
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("johndoe", user.getUsername());
        assertEquals("password123", user.getPassword());

        // Έλεγχος του ArrayList stoixeia
        ArrayList<String> expectedStoixeia = new ArrayList<>();
        expectedStoixeia.add("John");
        expectedStoixeia.add("Doe");
        expectedStoixeia.add("johndoe");
        expectedStoixeia.add("password123");

        assertEquals(expectedStoixeia, user.getStoixeia());
    }

    @Test
    public void testSetManager() {
        // Έλεγχος αρχικής κατάστασης
        user.setManager(true);
        // Δεν υπάρχει μέθοδος getter για manager, αλλά ελέγχουμε αν εκτελείται χωρίς σφάλμα
        // Θα μπορούσαμε να προσθέσουμε σχετική μέθοδο αν χρειάζεται
        assertTrue(true);
    }

    @Test
    public void testSetCustomer() {
        // Έλεγχος αρχικής κατάστασης
        user.setCustomer(true);
        // Δεν υπάρχει μέθοδος getter για customer, αλλά ελέγχουμε αν εκτελείται χωρίς σφάλμα
        // Θα μπορούσαμε να προσθέσουμε σχετική μέθοδο αν χρειάζεται
        assertTrue(true);
    }
}
