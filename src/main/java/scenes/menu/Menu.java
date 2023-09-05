package scenes.menu;

import inputs.ActionListener;
import main.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Menu {

    MainPanel mainPanel;
    ActionListener actionListener;
    public static JButton startGameWithServer;
    public static JButton joinExistingGame;

    public Menu(MainPanel mainPanel, ActionListener actionListener) {
        this.mainPanel = mainPanel;
        this.actionListener = actionListener;
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
        startGameWithServer = new JButton("START NEW GAME AND BECOME A HOST");
        joinExistingGame = new JButton("JOIN EXISTING GAME");
        JButton scoreBoard = new JButton("");
        JButton zasadyGry = new JButton("");
        joinExistingGame.setBackground(Color.YELLOW);
        startGameWithServer.setBackground(Color.YELLOW);
        scoreBoard.setBackground(Color.YELLOW);
        zasadyGry.setBackground(Color.YELLOW);
        joinExistingGame.setFocusable(false);
        startGameWithServer.setFocusable(false);
        scoreBoard.setFocusable(false);
        zasadyGry.setFocusable(false);
        joinExistingGame.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGameWithServer.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreBoard.setAlignmentX(Component.CENTER_ALIGNMENT);
        zasadyGry.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinExistingGame.setMaximumSize(buttonMaxSize);
        startGameWithServer.setMaximumSize(buttonMaxSize);
        scoreBoard.setMaximumSize(buttonMaxSize);
        zasadyGry.setMaximumSize(buttonMaxSize);
        startGameWithServer.addActionListener(actionListener);
        joinExistingGame.addActionListener(actionListener);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        buttonPanel.add(startGameWithServer);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        buttonPanel.add(joinExistingGame);
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
