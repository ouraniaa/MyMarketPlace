package gui.guiManager;

import api.fileEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class editProduct extends JFrame implements ActionListener {

    private JTextField titleText;
    private JTextField descriptionText;
    private JTextField categoryText;
    private JTextField subcategoryText;
    private JTextField priceText;
    private JTextField quantityText;
    private JButton saveButton;
    private JButton backButton;
    private String productTitle;

    public editProduct(String productTitle) {
        // Αποθήκευση του τίτλου του προϊόντος που θα επεξεργαστούμε
        this.productTitle = productTitle;

        // Βασικές ρυθμίσεις του JFrame
        setTitle("Επεξεργασία Προϊόντος");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Δημιουργία και τοποθέτηση των στοιχείων
        JLabel titleLabel = new JLabel("Τίτλος:");
        titleLabel.setBounds(30, 30, 100, 25);
        add(titleLabel);

        titleText = new JTextField(productTitle);
        titleText.setBounds(150, 30, 200, 25);
        titleText.setEditable(true);  // Ο τίτλος δεν μπορεί να τροποποιηθεί
        add(titleText);

        JLabel descriptionLabel = new JLabel("Περιγραφή:");
        descriptionLabel.setBounds(30, 70, 100, 25);
        add(descriptionLabel);

        descriptionText = new JTextField(fileEditor.getProductDetailByTitle("Files\\Products.txt", productTitle, "Περιγραφή"));
        descriptionText.setBounds(150, 70, 200, 25);
        add(descriptionText);

        JLabel categoryLabel = new JLabel("Κατηγορία:");
        categoryLabel.setBounds(30, 110, 100, 25);
        add(categoryLabel);

        categoryText = new JTextField(fileEditor.getProductDetailByTitle("Files\\Products.txt", productTitle, "Κατηγορία"));
        categoryText.setBounds(150, 110, 200, 25);
        add(categoryText);

        JLabel subcategoryLabel = new JLabel("Υποκατηγορία:");
        subcategoryLabel.setBounds(30, 150, 100, 25);
        add(subcategoryLabel);

        subcategoryText = new JTextField(fileEditor.getProductDetailByTitle("Files\\Products.txt", productTitle, "Υποκατηγορία"));
        subcategoryText.setBounds(150, 150, 200, 25);
        add(subcategoryText);

        JLabel priceLabel = new JLabel("Τιμή:");
        priceLabel.setBounds(30, 190, 100, 25);
        add(priceLabel);

        priceText = new JTextField(fileEditor.getProductDetailByTitle("Files\\Products.txt", productTitle, "Τιμή"));
        priceText.setBounds(150, 190, 200, 25);
        add(priceText);

        JLabel quantityLabel = new JLabel("Ποσότητα:");
        quantityLabel.setBounds(30, 230, 100, 25);
        add(quantityLabel);

        String quantityWithUnit = fileEditor.getProductDetailByTitle("Files\\Products.txt", productTitle, "Ποσότητα");
        String quantityOnly = quantityWithUnit.replaceAll("[^\\d.]", "");
        quantityText = new JTextField(quantityOnly);
        quantityText.setBounds(150, 230, 200, 25);
        add(quantityText);

        JLabel unitTypeLabel = new JLabel("Μονάδα:");
        unitTypeLabel.setBounds(30, 270, 100, 25);
        add(unitTypeLabel);

        try {
            String currentUnit = fileEditor.getProductUnit("Files\\Products.txt", productTitle);
            JLabel unitTypeLabel1 = new JLabel(currentUnit);
            unitTypeLabel1.setBounds(150, 270, 100, 25);
            add(unitTypeLabel1);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: Το αρχείο δεν βρέθηκε!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
        }

        // Κουμπί "Αποθήκευση Αλλαγών"
        saveButton = new JButton("Αποθήκευση Αλλαγών");
        saveButton.setBounds(50, 320, 150, 30);
        saveButton.setBackground(new java.awt.Color(93, 176, 93, 255));
        saveButton.addActionListener(this);
        add(saveButton);

        // Κουμπί "Πίσω"
        backButton = new JButton("Πίσω");
        backButton.setBounds(220, 320, 100, 30);
        backButton.setBackground(new java.awt.Color(217, 64, 80, 255));
        backButton.addActionListener(this);
        add(backButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            String newTitle = titleText.getText();
            Map<String, String> updatedFields = new HashMap<>();

            if (!descriptionText.getText().isEmpty()) {
                updatedFields.put("Περιγραφή", descriptionText.getText());
            }
            if (!categoryText.getText().isEmpty()) {
                updatedFields.put("Κατηγορία", categoryText.getText());
            }
            if (!subcategoryText.getText().isEmpty()) {
                updatedFields.put("Υποκατηγορία", subcategoryText.getText());
            }
            if (!priceText.getText().isEmpty()) {
                updatedFields.put("Τιμή", priceText.getText());
            }
            if (!quantityText.getText().isEmpty()) {
                String quantity = quantityText.getText();
                JLabel unitTypeLabel1 = (JLabel) getContentPane().getComponent(13); // Παίρνουμε το unitTypeLabel1 από τα components
                String unit = unitTypeLabel1.getText(); // Παίρνουμε τη μονάδα μέτρησης
                if (unit.equalsIgnoreCase("τεμάχια"))
                    updatedFields.put("Ποσότητα", quantity + " " + unit); // Συνδυάζουμε ποσότητα και μονάδα
                else
                    updatedFields.put("Ποσότητα", quantity + unit);
            }

            String filePath = "Files\\Products.txt";
            try {
                if (!newTitle.equals(productTitle)) {
                    fileEditor.updateProductTitle(filePath, productTitle, newTitle);
                    productTitle = newTitle; // Ενημερώνουμε την τοπική μεταβλητή
                }
                fileEditor.updateProductInFile(filePath, newTitle, updatedFields);
                JOptionPane.showMessageDialog(this, "Οι αλλαγές αποθηκεύτηκαν επιτυχώς!");
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(this, "Το προϊόν δεν βρέθηκε!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την αποθήκευση των αλλαγών!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == backButton) {
            dispose(); // Κλείσιμο παραθύρου
        }
    }

}
