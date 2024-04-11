package scenes;

import main.AssetLoader;
import main.MainPanel;
import scenes.SceneEssentials;
import scenes.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class ChampionSelect implements SceneEssentials {

    public BufferedImage[][] playerSpriteIDLE_RIGHT;
    public BufferedImage[][] playerSpriteIDLE_LEFT;
    public BufferedImage[][] playerSpriteMOVE_LEFT;
    public BufferedImage[][] playerSpriteMOVE_RIGHT;
    public BufferedImage[][] playerSpriteDEATH_RIGHT;
    public BufferedImage[][] playerSpriteDEATH_LEFT;
    public BufferedImage[][] playerSpriteTAKE_DMG_RIGHT;
    public BufferedImage[][] playerSpriteTAKE_DMG_LEFT;
    public BufferedImage[][] playerSpriteROLL_RIGHT;
    public BufferedImage[][] playerSpriteROLL_LEFT;

    JPanel panelForChampionChoice;
    JPanel panelForChampionChoiceBox;
    public JLabel[] championLabels;
    public Map<Integer, Boolean> isChampionBeingMouseHovered;
    public JLabel backToMenu;

    public MainPanel mainPanel;
    private final AssetLoader assetLoader;

    private int animationTickDefault, animationTickRoll;
    private final int defaultAnimationSpeed = 15;
    private final int rollAnimationSpeed = 10;
    public int animationIndexMoving, animationIndexIdle;
    private int[] animationIndexRollForEveryChampion;
    private int[] animationCyclesForRollEveryChampion;


    public ChampionSelect(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        setPlayerSprites();
        championLabels = new JLabel[4];
        isChampionBeingMouseHovered = new HashMap<>();
        animationCyclesForRollEveryChampion = new int[]{0, 0, 0, 0};
        animationIndexRollForEveryChampion = new int[4];
    }

    private void setPlayerSprites() {
        playerSpriteDEATH_RIGHT = assetLoader.playerSpriteDEATH_RIGHT;
        playerSpriteDEATH_LEFT = assetLoader.playerSpriteDEATH_LEFT;
        playerSpriteIDLE_RIGHT = assetLoader.playerSpriteIDLE_RIGHT;
        playerSpriteIDLE_LEFT = assetLoader.playerSpriteIDLE_LEFT;
        playerSpriteROLL_RIGHT = assetLoader.playerSpriteROLL_RIGHT;
        playerSpriteROLL_LEFT = assetLoader.playerSpriteROLL_LEFT;
        playerSpriteMOVE_RIGHT = assetLoader.playerSpriteMOVE_RIGHT;
        playerSpriteMOVE_LEFT = assetLoader.playerSpriteMOVE_LEFT;
        playerSpriteTAKE_DMG_RIGHT = assetLoader.playerSpriteTAKE_DMG_RIGHT;
        playerSpriteTAKE_DMG_LEFT = assetLoader.playerSpriteTAKE_DMG_LEFT;

    }

    public void initComponents() {
        panelForChampionChoice = new JPanel(new GridBagLayout());
        panelForChampionChoiceBox = new JPanel();
        panelForChampionChoiceBox.setLayout(new BoxLayout(panelForChampionChoiceBox, BoxLayout.X_AXIS));
        panelForChampionChoiceBox.setOpaque(false);

        panelForChampionChoiceBox.add(Box.createRigidArea(new Dimension(0, 10)));
        for (int i = 0; i < 4; i++) {
            JLabel label = new JLabel();
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            label.setFocusable(false);
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setMaximumSize(new Dimension(256, 256));
            label.setMinimumSize(new Dimension(256, 256));
            label.setPreferredSize(new Dimension(256, 256));
            label.addMouseListener(mainPanel.getMouseListeners()[0]);
            championLabels[i] = label;
            panelForChampionChoiceBox.add(label);

            if (i < 3) {
                panelForChampionChoiceBox.add(Box.createRigidArea(new Dimension(40, 0)));
            }
        }
        panelForChampionChoiceBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 250, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.PAGE_START;
        panelForChampionChoice.add(panelForChampionChoiceBox, gbc);
        panelForChampionChoice.setOpaque(false);

        JLabel chooseYourHero = new JLabel("CHOOSE YOUR HERO");
        chooseYourHero.setFont(Menu.googleExo2.deriveFont(60F));
        chooseYourHero.setHorizontalAlignment(JLabel.CENTER);
        chooseYourHero.setVerticalAlignment(JLabel.CENTER);
        chooseYourHero.setFocusable(false);
        chooseYourHero.setAlignmentX(Component.CENTER_ALIGNMENT);
        chooseYourHero.setBorder(BorderFactory.createEmptyBorder(200, 0, 0, 0));
        mainPanel.add(chooseYourHero, BorderLayout.NORTH);

        backToMenu = new JLabel("Back to menu");
        backToMenu.setFont(Menu.googleExo2.deriveFont(30F));
        backToMenu.setHorizontalAlignment(JLabel.CENTER);
        backToMenu.setVerticalAlignment(JLabel.CENTER);
        backToMenu.setFocusable(false);
        backToMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        backToMenu.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 30));
        backToMenu.addMouseListener(mainPanel.getMouseListeners()[0]);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backToMenu);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void addComponentsToMainPanel() {
        initComponents();

        mainPanel.add(panelForChampionChoice, BorderLayout.CENTER);
        mainPanel.revalidate();
    }

    private void animationController() {
        animationTickDefault++;
        animationTickRoll++;
        if (animationTickDefault >= defaultAnimationSpeed) {
            if (animationIndexIdle < 5) animationIndexIdle++;
            else animationIndexIdle = 0;
            if (animationIndexMoving < 7) animationIndexMoving++;
            else animationIndexMoving = 0;

            animationTickDefault = 0;
        }
        if (animationTickRoll >= rollAnimationSpeed) {
            isChampionBeingMouseHovered.forEach((key, value) -> {
                if (animationIndexRollForEveryChampion[key] < 3 && value) animationIndexRollForEveryChampion[key]++;
                else {
                    animationCyclesForRollEveryChampion[key]++;
                    animationIndexRollForEveryChampion[key] = 0;
//                    animation finished
                    if (animationCyclesForRollEveryChampion[key] >= 2) {
                        animationCyclesForRollEveryChampion[key] = 0;
                        animationIndexRollForEveryChampion[key] = 0;
                        isChampionBeingMouseHovered.put(key, false);
                    }
                }
            });
            animationTickRoll = 0;
        }
    }


    public void update() {
        animationController();
    }


    @Override
    public void draw(Graphics g) {
        Component[] components = panelForChampionChoiceBox.getComponents();

        for (int i = 1, j = 0; i < components.length; i += 2, j++) {
            Component component = components[i];

            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                Rectangle bounds = label.getBounds();
                // Convert bounds to mainPanel's coordinate system
                bounds = SwingUtilities.convertRectangle(panelForChampionChoiceBox, bounds, panelForChampionChoice);
                bounds = SwingUtilities.convertRectangle(panelForChampionChoice, bounds, mainPanel);

                if (isChampionBeingMouseHovered.containsKey(j) && isChampionBeingMouseHovered.get(j)) {
                    g.drawImage(playerSpriteROLL_RIGHT[j][animationIndexRollForEveryChampion[j]], bounds.x, bounds.y, null);
                } else {
                    g.drawImage(playerSpriteIDLE_RIGHT[j][animationIndexIdle], bounds.x, bounds.y, null);
                }
            }
        }

//   For debugging
//        for (int i = 1; i < components.length; i += 2) {
//            Component component = components[i];
//
//            if (component instanceof JLabel) {
//                JLabel label = (JLabel) component;
//                Rectangle bounds = label.getBounds();
//                bounds = SwingUtilities.convertRectangle(panelForChampionChoiceBox, bounds, panelForChampionChoice);
//                bounds = SwingUtilities.convertRectangle(panelForChampionChoice, bounds, mainPanel);
//
//                g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
//            }
//        }
    }
}


