package main;

import javax.swing.*;

public class MainFrame extends JFrame {

//Why you do this to meeee!

    MainPanel mainPanel;

    public MainFrame(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.add(mainPanel);

        mainPanel.setFocusable(true);
        mainPanel.requestFocus();

        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
