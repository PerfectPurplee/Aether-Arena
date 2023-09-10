package entities.playercharacters;

import main.EnumContainer;
import scenes.playing.Camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnlinePlayer {

    public BufferedImage allOnlinePlayerSprites;

    public BufferedImage[] playerSpriteIDLE_UP = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_DOWN = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_LEFT = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_RIGHT = new BufferedImage[2];

    public BufferedImage[] playerSpriteIDLE_UP_LEFT = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_UP_RIGHT = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_DOWN_LEFT = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_DOWN_RIGHT = new BufferedImage[2];

    public BufferedImage[] playerSpriteUP = new BufferedImage[8];
    public BufferedImage[] playerSpriteDOWN = new BufferedImage[8];
    public BufferedImage[] playerSpriteLEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteRIGHT = new BufferedImage[8];

    public BufferedImage[] playerSpriteUP_LEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteUP_RIGHT = new BufferedImage[8];
    public BufferedImage[] playerSpriteDOWN_LEFT = new BufferedImage[8];
    public BufferedImage[] playerSpriteDOWN_RIGHT = new BufferedImage[8];

    public BufferedImage[] currentPlayerSpriteOnlinePlayer;

    public EnumContainer.AllPlayerStates Current_Player_State_Online_Player;
    public EnumContainer.AllPlayableChampions onlinePlayerChampion;

    public float playerPosXWorld, playerPosYWorld;
    public float playerPosXScreen, playerPosYScreen;
    private int animationTick, animationSpeed = 15;
    public int animationIndexMoving, animationIndexIdle;

    public int onlinePlayerID;
    public boolean isPlayerMoving;

    public static List<OnlinePlayer> listOfAllConnectedOnlinePLayers = new ArrayList<>();


    public OnlinePlayer(int onlinePlayerID) {
        onlinePlayerChampion = EnumContainer.AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE;
        getPlayerSprites8Directional(onlinePlayerChampion);
        this.onlinePlayerID = onlinePlayerID;
        this.Current_Player_State_Online_Player = EnumContainer.AllPlayerStates.IDLE_DOWN;
        currentPlayerSpriteOnlinePlayer = playerSpriteController();


        listOfAllConnectedOnlinePLayers.add(this);

    }

    public void getPlayerSprites8Directional(EnumContainer.AllPlayableChampions onlinePlayerChampion) {

        int spriteSize = 0;
        int spriteXpos = 0;
        if (onlinePlayerChampion == EnumContainer.AllPlayableChampions.DON_OHL) {
            InputStream inputStream = getClass().getResourceAsStream("/DON_OHL.png");
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

            spriteSize = 72;

        } else if (onlinePlayerChampion == EnumContainer.AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE) {
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

            spriteSize = 128;
        }

//        Assigning moving sprites for all directions
        for (int i = 0; i < 8; i++) {
            playerSpriteDOWN[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteLEFT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize, spriteSize, spriteSize);
            playerSpriteRIGHT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 2, spriteSize, spriteSize);
            playerSpriteUP[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 3, spriteSize, spriteSize);
            playerSpriteDOWN_RIGHT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 4, spriteSize, spriteSize);
            playerSpriteDOWN_LEFT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 5, spriteSize, spriteSize);
            playerSpriteUP_RIGHT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 6, spriteSize, spriteSize);
            playerSpriteUP_LEFT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 7, spriteSize, spriteSize);

            spriteXpos += spriteSize;
        }

        spriteXpos = 0;
//       Assinging idle sprites for all directions
        for (int i = 0; i < 1; i++) {
            playerSpriteIDLE_DOWN[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteIDLE_LEFT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize, spriteSize, spriteSize);
            playerSpriteIDLE_RIGHT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 2, spriteSize, spriteSize);
            playerSpriteIDLE_UP[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 3, spriteSize, spriteSize);
            playerSpriteIDLE_DOWN_RIGHT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 4, spriteSize, spriteSize);
            playerSpriteIDLE_DOWN_LEFT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 5, spriteSize, spriteSize);
            playerSpriteIDLE_UP_RIGHT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 6, spriteSize, spriteSize);
            playerSpriteIDLE_UP_LEFT[i] = allOnlinePlayerSprites.getSubimage(spriteXpos, spriteSize * 7, spriteSize, spriteSize);


            spriteXpos += spriteSize;
        }


    }

    public void updatePlayerPositionOnScreen() {
        playerPosXScreen = playerPosXWorld - Camera.cameraPosX;
        playerPosYScreen = playerPosYWorld - Camera.cameraPosY;
    }


    public BufferedImage[] playerSpriteController() {
        switch (Current_Player_State_Online_Player) {
            case IDLE_UP -> {
                return playerSpriteIDLE_UP;
            }
            case IDLE_DOWN -> {
                return playerSpriteIDLE_DOWN;
            }
            case IDLE_LEFT -> {
                return playerSpriteIDLE_LEFT;
            }
            case IDLE_RIGHT -> {
                return playerSpriteIDLE_RIGHT;
            }
            case IDLE_UP_LEFT -> {
                return playerSpriteIDLE_UP_LEFT;
            }
            case IDLE_UP_RIGHT -> {
                return playerSpriteIDLE_UP_RIGHT;
            }
            case IDLE_DOWN_LEFT -> {
                return playerSpriteIDLE_DOWN_LEFT;
            }
            case IDLE_DOWN_RIGHT -> {
                return playerSpriteIDLE_DOWN_RIGHT;
            }
            case MOVING_UP -> {
                return playerSpriteUP;
            }
            case MOVING_DOWN -> {
                return playerSpriteDOWN;
            }
            case MOVING_LEFT -> {
                return playerSpriteLEFT;
            }
            case MOVING_RIGHT -> {
                return playerSpriteRIGHT;
            }
            case MOVING_UP_LEFT -> {
                return playerSpriteUP_LEFT;
            }
            case MOVING_UP_RIGHT -> {
                return playerSpriteUP_RIGHT;
            }
            case MOVING_DOWN_LEFT -> {
                return playerSpriteDOWN_LEFT;
            }
            case MOVING_DOWN_RIGHT -> {
                return playerSpriteDOWN_RIGHT;
            }
            default -> {
                return null;
            }
        }
    }


    public void animationController() {
        animationTick++;
        if (animationTick >= animationSpeed) {
            if (playerSpriteController() == playerSpriteIDLE_UP |
                    playerSpriteController() == playerSpriteIDLE_DOWN |
                    playerSpriteController() == playerSpriteIDLE_LEFT |
                    playerSpriteController() == playerSpriteIDLE_RIGHT |
                    playerSpriteController() == playerSpriteIDLE_UP_LEFT |
                    playerSpriteController() == playerSpriteIDLE_UP_RIGHT |
                    playerSpriteController() == playerSpriteIDLE_DOWN_LEFT |
                    playerSpriteController() == playerSpriteIDLE_DOWN_RIGHT) {

                if (animationIndexIdle < 1)
                    animationIndexIdle++;
                else animationIndexIdle = 0;
            } else if (playerSpriteController() == playerSpriteUP |
                    playerSpriteController() == playerSpriteDOWN |
                    playerSpriteController() == playerSpriteLEFT |
                    playerSpriteController() == playerSpriteRIGHT |
                    playerSpriteController() == playerSpriteUP_LEFT |
                    playerSpriteController() == playerSpriteUP_RIGHT |
                    playerSpriteController() == playerSpriteDOWN_LEFT |
                    playerSpriteController() == playerSpriteDOWN_RIGHT) {

                if (animationIndexMoving < 7)
                    animationIndexMoving++;
                else animationIndexMoving = 0;
            }
            animationTick = 0;
        }
    }

    public void checkIsOnlinePlayerMoving() {

        switch (Current_Player_State_Online_Player) {
            case IDLE_UP, IDLE_DOWN_RIGHT, IDLE_UP_LEFT, IDLE_DOWN_LEFT,
                    IDLE_UP_RIGHT, IDLE_RIGHT, IDLE_LEFT, IDLE_DOWN -> {
                isPlayerMoving = false;
            }
            case MOVING_UP, MOVING_DOWN_RIGHT, MOVING_DOWN, MOVING_LEFT,
                    MOVING_RIGHT, MOVING_UP_LEFT, MOVING_UP_RIGHT, MOVING_DOWN_LEFT -> {
                isPlayerMoving = true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + Current_Player_State_Online_Player);
        }
    }
}
