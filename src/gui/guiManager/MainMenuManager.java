package gui.guiManager;

import api.ShoppingCart;
import gui.guiLogin;
import gui.guiSearchProduct;

import static gui.guiLogin.activeUser;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuManager extends JFrame implements ActionListener {
    public JFrame frame;
    JPanel panel;
    JButton addProductButton;
    JButton searchButton;
    JButton viewStatsButton;
    JButton logoutButton;
    private boolean isAdmin = true; // Ο χρήστης είναι διαχειριστής
    private ShoppingCart shoppingCart;
    private String username;

    public MainMenuManager(String username){
        this.shoppingCart = new ShoppingCart();
        this.username = username;

        frame = new JFrame();
        frame.setSize(600,450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel menu = new JLabel("Κεντρικό Μενού Διαχειριστή");
        menu.setBounds(200, 20, 250, 25);
        panel.add(menu);

        addProductButton = new JButton("Προσθήκη προϊόντος");
        addProductButton.setBounds(200, 70, 200, 25);
        panel.add(addProductButton);
        addProductButton.addActionListener(this);

        viewStatsButton = new JButton("Προβολή Στατιστικών");
        viewStatsButton.setBounds(200, 130, 200, 25);
        panel.add(viewStatsButton);
        viewStatsButton.addActionListener(this);

        searchButton = new JButton("Αναζήτηση");
        searchButton.setBounds(200, 190,200,25);
        panel.add(searchButton);
        searchButton.addActionListener(this);

        logoutButton = new JButton("Αποσύνδεση");
        logoutButton.setBounds(200, 260, 200, 25);
        logoutButton.setBackground(new java.awt.Color(146, 203, 208, 255));
        panel.add(logoutButton);
        logoutButton.addActionListener(this);

        frame.setVisible(true);
        panel.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addProductButton) {
            insertProduct insertProductGUI = new insertProduct();
            insertProductGUI.frame.setVisible(true);
        }

        if (e.getSource() == viewStatsButton) {
            viewStatistics statsGUI = new viewStatistics();
            statsGUI.frame.setVisible(true);
        }

        if(e.getSource() == searchButton){
            guiSearchProduct searchGUI = new guiSearchProduct(isAdmin, shoppingCart, username);
            searchGUI.frame.setVisible(true);
        }

        if (e.getSource() == logoutButton) {
            activeUser = null;
            frame.dispose();
            guiLogin loginGUI = new guiLogin();
            loginGUI.frame.setVisible(true);
        }
    }
}
