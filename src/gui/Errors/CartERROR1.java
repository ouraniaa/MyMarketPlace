package gui.Errors;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartERROR1 extends JFrame implements ActionListener {
    JFrame frame;
    JPanel panel;
    JLabel message;
    JButton okButton;

    public CartERROR1() {
        // Create the frame and set its properties
        frame = new JFrame();
        frame.setSize(450, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Create the panel and set layout
        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        // Create and set the message label
        message = new JLabel("Το καλάθι είναι άδειο. Δεν μπορείτε να ολοκληρώσετε την παραγγελία.");
        message.setBounds(15, 10, 400, 35);
        panel.add(message);

        // Create and set the OK button
        okButton = new JButton("OK");
        okButton.setBounds(180, 70, 75, 25);
        okButton.addActionListener(this);
        panel.add(okButton);

        // Make the frame visible
        frame.setVisible(true);
        panel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Close the frame when the OK button is pressed
        if (e.getSource() == okButton) {
            frame.dispose();
        }
    }
}
