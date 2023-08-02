package entities.playercharacters;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class Player1 {

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

    public int playerPosX, playerPosY;
    public int playerMoveSpeed;

    public Player1(int playerPosX, int playerPosY) {

        this.playerPosX = playerPosX;
        this.playerPosY = playerPosY;
        playerMoveSpeed = 1;

        Current_Player_State = PlayerState.IDLE_DOWN;
        getPlayerSprites();
    }

    public void moveController() {
        if (Current_Player_State == PlayerState.MOVING_UP) {
            playerPosY -= playerMoveSpeed;

        }
        if (Current_Player_State == PlayerState.MOVING_DOWN) {
            playerPosY += playerMoveSpeed;

        }
        if (Current_Player_State == PlayerState.MOVING_LEFT) {
            playerPosX -= playerMoveSpeed;

        }
        if (Current_Player_State == PlayerState.MOVING_RIGHT) {
            playerPosX += playerMoveSpeed;
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

        spriteXpos = 72;
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
