package gui;

import api.*;
import gui.Errors.SearchERROR1;
import gui.guiCustomer.guiShoppingCart;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

public class guiSearchProduct extends JFrame implements ActionListener {

    public JFrame frame;
    private JPanel panel;
    private JTextField titleField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> subcategoryBox;
    private JButton searchButton, backButton;
    private JPanel resultPanel;
    private JScrollPane scrollPane;
    private JButton viewButton;
    private boolean isAdmin;
    private ShoppingCart shoppingCart;
    private JButton cartButton;
    private String username;
    public guiSearchProduct(boolean isAdmin, ShoppingCart shoppingCart, String username) {
        this.isAdmin = isAdmin;
        this.shoppingCart = shoppingCart;
        this.username = username;

        // Δημιουργία παραθύρου
        frame = new JFrame("Αναζήτηση Προϊόντων");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        // Εισαγωγή τίτλου
        JLabel titleLabel = new JLabel("Τίτλος:");
        titleLabel.setBounds(50, 20, 80, 25);
        panel.add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(140, 20, 200, 25);
        panel.add(titleField);

        // Επιλογή κατηγορίας
        JLabel categoryLabel = new JLabel("Κατηγορία:");
        categoryLabel.setBounds(50, 60, 80, 25);
        panel.add(categoryLabel);

        categoryBox = new JComboBox<>();
        categoryBox.setBounds(140, 60, 200, 25);
        categoryBox.addItem("Όλες");
        fileEditor.loadCategoriesFromFile("Files/categories_subcategories.txt");
        for (Category category : fileEditor.getCategories()) {
            categoryBox.addItem(category.getName());
        }
        categoryBox.addActionListener(this);
        panel.add(categoryBox);

        // Επιλογή υποκατηγορίας
        JLabel subcategoryLabel = new JLabel("Υποκατηγορία:");
        subcategoryLabel.setBounds(50, 100, 100, 25);
        panel.add(subcategoryLabel);

        subcategoryBox = new JComboBox<>();
        subcategoryBox.setBounds(140, 100, 200, 25);
        subcategoryBox.addItem("Όλες");
        panel.add(subcategoryBox);

        // Περιοχή αποτελεσμάτων
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(resultPanel);
        scrollPane.setBounds(50, 180, 500, 200);
        panel.add(scrollPane);

        // Κουμπί αναζήτησης
        searchButton = new JButton("Αναζήτηση");
        searchButton.setBounds(50, 140, 120, 25);
        searchButton.setBackground(new java.awt.Color(46, 196, 144, 255));
        searchButton.addActionListener(this);
        panel.add(searchButton);

        // Κουμπί επιστροφής
        backButton = new JButton("Πίσω");
        backButton.setBounds(200, 140, 120, 25);
        backButton.setBackground(new java.awt.Color(217, 64, 80, 255));
        backButton.addActionListener(this);
        panel.add(backButton);

        if(!isAdmin) {
            // Κουμπί Καλαθιού
            cartButton = new JButton("Καλάθι");
            cartButton.setBounds(460, 10, 100, 25);  // Τοποθέτηση στην πάνω δεξιά γωνία
            cartButton.addActionListener(this);
            panel.add(cartButton);
        }

        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String title = titleField.getText().trim();
            String category = categoryBox.getSelectedItem().toString();
            String subcategory = subcategoryBox.getSelectedItem().toString();
            ArrayList<String> results = searchProducts(title, category, subcategory, "Files\\Products.txt");

            resultPanel.removeAll();
            if (results.isEmpty()) {
                JLabel noResultsLabel = new JLabel("Δεν βρέθηκαν προϊόντα.");
                new SearchERROR1();
                resultPanel.add(noResultsLabel);
            } else {
                for (String productTitle : results) {
                    JPanel productPanel = new JPanel();
                    productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.X_AXIS));

                    JLabel titleLabel = new JLabel(productTitle);
                    JButton viewButton = new JButton("View");

                    viewButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                String ProductTitle = productTitle.replace("• ", "");
                                new guiViewProduct(isAdmin, ProductTitle, shoppingCart);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    productPanel.add(titleLabel);
                    productPanel.add(Box.createHorizontalStrut(10));
                    productPanel.add(viewButton);

                    resultPanel.add(productPanel);
                }
            }
            resultPanel.revalidate();
            resultPanel.repaint();
        }

        if (e.getSource() == backButton) {
            frame.dispose();
        }

        if (e.getSource() == categoryBox) {
            String selectedCategory = categoryBox.getSelectedItem().toString();
            updateSubcategories(selectedCategory);
        }

        if (e.getSource() == cartButton){
            new guiShoppingCart(shoppingCart);
        }
    }

    // Μέθοδος για αναζήτηση προϊόντων με βάση τα κριτήρια
    public static ArrayList<String> searchProducts(String title, String category, String subcategory, String productFilePath) {
        fileEditor.loadProductsFromFile(productFilePath); // Φόρτωση προϊόντων από το αρχείο

        ArrayList<String> result = new ArrayList<>();
        HashSet<String> uniqueResults = new HashSet<>(); // Χρήση για έλεγχο μοναδικότητας

        for (Category cat : fileEditor.getCategories()) {
            if (!category.equals("Όλες") && !cat.getName().equalsIgnoreCase(category)) {
                continue;
            }

            for (Subcategory subcat : cat.getSubcategories()) {
                if (!subcategory.equals("Όλες") && !subcat.getName().equalsIgnoreCase(subcategory)) {
                    continue;
                }

                for (Product product : subcat.getProducts()) {
                    if (title.isEmpty() || product.getTitle().toLowerCase().contains(title.toLowerCase())) {
                        String productTitle = "• " + product.getTitle();
                        if (uniqueResults.add(productTitle)) {
                            result.add(productTitle);
                        }
                    }
                }
            }
        }

        return result;
    }

    private void updateSubcategories(String selectedCategory) {
        subcategoryBox.removeAllItems();
        subcategoryBox.addItem("Όλες");
        if (!selectedCategory.equals("Όλες")) {
            for (Category category : fileEditor.getCategories()) {
                if (category.getName().equals(selectedCategory)) {
                    for (Subcategory subcategory : category.getSubcategories()) {
                        subcategoryBox.addItem(subcategory.getName());
                    }
                }
            }
        }
    }
}
