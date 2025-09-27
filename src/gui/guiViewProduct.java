package gui;

import api.ShoppingCart;
import api.fileEditor;
import gui.guiManager.editProduct;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

public class guiViewProduct extends JFrame implements ActionListener {
    public JFrame frame;
    JPanel panel;
    JTextField quantityField;
    JButton backButton;
    JButton editButton;
    JButton addToCartButton;
    private boolean isAdmin;
    private String name;
    private ShoppingCart shoppingCart;

    public guiViewProduct(boolean isAdmin, String name, ShoppingCart shoppingCart) throws FileNotFoundException {
        this.isAdmin = isAdmin;
        this.name = name;
        this.shoppingCart = shoppingCart;

        frame = new JFrame("Προβολή Προϊόντος");
        frame.setSize(550, 450);
        //frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        // Εμφάνιση τίτλου προϊόντος
        JLabel name1 = new JLabel("Τίτλος: ");
        name1.setBounds(20, 20, 450, 25);
        panel.add(name1);

        JLabel name2 = new JLabel(name);
        name2.setBounds(150, 20, 120, 25);
        panel.add(name2);

        // Εμφάνιση κατηγορίας προϊόντος
        JLabel cat = new JLabel("Κατηγορία: ");
        cat.setBounds(20, 60, 450, 25);
        panel.add(cat);

        String cat1 = fileEditor.getProductDetailByTitle("Files\\Products.txt", name, "Κατηγορία");

        JLabel cat2 = new JLabel(cat1);
        cat2.setBounds(150, 60, 120, 25);
        panel.add(cat2);

        JLabel sub = new JLabel("Υποκατηγορία: ");
        sub.setBounds(20, 100, 450, 25);
        panel.add(sub);

        String sub1 = fileEditor.getProductDetailByTitle("Files\\Products.txt", name, "Υποκατηγορία");

        JLabel sub2 = new JLabel(sub1);
        sub2.setBounds(150, 100, 120, 25);
        panel.add(sub2);

        // Εμφάνιση περιγραφής προϊόντος
        JLabel desc = new JLabel("Περιγραφή: ");
        desc.setBounds(20, 140, 450, 25);
        panel.add(desc);

        String desc1 = fileEditor.getProductDetailByTitle("Files\\Products.txt", name, "Περιγραφή");

        JLabel desc2 = new JLabel(desc1);
        desc2.setBounds(150, 140, 320, 25);
        panel.add(desc2);

        // Εμφάνιση τιμής προϊόντος
        JLabel price = new JLabel("Τιμή: ");
        price.setBounds(20, 180, 450, 25);
        panel.add(price);

        String price1 = fileEditor.getProductDetailByTitle("Files\\Products.txt", name, "Τιμή");

        JLabel price2 = new JLabel(price1);
        price2.setBounds(150, 180, 120, 25);
        panel.add(price2);

        JLabel quan = new JLabel("Διαθεσιμότητα: ");
        quan.setBounds(20, 220, 450, 25);
        panel.add(quan);

        String quan1 = fileEditor.getProductDetailByTitle("Files\\Products.txt", name, "Ποσότητα");

        JLabel quan2 = new JLabel(quan1);
        quan2.setBounds(150, 220, 120, 25);
        panel.add(quan2);

        // Πεδίο για προσδιορισμό ποσότητας (μόνο για πελάτες)
        if (!isAdmin) {
            JLabel quantityLabel = new JLabel("Ποσότητα:");
            quantityLabel.setBounds(20, 300, 100, 25);
            panel.add(quantityLabel);

            quantityField = new JTextField();
            quantityField.setBounds(100, 300, 100, 25);
            panel.add(quantityField);

            addToCartButton = new JButton("Προσθήκη στο καλάθι");
            addToCartButton.setBounds(220, 300, 200, 25);
            addToCartButton.addActionListener(this);
            panel.add(addToCartButton);
        }

        // Κουμπί επιστροφής
        backButton = new JButton("Πίσω");
        backButton.setBounds(150, 340, 180, 30);
        backButton.addActionListener(this);
        panel.add(backButton);

        // Κουμπί επεξεργασίας (μόνο για διαχειριστές)
        if (isAdmin) {
            editButton = new JButton("Επεξεργασία Προϊόντος");
            editButton.setBounds(150, 260, 180, 30);
            editButton.addActionListener(this);
            panel.add(editButton);
        }

        panel.setVisible(true);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            frame.dispose();
        } else if (e.getSource() == editButton) {
            frame.dispose();
            editProduct editWindow = new editProduct(name);
            editWindow.setVisible(true);
        } else if (e.getSource() == addToCartButton) {
            // Διαβάζουμε την ποσότητα από το πεδίο εισαγωγής
            String quantityText = quantityField.getText().trim();

            if (quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Παρακαλώ εισάγετε ποσότητα.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if (Double.parseDouble(quantityText) <= 0){
                JOptionPane.showMessageDialog(frame, "Παρακαλώ εισάγετε θετική ποσότητα.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Προσδιορίζουμε αν η ποσότητα είναι ακέραιος ή δεκαδικός αριθμός
                double quantity = Double.parseDouble(quantityText);  // Είτε ακέραιο είτε δεκαδικό αριθμό

                // Παίρνουμε την τιμή ανά μονάδα του προϊόντος από το αρχείο
                String priceText = fileEditor.getProductDetailByTitle("Files\\Products.txt", name, "Τιμή");

                // Αφαιρούμε το σύμβολο ευρώ και αντικαθιστούμε το κόμμα με τελεία
                priceText = priceText.replace("€", "").replace(",", ".");

                double pricePerUnit = Double.parseDouble(priceText);  // Μετατρέπουμε την τιμή σε double

                // Προσθήκη του προϊόντος στο καλάθι
                shoppingCart.addProduct(name, pricePerUnit, quantity);

                JOptionPane.showMessageDialog(frame, "Το προϊόν προστέθηκε στο καλάθι.", "Επιτυχία", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Η ποσότητα ή η τιμή δεν είναι έγκυρη. Παρακαλώ εισάγετε έναν αριθμό.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}