package scenes.menu;

import main.Main;
import main.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Menu {

    MainPanel mainPanel;

    public Menu(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        initiateMenuComponents();
    }

    public void initiateMenuComponents() {
        Dimension buttonMaxSize = new Dimension(600, 75);
        JPanel panelForMenuButtons = new JPanel();
        panelForMenuButtons.setLayout(new GridBagLayout());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(800, 600));

//        button properties
        JButton startGame = new JButton("JOIN EXISTING GAME");
        JButton settings = new JButton("START NEW GAME AND BECOME A HOST");
        JButton scoreBoard = new JButton("");
        JButton zasadyGry = new JButton("");
        settings.setBackground(Color.YELLOW);
        startGame.setBackground(Color.YELLOW);
        scoreBoard.setBackground(Color.YELLOW);
        zasadyGry.setBackground(Color.YELLOW);
        settings.setFocusable(false);
        startGame.setFocusable(false);
        scoreBoard.setFocusable(false);
        zasadyGry.setFocusable(false);
        settings.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreBoard.setAlignmentX(Component.CENTER_ALIGNMENT);
        zasadyGry.setAlignmentX(Component.CENTER_ALIGNMENT);
        settings.setMaximumSize(buttonMaxSize);
        startGame.setMaximumSize(buttonMaxSize);
        scoreBoard.setMaximumSize(buttonMaxSize);
        zasadyGry.setMaximumSize(buttonMaxSize);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        buttonPanel.add(startGame);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        buttonPanel.add(settings);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        buttonPanel.add(scoreBoard);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        buttonPanel.add(zasadyGry);

        panelForMenuButtons.add(buttonPanel);
        panelForMenuButtons.setOpaque(false);

        mainPanel.add(panelForMenuButtons, BorderLayout.CENTER);
    }


    public void draw(Graphics g) {

    }
}
