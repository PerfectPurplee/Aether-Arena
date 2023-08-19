package main;

import javax.swing.*;

public class MainFrame extends JFrame {

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    MainPanel mainPanel;

    public MainFrame(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.add(mainPanel);

        mainPanel.setFocusable(true);
        mainPanel.requestFocus();

        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        SCREEN_WIDTH = this.getWidth();
        SCREEN_HEIGHT = this.getHeight();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
