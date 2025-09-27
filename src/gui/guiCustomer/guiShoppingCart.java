package gui.guiCustomer;

import api.OrderHistory;
import api.ShoppingCart;
import api.fileEditor;
import gui.guiLogin;
import gui.Errors.CartERROR1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * Αυτή η κλάση αντιπροσωπεύει το γραφικό περιβάλλον για το καλάθι αγορών του πελάτη.
 * Χειρίζεται την εμφάνιση των προϊόντων, την επεξεργασία της ποσότητας, την ολοκλήρωση παραγγελίας και την επιστροφή στο προηγούμενο μενού.
 */

public class guiShoppingCart implements ActionListener {
    private JFrame frame;
    private JPanel panel;
    private JPanel cartDetailsPanel;
    private JLabel totalCostLabel;
    private JButton completeOrderButton;
    private JButton backButton;
    private ShoppingCart shoppingCart;
    private String username;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public guiShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        String username = guiLogin.activeUser;

        frame = new JFrame();
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        cartDetailsPanel = new JPanel();
        cartDetailsPanel.setLayout(new BoxLayout(cartDetailsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(cartDetailsPanel);
        scrollPane.setBounds(30, 30, 540, 500);
        panel.add(scrollPane);

        totalCostLabel = new JLabel("Συνολικό Κόστος: 0.0 €");
        totalCostLabel.setBounds(30, 540, 200, 25);
        totalCostLabel.setForeground(new java.awt.Color(0, 128, 0));
        panel.add(totalCostLabel);

        completeOrderButton = new JButton("Ολοκλήρωση Παραγγελίας");
        completeOrderButton.setBounds(30, 580, 200, 30);
        completeOrderButton.setBackground(new java.awt.Color(0, 123, 255)); // Μπλε χρώμα
        completeOrderButton.setForeground(java.awt.Color.WHITE);
        completeOrderButton.addActionListener(this);
        panel.add(completeOrderButton);

        backButton = new JButton("Πίσω");
        backButton.setBounds(340, 580, 200, 30);
        backButton.setBackground(new java.awt.Color(217, 64, 80, 255));
        backButton.setForeground(java.awt.Color.WHITE);
        backButton.addActionListener(this);
        panel.add(backButton);

        updateCartDetails();

        frame.setVisible(true);
    }

    /**
     * Ενημερώνει τις λεπτομέρειες του καλαθιού, όπως τα προϊόντα, τις ποσότητες και το συνολικό κόστος.
     */
    private void updateCartDetails() {
        cartDetailsPanel.removeAll();

        for (ShoppingCart.CartItem item : shoppingCart.getCartDetails()) {
            JPanel productPanel = new JPanel();
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.X_AXIS));

            try {
                // Παίρνουμε τη μονάδα μέτρησης του προϊόντος από το αρχείο
                String unitOfMeasurement = fileEditor.getProductUnit("Files\\Products.txt", item.getProductName());

                String productDetails;
                if (unitOfMeasurement.equalsIgnoreCase("kg")) {
                    // Χρήση του DecimalFormat για μορφοποίηση της ποσότητας και του κόστους
                    productDetails = "Προϊόν: " + item.getProductName() +
                            ", Ποσότητα: " + df.format(item.getQuantity()) +
                            " kg, Κόστος: " + df.format(item.getTotalCost()) + " €";
                } else {
                    // Για τεμάχια, η ποσότητα πρέπει να είναι ακέραιος αριθμός
                    productDetails = "Προϊόν: " + item.getProductName() +
                            ", Ποσότητα: " + (int) item.getQuantity() +
                            " τεμάχια, Κόστος: " + df.format(item.getTotalCost()) + " €";
                }

                JLabel productLabel = new JLabel(productDetails);
                productPanel.add(productLabel);

            } catch (FileNotFoundException e) {
                throw new RuntimeException("Το αρχείο προϊόντων δεν βρέθηκε: " + e.getMessage(), e);
            }

            JButton removeButton = new JButton("Αφαίρεση");
            JButton updateQuantityButton = new JButton("Αλλαγή Ποσότητας");

            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    shoppingCart.removeProduct(item.getProductName());
                    updateCartDetails();
                }
            });

            updateQuantityButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String newQuantityStr = JOptionPane.showInputDialog(frame,
                            "Εισάγετε τη νέα ποσότητα για το προϊόν: " + item.getProductName(),
                            "Αλλαγή Ποσότητας",
                            JOptionPane.PLAIN_MESSAGE);

                    if (newQuantityStr != null && !newQuantityStr.isEmpty()) {
                        try {
                            // Αντικατάσταση κόμματος με τελεία για δεκαδικούς αριθμούς
                            newQuantityStr = newQuantityStr.replace(",", ".");

                            // Έλεγχος αν είναι αριθμός
                            if (!newQuantityStr.matches("^-?\\d*\\.?\\d+$")) {
                                throw new NumberFormatException("Μη έγκυρη μορφή αριθμού");
                            }

                            double newQuantity = Double.parseDouble(newQuantityStr);

                            // Έλεγχος για αρνητική ποσότητα
                            if (newQuantity < 0) {
                                throw new IllegalArgumentException("Η ποσότητα δεν μπορεί να είναι αρνητική.");
                            }

                            // Παίρνουμε το διαθέσιμο απόθεμα από το αρχείο
                            String stockStr = fileEditor.getProductDetailByTitle("Files\\Products.txt", item.getProductName(), "Ποσότητα");

                            // Αφαίρεση μονάδας μέτρησης από το stockStr, αν υπάρχει
                            stockStr = stockStr.replaceAll("[^0-9.,-]", "").replace(",", ".");
                            double availableStock = Double.parseDouble(stockStr);

                            if (newQuantity == 0) {
                                // Αφαίρεση προϊόντος από το καλάθι
                                shoppingCart.removeProduct(item.getProductName());
                                JOptionPane.showMessageDialog(frame, "Το προϊόν αφαιρέθηκε από το καλάθι.", "Πληροφορία", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                // Ενημέρωση ποσότητας στο καλάθι
                                shoppingCart.updateProductQuantity(item.getProductName(), newQuantity, availableStock);
                            }

                            // Ενημέρωση εμφάνισης καλαθιού
                            updateCartDetails();
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Η ποσότητα πρέπει να είναι αριθμός.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                        } catch (IllegalArgumentException ex) {
                            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            productPanel.add(Box.createHorizontalStrut(10)); // Διάστημα μεταξύ στοιχείων
            productPanel.add(removeButton);
            productPanel.add(Box.createHorizontalStrut(10));
            productPanel.add(updateQuantityButton);

            cartDetailsPanel.add(productPanel);
        }

        totalCostLabel.setText("Συνολικό Κόστος: " + df.format(shoppingCart.getTotalCost()) + " €");
        cartDetailsPanel.revalidate();
        cartDetailsPanel.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == completeOrderButton) {
            if (shoppingCart.getCartDetails().isEmpty()) {
                new CartERROR1(); // Εμφάνιση μηνύματος σφάλματος εάν το καλάθι είναι άδειο
            } else {

                // Αποθήκευση παραγγελίας στο ιστορικό
                OrderHistory orderHistory = new OrderHistory(); // Χρήστης που συνδέεται
                orderHistory.addOrder(shoppingCart.getCartDetails(), shoppingCart.getTotalCost());

                JOptionPane.showMessageDialog(frame, "Η παραγγελία ολοκληρώθηκε επιτυχώς!");
                shoppingCart.completeOrder();
                updateCartDetails(); // Ενημέρωση καλαθιού
            }
        } else if (e.getSource() == backButton) {
            frame.dispose();
        }
    }

}
