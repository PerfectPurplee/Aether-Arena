package main;

import entities.playercharacters.PlayerClass;
import inputs.PlayerKeyboardInputs;
import inputs.PlayerMouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static main.GameEngine.*;

public class MainPanel extends JPanel {

    public static Dimension worldSize;
    PlayerKeyboardInputs playerKeyboardInputs;
    PlayerMouseInputs playerMouseInputs;
    public static BufferedImage worldMap;
    public static int worldScale;


    public MainPanel(PlayerKeyboardInputs playerKeyboardInputs, PlayerMouseInputs playerMouseInputs) {
        getWholeMapImage();
        this.playerKeyboardInputs = playerKeyboardInputs;
        this.playerMouseInputs = playerMouseInputs;
        this.addKeyListener(playerKeyboardInputs);
        this.addMouseListener(playerMouseInputs);
        this.addMouseMotionListener(playerMouseInputs);
        setWorldScaleAndPanelSize();
        this.setFocusable(true);
        this.requestFocus();
//        this.setBackground(Color.white);

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }


    public void getWholeMapImage() {
        InputStream inputStream = getClass().getResourceAsStream("/Map.png");
        try {
            worldMap = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setWorldScaleAndPanelSize() {
        worldScale = 4;
        worldSize = new Dimension(worldMap.getWidth() * worldScale,
                worldMap.getHeight() * worldScale);
        this.setPreferredSize(worldSize);

        PlayerClass.playerPosX = worldSize.width;


    }

//    g.drawImage(worldMap,  // The source image to draw
//            0, 0,      // Destination x and y coordinates (top-left corner on the panel)
//            screenWidth, screenHeight,  // Destination width and height (size on the panel)
//            cameraX, cameraY,            // Source x and y coordinates (top-left corner of the source image)
//            cameraX + screenWidth, cameraY + screenHeight,  // Source width and height (size of the portion to draw)
//            null);     // ImageObserver (can be set to null)
}


