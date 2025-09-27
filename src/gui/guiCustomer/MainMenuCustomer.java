package gui.guiCustomer;

import gui.guiLogin;
import gui.guiSearchProduct;
import api.ShoppingCart;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuCustomer extends JFrame implements ActionListener {
    public JFrame frame;
    JPanel panel;
    JButton searchButton;
    JButton orderHistoryButton;
    JButton logoutButton;
    JButton cartButton; // Καλάθι Αγορών
    private ShoppingCart shoppingCart;
    private boolean isAdmin = false;
    private String username;

    public MainMenuCustomer(String username) {
        shoppingCart = new ShoppingCart();
        this.username = username;

        frame = new JFrame();
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel menu = new JLabel("Κεντρικό Μενού Πελάτη");
        menu.setBounds(225, 60, 250, 25);
        panel.add(menu);

        searchButton = new JButton("Αναζήτηση");
        searchButton.setBounds(200, 100, 200, 25);
        panel.add(searchButton);
        searchButton.addActionListener(this);

        orderHistoryButton = new JButton("Ιστορικό Παραγγελιών");
        orderHistoryButton.setBounds(200, 140, 200, 25);
        panel.add(orderHistoryButton);
        orderHistoryButton.addActionListener(this);

        logoutButton = new JButton("Αποσύνδεση");
        logoutButton.setBounds(200, 350, 200, 25);
        logoutButton.setBackground(new java.awt.Color(146, 203, 208, 255));
        panel.add(logoutButton);
        logoutButton.addActionListener(this);

        cartButton = new JButton("Καλάθι Αγορών");
        cartButton.setBounds(450, 10, 130, 25);
        panel.add(cartButton);
        cartButton.addActionListener(this);

        frame.setVisible(true);
        panel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            // Ανοίγει το παράθυρο αναζήτησης προϊόντων
            guiSearchProduct searchGUI = new guiSearchProduct(isAdmin, shoppingCart, username);
            searchGUI.frame.setVisible(true);
        }

        if (e.getSource() == orderHistoryButton) {
            // Ανοίγει το παράθυρο ιστορικού παραγγελιών
            new guiOrderHistory(); // Εισάγουμε το username για να φορτώσουμε το ιστορικό
        }

        if (e.getSource() == logoutButton) {
            // Αποσύνδεση του χρήστη και επιστροφή στο login
            frame.dispose();
            guiLogin loginGUI = new guiLogin();
            loginGUI.frame.setVisible(true);
        }

        if (e.getSource() == cartButton) {
            // Ανοίγει το καλάθι αγορών
            new guiShoppingCart(shoppingCart);
        }
    }
}
