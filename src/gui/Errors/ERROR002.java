package gui.Errors;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ERROR002 extends JFrame implements ActionListener {

    JFrame frame;
    JPanel panel;
    JLabel message;
    JButton returntoRegistration;

    public ERROR002() {
        frame = new JFrame();
        frame.setSize(350, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        // Ενημέρωση μηνύματος για τα κενά πεδία
        message = new JLabel("Παρακαλώ συμπληρώστε όλα τα πεδία");
        message.setBounds(15, 10, 300, 30);
        panel.add(message);

        returntoRegistration = new JButton("OK");
        returntoRegistration.setBounds(250, 70, 75, 25);
        returntoRegistration.addActionListener(this);
        panel.add(returntoRegistration);

        frame.setVisible(true);
        panel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == returntoRegistration) {
            frame.dispose();
        }
    }
}
