package scenes;

import inputs.PlayerMouseInputs;
import main.MainPanel;
import scenes.SceneEssentials;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class Menu implements SceneEssentials {

    private final MainPanel mainPanel;
    private final MouseListener mouseListener;

    public static JLabel startGameWithServer;
    public static JLabel joinExistingGame;
    public static JLabel settings;
    public static JLabel exit;

    private JPanel panelForMenuButtons;
    private JPanel mainMenuPanel;

    public static Font googleExo2;
    private Dimension jLabelSize;


    public Menu(MainPanel mainPanel, PlayerMouseInputs playerMouseInputs) {
        this.mainPanel = mainPanel;
        this.mouseListener = playerMouseInputs;
        this.jLabelSize = new Dimension(500, 75);

        try {
            InputStream fontStream = getClass().getResourceAsStream("/Exo2-BoldItalic.ttf");
            googleExo2 = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(fontStream));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        initiateMenuComponents();
    }

    public void initiateMenuComponents() {
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridBagLayout());
        panelForMenuButtons = new JPanel();
        panelForMenuButtons.setLayout(new BoxLayout(panelForMenuButtons, BoxLayout.Y_AXIS));
        panelForMenuButtons.setOpaque(false);
        panelForMenuButtons.setPreferredSize(new Dimension(800, 600));

        // Button properties
        startGameWithServer = createMenuLabel("HOST NEW GAME");
        joinExistingGame = createMenuLabel("JOIN EXISTING GAME");
        settings = createMenuLabel("SETTINGS");
        exit = createMenuLabel("EXIT");

        // Add buttons to the panel

        panelForMenuButtons.add(Box.createRigidArea(new Dimension(0, 50)));
        panelForMenuButtons.add(startGameWithServer);
        panelForMenuButtons.add(Box.createRigidArea(new Dimension(0, 50)));
        panelForMenuButtons.add(joinExistingGame);
        panelForMenuButtons.add(Box.createRigidArea(new Dimension(0, 50)));
        panelForMenuButtons.add(settings);
        panelForMenuButtons.add(Box.createRigidArea(new Dimension(0, 50)));
        panelForMenuButtons.add(exit);


        mainMenuPanel.add(panelForMenuButtons);
        mainMenuPanel.setOpaque(false);

        addComponentsToMainPanel();
    }

    private JLabel createMenuLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(googleExo2.deriveFont(26F));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFocusable(false);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(jLabelSize);
        label.setMinimumSize(jLabelSize);
        label.addMouseListener(mouseListener);
        return label;
    }


    public void addComponentsToMainPanel() {
        mainPanel.add(mainMenuPanel, BorderLayout.CENTER);
    }


    public void draw(Graphics g) {

    }
}
