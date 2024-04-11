package scenes;

import inputs.PlayerKeyboardInputs;
import inputs.PlayerMouseInputs;
import main.MainPanel;

import javax.swing.*;
import java.awt.*;

public class Pause implements SceneEssentials {

    MainPanel mainPanel;
    Dimension pauseWindowSize;
    PlayerMouseInputs playerMouseInputs;
    PlayerKeyboardInputs playerKeyboardInputs;
    Font googleExo2 = Menu.googleExo2;
    private final Dimension jLabelSize;
    final int scaleFactorWidth = 5;
    final int scaleFactorHeight = 3;

    //    Public for dragging window in MouseInputs
    public JPanel pausePanel;

    public JLabel changeYourHeroButton;
    public JLabel settingsButton;
    public JLabel exitToMainMenuButton;

    public Pause(PlayerMouseInputs playerMouseInputs, PlayerKeyboardInputs playerKeyboardInputs, MainPanel mainPanel) {
        this.jLabelSize = new Dimension(300, 40);
        this.mainPanel = mainPanel;
        this.playerMouseInputs = playerMouseInputs;
        this.playerKeyboardInputs = playerKeyboardInputs;
    }


    public void initNewPanel() {
        pausePanel = new JPanel();
        pausePanel.setLayout(new BoxLayout(pausePanel, BoxLayout.PAGE_AXIS));
        pauseWindowSize = new Dimension(MainPanel.gameSize.width / scaleFactorWidth, MainPanel.gameSize.height / scaleFactorHeight);
        pausePanel.setPreferredSize(pauseWindowSize);
        pausePanel.setMaximumSize(pauseWindowSize);
        pausePanel.setMinimumSize(pauseWindowSize);
        pausePanel.addKeyListener(playerKeyboardInputs);
        pausePanel.addMouseListener(playerMouseInputs);
        pausePanel.addMouseMotionListener(playerMouseInputs);
        pausePanel.setFocusable(true);
        pausePanel.requestFocus();

        changeYourHeroButton = createPauseLabel("Change Your Hero");
        settingsButton = createPauseLabel("Settings");
        exitToMainMenuButton = createPauseLabel("Main Menu");

        pausePanel.setOpaque(false);
        pausePanel.add(Box.createRigidArea(new Dimension(0, 35)));
        pausePanel.add(changeYourHeroButton);
        pausePanel.add(Box.createRigidArea(new Dimension(0, 35)));
        pausePanel.add(settingsButton);
        pausePanel.add(Box.createRigidArea(new Dimension(0, 35)));
        pausePanel.add(exitToMainMenuButton);
        pausePanel.add(Box.createRigidArea(new Dimension(0, 35)));
        pausePanel.add(Box.createRigidArea(new Dimension(350,0)));


        pausePanel.setVisible(true);

        JPanel gridBagPanel = new JPanel(new GridBagLayout());
        gridBagPanel.setOpaque(false);
        gridBagPanel.add(pausePanel);

        mainPanel.add(gridBagPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
    }

    private JLabel createPauseLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(googleExo2.deriveFont(26F));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFocusable(false);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setMaximumSize(jLabelSize);
        label.setMinimumSize(jLabelSize);
        label.addMouseListener(playerMouseInputs);
        return label;
    }


    private void initComponents() {

    }

    @Override
    public void draw(Graphics graphics) {

    }
}
