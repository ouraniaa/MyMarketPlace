package gui.guiManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class viewStatistics extends JFrame implements ActionListener {

    public JFrame frame;
    private JButton unavailableProductsButton;
    private JButton popularProductsButton;
    private JButton backButton;
    private JTextArea textArea;
    private static final String PRODUCTS_FILE_PATH = "Files\\Products.txt"; // Replace with the actual file path
    private static final String COMPLETED_ORDERS_PATH = "Files\\CompletedOrders\\";

    public viewStatistics() {
        frame = new JFrame("Στατιστικά Προϊόντων");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        unavailableProductsButton = new JButton("Μη Διαθέσιμα Προϊόντα");
        unavailableProductsButton.setBackground(new java.awt.Color(233, 236, 117, 255));
        unavailableProductsButton.addActionListener(this);

        popularProductsButton = new JButton("Δημοφιλέστερα Προϊόντα");
        popularProductsButton.setBackground(new java.awt.Color(233, 236, 117, 255));
        popularProductsButton.addActionListener(this);

        textArea = new JTextArea();
        textArea.setEditable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(unavailableProductsButton);
        buttonPanel.add(popularProductsButton);

        backButton = new JButton("Πίσω");
        backButton.addActionListener(this);
        backButton.setBackground(new java.awt.Color(227, 108, 122, 255));
        buttonPanel.add(backButton);

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == unavailableProductsButton) {
            // Εύρεση μη διαθέσιμων προϊόντων
            ArrayList<String> unavailableProducts = getUnavailableProducts(PRODUCTS_FILE_PATH);
            displayUnavailableProducts(unavailableProducts);
        } else if (e.getSource() == popularProductsButton) {
            // Εύρεση δημοφιλέστερων προϊόντων
            Map<String, Integer> popularProducts = getPopularProducts(COMPLETED_ORDERS_PATH);
            displayPopularProducts(popularProducts);
        }  else if (e.getSource() == backButton) {
            frame.dispose(); // Κλείσιμο παραθύρου
        }
    }

    // Εύρεση μη διαθέσιμων προϊόντων από το αρχείο
    private ArrayList<String> getUnavailableProducts(String filePath) {
        ArrayList<String> unavailableProducts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String title = null;
            double stock = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Τίτλος:")) {
                    title = line.substring(7).trim();
                } else if (line.startsWith("Ποσότητα:")) {
                    String quantity = line.substring(9).trim();
                    if (quantity.endsWith("kg")) {
                        stock = Double.parseDouble(quantity.replace("kg", "").trim());
                    } else if (quantity.endsWith("τεμάχια")) {
                        stock = Double.parseDouble(quantity.replace("τεμάχια", "").trim());
                    }

                    // Αν η ποσότητα είναι 0, προσθήκη στη λίστα μη διαθέσιμων προϊόντων
                    if (stock == 0 && title != null) {
                        unavailableProducts.add(title);
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα κατά την ανάγνωση του αρχείου: " + ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }
        return unavailableProducts;
    }

    // Εύρεση δημοφιλέστερων προϊόντων από ολοκληρωμένες παραγγελίες
    private Map<String, Integer> getPopularProducts(String directoryPath) {
        Map<String, Integer> productFrequency = new HashMap<>();

        File directory = new File(directoryPath);
        // Έλεγχος αν ο φάκελος υπάρχει και περιέχει αρχεία
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith("Orders.txt"));

            if (files != null) {
                for (File file : files) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            // Επεξεργασία γραμμής για λήψη προϊόντων
                            String[] parts = line.split("\\|");
                            if (parts.length >= 2) {
                                String productsPart = parts[1].trim();
                                String[] products = productsPart.split(";");
                                for (String productEntry : products) {
                                    String[] productDetails = productEntry.split(",");
                                    if (productDetails.length > 0) {
                                        String productName = productDetails[0].trim();
                                        if (!productName.isEmpty()) { // Προσθήκη προϊόντος στο χάρτη με συχνότητα
                                            productFrequency.put(productName, productFrequency.getOrDefault(productName, 0) + 1);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Σφάλμα κατά την ανάγνωση του αρχείου: " + file.getName() + " - " + ex.getMessage(), "Σφάλμα", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ο φάκελος " + directoryPath + " δεν υπάρχει.", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }

        return productFrequency;
    }

    // Εμφάνιση μη διαθέσιμων προϊόντων στην περιοχή κειμένου
    private void displayUnavailableProducts(ArrayList<String> unavailableProducts) {
        if (unavailableProducts.isEmpty()) {
            textArea.setText("Όλα τα προϊόντα είναι διαθέσιμα.");
        } else {
            StringBuilder builder = new StringBuilder("Μη Διαθέσιμα Προϊόντα:\n\n");
            for (String product : unavailableProducts) {
                builder.append(product).append("\n");
            }
            textArea.setText(builder.toString());
        }
    }

    // Εμφάνιση δημοφιλέστερων προϊόντων στην περιοχή κειμένου
    private void displayPopularProducts(Map<String, Integer> popularProducts) {
        if (popularProducts.isEmpty()) {
            textArea.setText("Δεν υπάρχουν δεδομένα για δημοφιλή προϊόντα.");
        } else {
            StringBuilder builder = new StringBuilder("Δημοφιλέστερα Προϊόντα:\n\n");
            popularProducts.entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Ταξινόμηση κατά συχνότητα
                    .forEach(entry -> builder.append(entry.getKey()).append(" - Εμφανίσεις: ").append(entry.getValue()).append("\n"));

            textArea.setText(builder.toString());
        }
    }
}
