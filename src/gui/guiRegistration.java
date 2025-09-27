package gui;

import api.User;
import api.fileEditor;
import gui.Errors.ERROR001;
import gui.Errors.ERROR002;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class guiRegistration extends JFrame implements ActionListener {
    JFrame frame;
    JPanel panel;
    JTextField fistNameText;
    JTextField lastNameText;
    JTextField usernameText;
    JPasswordField passwordText;
    JButton register;
    JButton back;

    public guiRegistration() {
        frame = new JFrame();
        frame.setSize(450, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        // First Name
        JLabel firstName = new JLabel("Όνομα:");
        firstName.setBounds(40, 20, 120, 25);
        panel.add(firstName);

        fistNameText = new JTextField();
        fistNameText.setBounds(150, 20, 220, 25);
        panel.add(fistNameText);

        // Last Name
        JLabel lastName = new JLabel("Επίθετο:");
        lastName.setBounds(40, 80, 120, 25);
        panel.add(lastName);

        lastNameText = new JTextField();
        lastNameText.setBounds(150, 80, 220, 25);
        panel.add(lastNameText);

        // Username
        JLabel userName = new JLabel("Username:");
        userName.setBounds(40, 140, 120, 25);
        panel.add(userName);

        usernameText = new JTextField();
        usernameText.setBounds(150, 140, 220, 25);
        panel.add(usernameText);

        // Password
        JLabel password = new JLabel("Κωδικός:");
        password.setBounds(40, 200, 120, 25);
        panel.add(password);

        passwordText = new JPasswordField();
        passwordText.setBounds(150, 200, 220, 25);
        panel.add(passwordText);

        // Εγγραφή
        register = new JButton("Εγγραφή");
        register.setBounds(300, 320, 120, 30);
        register.addActionListener(this);
        panel.add(register);

        // Πίσω
        back = new JButton("Πίσω");
        back.setBounds(20, 320, 120, 30);
        back.addActionListener(this);
        panel.add(back);

        frame.setVisible(true);
        panel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == register) {
            // Εισαγωγή στοιχείων
            String username = usernameText.getText();
            String firstName = fistNameText.getText();
            String lastName = lastNameText.getText();
            String password = new String(passwordText.getPassword());

            // Έλεγχος για κενά πεδία
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                ERROR002 erGUI = new ERROR002();
                erGUI.setVisible(true);
                return;
            }

            // Έλεγχος αν το username υπάρχει ήδη
            File fileXrhsth = new File("Files\\Users\\" + username + ".txt");
            if (fileXrhsth.isFile()) {
                ERROR001 erGUI = new ERROR001(); // Το username υπάρχει ήδη
                erGUI.setVisible(true);
            } else {
                // Δημιουργία νέου χρήστη και αποθήκευση των στοιχείων του
                User neosUser = new User(firstName, lastName, username, password);
                try {
                    // Δημιουργία αρχείου για τον χρήστη
                    fileXrhsth.createNewFile();
                    fileEditor.createFile("Files\\Users\\" + username + ".txt", neosUser.getStoixeia(), "Πελάτης","registration");

                    // Δημιουργία αρχείου για το ιστορικό παραγγελιών
                    File fileIstoriko = new File("Files\\CompletedOrders\\" + username + "Orders.txt");
                    fileIstoriko.createNewFile();
                    fileEditor.createFile("Files\\CompletedOrders\\" + username + "Orders.txt", new ArrayList<>(), "", "completedorders");

                    // Μετάβαση στο παράθυρο σύνδεσης
                    frame.dispose();
                    guiLogin myGUIlogin2 = new guiLogin();
                    myGUIlogin2.setVisible(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == back) {
            // Επιστροφή στο παράθυρο σύνδεσης
            frame.dispose();
            guiLogin myGUIlogin = new guiLogin();
            myGUIlogin.setVisible(true);
        }
    }

}