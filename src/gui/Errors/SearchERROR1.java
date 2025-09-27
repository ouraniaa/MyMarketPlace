package gui.Errors;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchERROR1 implements ActionListener {
    JFrame frame;
    JPanel panel;
    JLabel message;
    JButton ok;


    public SearchERROR1() {
        frame = new JFrame();
        frame.setSize(350, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        message = new JLabel("<html>Δεν υπάρχουν διαθέσιμα<br>προιόντα με βάση την αναζήτησή σας.<html>");
        message.setBounds(15, 10, 300, 50);
        panel.add(message);

        ok = new JButton("OK");
        ok.setBounds(250, 70, 75, 25);
        ok.addActionListener(this);
        panel.add(ok);

        frame.setVisible(true);
        panel.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) {
            frame.dispose();
        }
    }
}
