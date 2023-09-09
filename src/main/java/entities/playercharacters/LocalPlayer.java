package entities.playercharacters;

import main.EnumContainer;
import scenes.playing.Camera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class LocalPlayer {

    public BufferedImage allPlayer1Sprites;

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

    public EnumContainer.PlayerState Current_Player_State;

    public static float playerPosXWorld, playerPosYWorld;
    public static float playerPosXScreen, playerPosYScreen;
    public int playerFeetX = 64, playerFeetY = 115;
    public int mouseClickXPos;
    public int mouseClickYPos;
    public float normalizedVectorX;
    public float normalizedVectorY;
    int playerMovespeed = 2;
    public float playerMovementStartingPosX, playerMovementStartingPosY;
    public float distanceToTravel;
    public boolean isPlayerMoving;


    private int animationTick, animationSpeed = 15;
    public int animationIndexMoving, animationIndexIdle;


    public LocalPlayer() {

        Current_Player_State = EnumContainer.PlayerState.MOVING_DOWN;
//        getPlayerSprites4Directional("/Player1.png");
        getPlayerSprites8Directional();
    }
//
//    public boolean checkIsCharacterMoving() {
//
////        switch (Current_Player_State) {
////            case IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT -> {
////                return false;
////            }
////            case MOVING_UP, MOVING_DOWN, MOVING_LEFT, MOVING_RIGHT, MOVING_UP_RIGHT,M-> {
//        return true;
////            }
////            default -> throw new IllegalStateException("Unexpected value: " + Current_Player_State);
////    }
//    }

    public void alternateMoveController() {
//
        if (distanceToTravel > 0) {

            playerPosXWorld += (playerMovespeed * normalizedVectorX);
            playerPosYWorld += (playerMovespeed * normalizedVectorY);
            distanceToTravel -= (float) (playerMovespeed * Math.sqrt(normalizedVectorX * normalizedVectorX + normalizedVectorY * normalizedVectorY));
            isPlayerMoving = true;
            setCurrent_Player_State();
        } else {
            isPlayerMoving = false;
            setCurrent_Player_State();
        }

    }

    public void setCurrent_Player_State() {
// double angle = atan2(y2 - y1, x2 - x1) * 180 / PI;".
        double movementAngle = Math.atan2(mouseClickYPos - playerMovementStartingPosY, mouseClickXPos - playerMovementStartingPosX);
        double angleDegrees = Math.toDegrees(movementAngle);
        if (angleDegrees < 0) {
            angleDegrees += 360;
        }
        if (isPlayerMoving) {
            if (angleDegrees >= 22.5 && angleDegrees < 67.5) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_DOWN_RIGHT;

            } else if (angleDegrees >= 67.5 && angleDegrees < 112.5) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_DOWN;

            } else if (angleDegrees >= 112.5 && angleDegrees < 157.5) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_DOWN_LEFT;

            } else if (angleDegrees >= 157.5 && angleDegrees < 202.5) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_LEFT;

            } else if (angleDegrees >= 202.5 && angleDegrees < 247.5) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_UP_LEFT;

            } else if (angleDegrees >= 247.5 && angleDegrees < 292.5) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_UP;

            } else if (angleDegrees >= 292.5 && angleDegrees < 337.5) {
                Current_Player_State = EnumContainer.PlayerState.MOVING_UP_RIGHT;

            } else {
                Current_Player_State = EnumContainer.PlayerState.MOVING_RIGHT;
            }
        } else {
            switch (Current_Player_State) {

                case MOVING_UP -> {
                    Current_Player_State = EnumContainer.PlayerState.IDLE_UP;
                }
                case MOVING_DOWN -> {
                    Current_Player_State = EnumContainer.PlayerState.IDLE_DOWN;
                }
                case MOVING_LEFT -> {
                    Current_Player_State = EnumContainer.PlayerState.IDLE_LEFT;
                }
                case MOVING_RIGHT -> {
                    Current_Player_State = EnumContainer.PlayerState.IDLE_RIGHT;
                }
                case MOVING_UP_LEFT -> {
                    Current_Player_State = EnumContainer.PlayerState.IDLE_UP_LEFT;
                }
                case MOVING_UP_RIGHT -> {
                    Current_Player_State = EnumContainer.PlayerState.IDLE_UP_RIGHT;
                }
                case MOVING_DOWN_LEFT -> {
                    Current_Player_State = EnumContainer.PlayerState.IDLE_DOWN_LEFT;
                }
                case MOVING_DOWN_RIGHT -> {
                    Current_Player_State = EnumContainer.PlayerState.IDLE_DOWN_RIGHT;
                }
            }

        }

    }

    public void setPlayerMovementStartingPosition(float playerPosXWorld, float playerPosYWorld) {
        this.playerMovementStartingPosX = playerPosXWorld + playerFeetX;
        this.playerMovementStartingPosY = playerPosYWorld + playerFeetY;
    }


    public void updatePlayerPositionOnScreen() {
        playerPosXScreen = playerPosXWorld - Camera.cameraPosX;
        playerPosYScreen = playerPosYWorld - Camera.cameraPosY;
    }

    public void getPlayerSprites8Directional() {
        InputStream inputStream = getClass().getResourceAsStream("/WIKING_RUN.png");
        try {
            allPlayer1Sprites = ImageIO.read(Objects.requireNonNull(inputStream));
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

        int spriteSize = 128;
        int spriteXpos = 0;

//        Assigning moving sprites for all directions
        for (int i = 0; i < 8; i++) {
            playerSpriteDOWN[i] = allPlayer1Sprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteLEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize, spriteSize, spriteSize);
            playerSpriteRIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 2, spriteSize, spriteSize);
            playerSpriteUP[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 3, spriteSize, spriteSize);
            playerSpriteDOWN_RIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 4, spriteSize, spriteSize);
            playerSpriteDOWN_LEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 5, spriteSize, spriteSize);
            playerSpriteUP_RIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 6, spriteSize, spriteSize);
            playerSpriteUP_LEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 7, spriteSize, spriteSize);

            spriteXpos += spriteSize;
        }

        spriteXpos = 0;
//       Assinging idle sprites for all directions
        for (int i = 0; i < 1; i++) {
            playerSpriteIDLE_DOWN[i] = allPlayer1Sprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteIDLE_LEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize, spriteSize, spriteSize);
            playerSpriteIDLE_RIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 2, spriteSize, spriteSize);
            playerSpriteIDLE_UP[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 3, spriteSize, spriteSize);
            playerSpriteIDLE_DOWN_RIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 4, spriteSize,spriteSize);
            playerSpriteIDLE_DOWN_LEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 5, spriteSize,spriteSize);
            playerSpriteIDLE_UP_RIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 6, spriteSize,spriteSize);
            playerSpriteIDLE_UP_LEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, spriteSize * 7, spriteSize,spriteSize);




            spriteXpos += spriteSize;
        }


    }

    public void getPlayerSprites4Directional(String classpath) {
        InputStream inputStream = getClass().getResourceAsStream(classpath);
        try {
            allPlayer1Sprites = ImageIO.read(Objects.requireNonNull(inputStream));
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

        int spriteSize = 72;
        int spriteXpos = 0;

//        Assigning moving sprites for all directions
        for (int i = 0; i < 4; i++) {
            playerSpriteDOWN[i] = allPlayer1Sprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteLEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, 72, spriteSize, spriteSize);
            playerSpriteRIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, 144, spriteSize, spriteSize);
            playerSpriteUP[i] = allPlayer1Sprites.getSubimage(spriteXpos, 216, spriteSize, spriteSize);
            spriteXpos += 72;
        }

        spriteXpos = 0;
//       Assinging idle sprites for all directions
        for (int i = 0; i < 2; i++) {
            playerSpriteIDLE_DOWN[i] = allPlayer1Sprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteIDLE_LEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, 72, spriteSize, spriteSize);
            playerSpriteIDLE_RIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, 144, spriteSize, spriteSize);
            playerSpriteIDLE_UP[i] = allPlayer1Sprites.getSubimage(spriteXpos, 216, spriteSize, spriteSize);

            spriteXpos += 144;
        }


    }

    public BufferedImage[] playerSpriteController() {
        switch (Current_Player_State) {
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


}

