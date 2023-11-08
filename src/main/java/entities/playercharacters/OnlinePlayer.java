package entities.playercharacters;

import entities.Healthbar;
import main.EnumContainer;
import scenes.playing.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnlinePlayer {

    public BufferedImage allOnlinePlayerSprites;

    public BufferedImage[] playerSpriteIDLE_RIGHT = new BufferedImage[6];
    public BufferedImage[] playerSpriteIDLE_LEFT = new BufferedImage[6];

    public BufferedImage[] playerSpriteMOVE_LEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteMOVE_RIGHT = new BufferedImage[8];

    public BufferedImage[] playerSpriteDEATH_RIGHT = new BufferedImage[10];
    public BufferedImage[] playerSpriteDEATH_LEFT = new BufferedImage[10];


    public BufferedImage[] playerSpriteTAKE_DMG_RIGHT = new BufferedImage[3];
    public BufferedImage[] playerSpriteTAKE_DMG_LEFT = new BufferedImage[3];

    public BufferedImage[] playerSpriteROLL_RIGHT = new BufferedImage[5];
    public BufferedImage[] playerSpriteROLL_LEFT = new BufferedImage[5];

    public BufferedImage[] currentPlayerSpriteOnlinePlayer;

    public EnumContainer.AllPlayerStates Current_Player_State_Online_Player;
    public EnumContainer.AllPlayableChampions onlinePlayerChampion;

    public Healthbar healthbar;
    public OnlinePlayerHitbox onlinePlayerHitbox;

    public float playerPosXWorld, playerPosYWorld;
    public float playerPosXScreen, playerPosYScreen;

    private Graphics2D g2d;
    private int animationTick, animationSpeed = 15;
    public int animationIndexMoving, animationIndexIdle;

    public final int onlinePlayerID;
    public boolean isPlayerMoving;

    public static List<OnlinePlayer> listOfAllConnectedOnlinePLayers = new ArrayList<>();


    public OnlinePlayer(int onlinePlayerID) {
        onlinePlayerChampion = EnumContainer.ServerClientConnectionCopyObjects.PLayer_Champion_Shared;
        getPlayerSprites2Directional(onlinePlayerChampion);
        this.onlinePlayerID = onlinePlayerID;
        this.Current_Player_State_Online_Player = EnumContainer.AllPlayerStates.MOVING_RIGHT;
        currentPlayerSpriteOnlinePlayer = setCurrentOnlinePlayerSprite();
        setPlayerHealthBar();
        onlinePlayerHitbox = new OnlinePlayerHitbox();


        listOfAllConnectedOnlinePLayers.add(this);

    }

    private void setPlayerHealthBar() {

        healthbar = new Healthbar(4000, playerPosXScreen, playerPosYScreen);

//        PROTOTYPE CODE IF YOU WANT DIFFERENT HEALTH CAPACITY FOR DIFFERENT CHAMPIONS
//        switch (localPlayerChampion) {
//
//            case BLUE_HAIR_DUDE -> {
//            }
//            case PINK_HAIR_GIRL -> {
//                healthbar = new Healthbar(3000, playerPosXScreen, playerPosYScreen);
//
//            }
//        }

    }

    private BufferedImage flipImageHorizontally(File imageFile) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        g2d = flippedImage.createGraphics();
        g2d.drawImage(image, width, 0, -width, height, null);

        return flippedImage;
    }

    public void getPlayerSprites2Directional(EnumContainer.AllPlayableChampions localPlayerChampion) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource;

        if (localPlayerChampion == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE) {

            resource = classLoader.getResource("NewAssets/FullChar/Char1/withHands");
            File folder = new File(Objects.requireNonNull(resource).getFile());
            File[] spriteImages = folder.listFiles();

            String fileNameTemp = null;
            for (int i = 0, j = 0; i < Objects.requireNonNull(spriteImages).length; i++, j++) {
                if (i != 0) {
                    if (!spriteImages[i].getName().startsWith(String.valueOf(fileNameTemp.charAt(0)))) {
                        j = 0;
                    }
                }
                fileNameTemp = spriteImages[i].getName();
                if (spriteImages[i].getName().startsWith("death")) {
                    try {
                        playerSpriteDEATH_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteDEATH_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                } else if (spriteImages[i].getName().startsWith("idle")) {
                    try {
                        playerSpriteIDLE_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteIDLE_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (spriteImages[i].getName().startsWith("roll")) {
                    try {
                        playerSpriteROLL_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteROLL_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (spriteImages[i].getName().startsWith("walk")) {
                    try {
                        playerSpriteMOVE_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteMOVE_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (spriteImages[i].getName().startsWith("hit")) {
                    try {
                        playerSpriteTAKE_DMG_RIGHT[j] = ImageIO.read(spriteImages[i]);
                        playerSpriteTAKE_DMG_LEFT[j] = flipImageHorizontally(spriteImages[i]);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }


        } else if (localPlayerChampion == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL) {
            InputStream inputStream = getClass().getResourceAsStream("/WIKING_RUN.png");
            try {
                allOnlinePlayerSprites = ImageIO.read(Objects.requireNonNull(inputStream));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert inputStream != null;
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    public void updatePlayerPositionOnScreenAndHitbox() {
        playerPosXScreen = playerPosXWorld - Camera.cameraPosX;
        playerPosYScreen = playerPosYWorld - Camera.cameraPosY;
        updatePlayerHitboxWorldAndPosOnScreen();
    }


    public BufferedImage[] setCurrentOnlinePlayerSprite() {
        switch (Current_Player_State_Online_Player) {
            case IDLE_LEFT -> {
                return playerSpriteIDLE_LEFT;
            }
            case IDLE_RIGHT -> {
                return playerSpriteIDLE_RIGHT;
            }
            case MOVING_LEFT -> {
                return playerSpriteMOVE_LEFT;
            }
            case MOVING_RIGHT -> {
                return playerSpriteMOVE_RIGHT;
            }
            default -> {
                return null;
            }
        }
    }


    public void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (currentPlayerSpriteOnlinePlayer == playerSpriteIDLE_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteIDLE_RIGHT) {
                if (animationIndexIdle < 6) animationIndexIdle++;
                else animationIndexIdle = 0;
            } else if (currentPlayerSpriteOnlinePlayer == playerSpriteMOVE_LEFT || currentPlayerSpriteOnlinePlayer == playerSpriteMOVE_RIGHT) {
                if (animationIndexMoving < 8) animationIndexMoving++;
                else animationIndexMoving = 0;
            }
            animationTick = 0;
        }
    }

    public void checkIsOnlinePlayerMoving() {

        switch (Current_Player_State_Online_Player) {
            case IDLE_RIGHT, IDLE_LEFT -> {
                isPlayerMoving = false;
            }
            case MOVING_LEFT,
                    MOVING_RIGHT -> {
                isPlayerMoving = true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + Current_Player_State_Online_Player);
        }
    }

    public void updatePlayerHitboxWorldAndPosOnScreen() {
        onlinePlayerHitbox.x = playerPosXWorld;
        onlinePlayerHitbox.y = playerPosYWorld;
        onlinePlayerHitbox.playerHitboxPosXScreen = playerPosXScreen;
        onlinePlayerHitbox.playerHitboxPosYScreen = playerPosYScreen;

    }

    public class OnlinePlayerHitbox extends Rectangle2D.Float {

        public float playerHitboxPosXScreen, playerHitboxPosYScreen;

        OnlinePlayerHitbox() {
            super(playerPosXWorld, playerPosYWorld,
                    playerSpriteMOVE_RIGHT[0].getWidth(), playerSpriteMOVE_RIGHT[0].getHeight());
        }

    }
}
