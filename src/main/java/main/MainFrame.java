package main;

import javax.swing.*;

public class MainFrame extends JFrame {



    MainPanel mainPanel;

    public MainFrame(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.add(mainPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
