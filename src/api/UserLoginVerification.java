/**
 * Η κλάση αυτή χρησιμοποιείται για την επαλήθευση του username και password του χρήστη .
 * @author 4256
 */

package api;
import gui.Errors.LoginERROR1;
import gui.Errors.LoginERROR2;
import java.io.*;
import java.util.Scanner;

public class UserLoginVerification {
    private static Scanner scan;
    public static boolean verified; // Χρησιμοποιείται για να δηλώσει αν έγινε επιτυχής σύνδεση

    public static void Verify(String name, String password, String filePath) {
        verified = false; // Αρχικοποίηση

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                // Το αρχείο δεν υπάρχει
                System.out.println("File does not exist: " + filePath);
                LoginERROR1 error1 = new LoginERROR1();
                return;
            }

            scan = new Scanner(file);
            int lineNumber = 0;
            String usernameFromFile = "";
            String passwordFromFile = "";

            // Διαβάζουμε το αρχείο γραμμή-γραμμή
            while (scan.hasNextLine()) {
                String tempLine = scan.nextLine().trim();
                lineNumber++;

                if (lineNumber == 3) {
                    usernameFromFile = tempLine; // Αποθηκεύουμε το username από τη 3η γραμμή
                } else if (lineNumber == 4) {
                    passwordFromFile = tempLine; // Αποθηκεύουμε το password από τη 4η γραμμή
                }

                if (lineNumber >= 4) {
                    break;
                }
            }

            // Έλεγχος username και password
            if (usernameFromFile.equals(name) && passwordFromFile.equals(password)) {
                verified = true;
            } else {
                verified = false;
                LoginERROR2 error2 = new LoginERROR2(); // Λάθος username ή password
            }

        } catch (Exception e) {
            e.printStackTrace(); // Εκτύπωση σφάλματος για debugging
            LoginERROR1 error1 = new LoginERROR1();
        } finally {
            if (scan != null) {
                scan.close();
            }
        }
    }
}