package entities.playercharacters;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class PlayerClass {

    public enum PlayerState {
        IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT,
        MOVING_UP, MOVING_DOWN, MOVING_LEFT, MOVING_RIGHT

    }

    public BufferedImage allPlayer1Sprites;
    public BufferedImage[] playerSpriteIDLE_UP = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_DOWN = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_LEFT = new BufferedImage[2];
    public BufferedImage[] playerSpriteIDLE_RIGHT = new BufferedImage[2];

    public BufferedImage[] playerSpriteUP = new BufferedImage[4];
    public BufferedImage[] playerSpriteDOWN = new BufferedImage[4];
    public BufferedImage[] playerSpriteLEFT = new BufferedImage[4];
    public BufferedImage[] playerSpriteRIGHT = new BufferedImage[4];

    public PlayerState Current_Player_State;

    public float playerPosX, playerPosY;
    public int mouseClickXPos;
    public int mouseClickYPos;
    public float normalizedVectorX;
    public float normalizedVectorY;
    int playerMovespeed = 2;
    public float playerMovementStartingPosX, playerMovementStartingPosY;

    public PlayerClass(int playerPosX, int playerPosY) {

        this.playerPosX = playerPosX;
        this.playerPosY = playerPosY;

        Current_Player_State = PlayerState.MOVING_UP;
        getPlayerSprites();
    }

    public boolean checkIsCharacterMoving() {

        switch (Current_Player_State) {
            case IDLE_UP, IDLE_DOWN, IDLE_LEFT, IDLE_RIGHT -> {
                return false;
            }
            case MOVING_UP, MOVING_DOWN, MOVING_LEFT, MOVING_RIGHT -> {
                return true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + Current_Player_State);
        }
    }

    public void moveController() {
//
//        Ruch na lewej gornej cwiartce liczac od postaci
        if (playerMovementStartingPosX > mouseClickXPos && playerMovementStartingPosY > mouseClickYPos) {
            if (playerPosX > mouseClickXPos && playerPosY > mouseClickYPos) {
                Current_Player_State = PlayerState.MOVING_UP;
                playerPosX += (playerMovespeed * normalizedVectorX);
                playerPosY += (playerMovespeed * normalizedVectorY);
            }
        } else if (playerMovementStartingPosX < mouseClickXPos && playerMovementStartingPosY < mouseClickYPos) {
            if (playerPosX < mouseClickXPos && playerPosY < mouseClickYPos) {
                Current_Player_State = PlayerState.MOVING_DOWN;
                playerPosX += (playerMovespeed * normalizedVectorX);
                playerPosY += (playerMovespeed * normalizedVectorY);
            }
        } else if(playerMovementStartingPosX < mouseClickXPos && playerMovementStartingPosY > mouseClickYPos) {
            if (playerPosX < mouseClickXPos && playerPosY > mouseClickYPos) {
                Current_Player_State = PlayerState.MOVING_RIGHT;
                playerPosX += (playerMovespeed * normalizedVectorX);
                playerPosY += (playerMovespeed * normalizedVectorY);
            }
        } else if(playerMovementStartingPosX > mouseClickXPos && playerMovementStartingPosY < mouseClickYPos) {
            if (playerPosX > mouseClickXPos && playerPosY < mouseClickYPos) {
                Current_Player_State = PlayerState.MOVING_LEFT  ;
                playerPosX += (playerMovespeed * normalizedVectorX);
                playerPosY += (playerMovespeed * normalizedVectorY);
            }
        }
    }

    public void playerMovementStartingPosition(float playerMovementStartingPosX, float playerMovementStartingPosY) {
        this.playerMovementStartingPosX = playerMovementStartingPosX;
        this.playerMovementStartingPosY = playerMovementStartingPosY;
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
            default -> {
                return null;
            }
        }
    }


    private void getPlayerSprites() {
        InputStream inputStream = getClass().getResourceAsStream("/Player1.png");
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
}

