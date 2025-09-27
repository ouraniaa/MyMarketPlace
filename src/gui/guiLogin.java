package gui;

import api.UserLoginVerification;
import gui.guiCustomer.MainMenuCustomer;
import gui.guiManager.MainMenuManager;
import api.fileEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;

import static api.UserLoginVerification.verified;

public class guiLogin extends JFrame implements ActionListener{
    public static String activeUser;
    public JFrame frame;
    JPanel panel;
    JButton login;
    JButton reg;
    static JTextField usernameText;
    JPasswordField passwordText;

    public guiLogin(){
        frame = new JFrame();
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel username = new JLabel("Username:");
        username.setBounds(15,15,80,25);
        panel.add(username);

        usernameText = new JTextField();
        usernameText.setBounds(100,15,170,25);
        panel.add(usernameText);

        JLabel password = new JLabel("Password");
        password.setBounds(15,50,80,25);
        panel.add(password);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50,170,25);
        panel.add(passwordText);

        login = new JButton("Login");
        login.setBounds(100, 85, 100,30);
        panel.add(login);
        login.addActionListener(this);

        reg = new JButton("Sign up");
        reg.setBounds(100, 125,100,30);
        panel.add(reg);
        reg.addActionListener(this);

        // Προσθήκη KeyListener για τα πεδία
        usernameText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Αν πατηθεί το Enter
                    performLogin(); // Κάνει login
                }
            }
        });

        passwordText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Αν πατηθεί το Enter
                    performLogin(); // Κάνει login
                }
            }
        });

        frame.setVisible(true);
        panel.setVisible(true);
    }

    private void performLogin() {
        String username = usernameText.getText();
        String password = passwordText.getText();
        activeUser = usernameText.getText();

        UserLoginVerification.Verify(username, password, "Files\\Users\\" + username + ".txt");

        if (verified == true) {
            try {
                if (fileEditor.read("Files\\Users\\" + username + ".txt", 5).equals("Διαχειριστής")) {
                    frame.dispose();
                    MainMenuManager myGUI = new MainMenuManager(username);
                    myGUI.frame.setVisible(true);
                } else if (fileEditor.read("Files\\Users\\" + username + ".txt", 5).equals("Πελάτης")) {
                    frame.dispose();
                    MainMenuCustomer myGUI = new MainMenuCustomer(username);
                    myGUI.frame.setVisible(true);
                }
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == reg) {
            frame.dispose();
            guiRegistration myGui = new guiRegistration();
            myGui.frame.setVisible(true);
        }

        if (e.getSource().equals(login)) {
            performLogin(); // Κλήση της συνάρτησης login όταν πατηθεί το κουμπί
        }
    }
}
