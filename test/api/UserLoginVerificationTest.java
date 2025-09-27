package api;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class UserLoginVerificationTest {

    private static final String TEST_FILE_PATH = "test_user_data.txt";

    /**
     * Δημιουργεί ένα προσωρινό αρχείο για τις δοκιμές.
     */
    private void createTestFile(String content) throws IOException {
        FileWriter writer = new FileWriter(TEST_FILE_PATH);
        writer.write(content);
        writer.close();
    }

    /**
     * Διαγράφει το προσωρινό αρχείο μετά τις δοκιμές.
     */
    private void deleteTestFile() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testVerifyWithCorrectCredentials() throws IOException {
        // Δημιουργία αρχείου με σωστά διαπιστευτήρια
        createTestFile("Line1\nLine2\ncorrectUsername\ncorrectPassword");

        // Κλήση της μεθόδου Verify
        UserLoginVerification.Verify("correctUsername", "correctPassword", TEST_FILE_PATH);

        // Επιβεβαίωση ότι η μεταβλητή verified έγινε true
        assertTrue(UserLoginVerification.verified);

        deleteTestFile();
    }

    @Test
    public void testVerifyWithIncorrectCredentials() throws IOException {
        // Δημιουργία αρχείου με σωστά διαπιστευτήρια
        createTestFile("Line1\nLine2\ncorrectUsername\ncorrectPassword");

        // Κλήση της μεθόδου Verify με λάθος password
        UserLoginVerification.Verify("correctUsername", "wrongPassword", TEST_FILE_PATH);

        // Επιβεβαίωση ότι η μεταβλητή verified είναι false
        assertFalse(UserLoginVerification.verified);

        deleteTestFile();
    }

    @Test
    public void testVerifyFileDoesNotExist() {
        // Κλήση της μεθόδου Verify με μη-υπαρκτό αρχείο
        UserLoginVerification.Verify("username", "password", "non_existing_file.txt");

        // Επιβεβαίωση ότι η μεταβλητή verified είναι false
        assertFalse(UserLoginVerification.verified);
    }

    @Test
    public void testVerifyEmptyFile() throws IOException {
        // Δημιουργία άδειου αρχείου
        createTestFile("");

        // Κλήση της μεθόδου Verify
        UserLoginVerification.Verify("username", "password", TEST_FILE_PATH);

        // Επιβεβαίωση ότι η μεταβλητή verified είναι false
        assertFalse(UserLoginVerification.verified);

        deleteTestFile();
    }

    @Test
    public void testVerifyWithPartialData() throws IOException {
        // Δημιουργία αρχείου με ελλιπή δεδομένα (μόνο username)
        createTestFile("Line1\nLine2\npartialUsername\n");

        // Κλήση της μεθόδου Verify
        UserLoginVerification.Verify("partialUsername", "anyPassword", TEST_FILE_PATH);

        // Επιβεβαίωση ότι η μεταβλητή verified είναι false
        assertFalse(UserLoginVerification.verified);

        deleteTestFile();
    }
}
