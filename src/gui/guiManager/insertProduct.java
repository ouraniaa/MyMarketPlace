package gui.guiManager;

import api.fileEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class insertProduct extends JFrame implements ActionListener{
    public JFrame frame;
    JPanel panel;
    JTextField titleText;
    JTextField descriptionText;
    JTextField category;
    JTextField subcategory;
    JTextField price;
    JTextField unitType;
    JTextField stock;
    JButton addButton, backButton;

    public insertProduct(){
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);

        JLabel title = new JLabel("Προσθήκη Προϊόντος");
        title.setBounds(180, 20, 200, 25);
        panel.add(title);

        JLabel title1 = new JLabel("Τίτλος:");
        title1.setBounds(20, 70, 100, 25);
        panel.add(title1);

        titleText = new JTextField();
        titleText.setBounds(150, 70, 300, 25);
        panel.add(titleText);

        JLabel description = new JLabel("Περιγραφή:");
        description.setBounds(20, 110, 100, 25);
        panel.add(description);

        descriptionText = new JTextField();
        descriptionText.setBounds(150, 110, 300, 25);
        panel.add(descriptionText);

        JLabel categoryLabel = new JLabel("Κατηγορία:");
        categoryLabel.setBounds(20, 150, 100, 25);
        panel.add(categoryLabel);

        category = new JTextField();
        category.setBounds(150, 150, 300, 25);
        panel.add(category);

        JLabel subcategoryLabel = new JLabel("Υποκατηγορία:");
        subcategoryLabel.setBounds(20, 190, 100, 25);
        panel.add(subcategoryLabel);

        subcategory = new JTextField();
        subcategory.setBounds(150, 190, 300, 25);
        panel.add(subcategory);

        JLabel priceLabel = new JLabel("Τιμή:");
        priceLabel.setBounds(20, 230, 100, 25);
        panel.add(priceLabel);

        price = new JTextField();
        price.setBounds(150, 230, 300, 25);
        panel.add(price);

        JLabel unitTypeLabel = new JLabel("Τύπος Μονάδας:");
        unitTypeLabel.setBounds(20, 270, 120, 25);
        panel.add(unitTypeLabel);

        unitType = new JTextField();
        unitType.setBounds(150, 270, 300, 25);
        panel.add(unitType);

        JLabel stockLabel = new JLabel("Απόθεμα:");
        stockLabel.setBounds(20, 310, 100, 25);
        panel.add(stockLabel);

        stock = new JTextField();
        stock.setBounds(150, 310, 300, 25);
        panel.add(stock);

        addButton = new JButton("Προσθήκη");
        addButton.setBounds(150, 370, 100, 30);
        addButton.setBackground(new java.awt.Color(87, 224, 87, 255));
        panel.add(addButton);
        addButton.addActionListener(this);

        backButton = new JButton("Πίσω");
        backButton.setBounds(270, 370, 100, 30);
        backButton.setBackground(new java.awt.Color(210, 75, 90, 255));
        panel.add(backButton);
        backButton.addActionListener(this);

        frame.setVisible(true);
        panel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == addButton) {
            String title = titleText.getText();
            String description = descriptionText.getText();
            String categoryValue = category.getText();
            String subcategoryValue = subcategory.getText();
            String priceValue = price.getText();
            String unitTypeValue = unitType.getText();
            String stockValue = stock.getText();

            if (title.isEmpty() || description.isEmpty() || categoryValue.isEmpty() ||
                    subcategoryValue.isEmpty() || priceValue.isEmpty() || unitTypeValue.isEmpty() ||
                    stockValue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Παρακαλώ συμπληρώστε όλα τα πεδία.");
                return;
            }
            try {
                String path = "Files\\Products.txt";
                fileEditor.writeProductToFile(path, title, description, categoryValue, subcategoryValue, priceValue, stockValue, unitTypeValue);
                JOptionPane.showMessageDialog(this, "Το προϊόν προστέθηκε επιτυχώς!");
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την προσθήκη του προϊόντος: " + ex.getMessage());
            }
        }

        if (e.getSource() == backButton) {
            frame.dispose();
        }
    }

    // Μέθοδος καθαρισμού όλων των πεδίων κειμένου
    private void clearFields() {
        titleText.setText("");
        descriptionText.setText("");
        category.setText("");
        subcategory.setText("");
        price.setText("");
        unitType.setText("");
        stock.setText("");
    }
}
