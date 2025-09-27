package gui.Errors;

import gui.guiLogin;
import gui.guiRegistration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginERROR1 extends JFrame implements ActionListener {

    JFrame frame;
    JPanel panel;
    JLabel message;
    JButton returntoLogin;
    public LoginERROR1() {
        frame = new JFrame();
        frame.setSize(350,150);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setVisible(true);
        frame.add(panel);

        message = new JLabel("Ο χρήστης δεν υπάρχει παρακαλώ δοκιμαστε ξανα.");
        message.setBounds(15,10,300,30);
        panel.add(message);

        returntoLogin = new JButton("OK");
        returntoLogin.setBounds(250,70,75,25);
        returntoLogin.addActionListener(this);
        panel.add(returntoLogin);

        frame.setVisible(true);
        panel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == returntoLogin){
            frame.dispose();
        }
    }
}
