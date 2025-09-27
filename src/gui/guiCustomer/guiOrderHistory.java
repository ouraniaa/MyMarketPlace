package gui.guiCustomer;

import api.Order;
import api.OrderHistory;
import api.ShoppingCart;
import api.fileEditor;
import gui.guiLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.io.FileNotFoundException;


public class guiOrderHistory {
    private JFrame frame;
    private JPanel panel;
    private OrderHistory orderHistory;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    public guiOrderHistory() {
        String username = guiLogin.activeUser;
        orderHistory = new OrderHistory();

        // Δημιουργία του παραθύρου
        frame = new JFrame("Ιστορικό Παραγγελιών");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Χρήση BorderLayout για το JFrame
        frame.setLayout(new BorderLayout());

        // Δημιουργία του κύριου πάνελ
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panel);

        // Προσθήκη του κύριου πάνελ στο κέντρο
        frame.add(scrollPane, BorderLayout.CENTER);

        // Δημιουργία του backButton
        JButton backButton = new JButton("Πίσω");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new java.awt.Color(217, 64, 80, 255));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Κλείνει το παράθυρο
            }
        });

        // Δημιουργία κάτω πάνελ για το κουμπί
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Κεντρική ευθυγράμμιση
        bottomPanel.add(backButton);

        // Προσθήκη του κάτω πάνελ στο κάτω μέρος του παραθύρου
        frame.add(bottomPanel, BorderLayout.SOUTH);

        displayOrderHistory();

        frame.setVisible(true);
    }

    private void displayOrderHistory() {
        List<Order> orders = orderHistory.getOrders();

        if (orders.isEmpty()) {
            JLabel noOrdersLabel = new JLabel("Δεν υπάρχει ιστορικό παραγγελιών.");
            noOrdersLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            noOrdersLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noOrdersLabel);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for (Order order : orders) {
                JPanel orderPanel = new JPanel();
                orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
                orderPanel.setBorder(BorderFactory.createTitledBorder("Παραγγελία στις: " + sdf.format(order.getOrderDate())));

                // Λεπτομέρειες προϊόντων
                for (ShoppingCart.CartItem item : order.getItems()) {
                    try {
                        // Παίρνουμε τη μονάδα μέτρησης του προϊόντος από το αρχείο
                        String unitOfMeasurement = fileEditor.getProductUnit("Files\\Products.txt", item.getProductName());

                        String productDetails;
                        if (unitOfMeasurement.equalsIgnoreCase("kg")) {
                            // Χρήση DecimalFormat για μορφοποίηση ποσοτήτων σε κιλά
                            productDetails = "Προϊόν: " + item.getProductName() +
                                    ", Ποσότητα: " + df.format(item.getQuantity()) +
                                    " kg, Κόστος: " + df.format(item.getTotalCost()) + " €";
                        } else {
                            // Εμφάνιση ποσότητας ως ακέραιος αριθμός για τεμάχια
                            productDetails = "Προϊόν: " + item.getProductName() +
                                    ", Ποσότητα: " + (int) item.getQuantity() +
                                    " τεμάχια, Κόστος: " + df.format(item.getTotalCost()) + " €";
                        }

                        JLabel productLabel = new JLabel(productDetails);
                        orderPanel.add(productLabel);

                    } catch (FileNotFoundException e) {
                        throw new RuntimeException("Το αρχείο προϊόντων δεν βρέθηκε: " + e.getMessage(), e);
                    }
                }

                JLabel totalCostLabel = new JLabel("Συνολικό Κόστος: " + String.format("%.2f €", order.getTotalCost()));
                totalCostLabel.setFont(new Font("Arial", Font.BOLD, 14));
                totalCostLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                orderPanel.add(totalCostLabel);

                panel.add(orderPanel);
                panel.add(Box.createVerticalStrut(10)); // Κενό ανάμεσα στις παραγγελίες
            }
        }

        panel.revalidate();
        panel.repaint();
    }
}
