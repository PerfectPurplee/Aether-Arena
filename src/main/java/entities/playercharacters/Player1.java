package entities.playercharacters;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

public class Player1 {

    public BufferedImage[] playerSpriteIDLE = new BufferedImage[4];
    public BufferedImage[] playerSpriteUP = new BufferedImage[4];
    public BufferedImage[] playerSpriteDOWN = new BufferedImage[4];
    public BufferedImage[] playerSpriteLEFT = new BufferedImage[4];
    public BufferedImage[] playerSpriteRIGHT = new BufferedImage[4];

    public BufferedImage allPlayer1Sprites;

    public Player1() {

        getPlayerSprites();
    }

    public void moveController() {

    }

    private void getPlayerSprites() {
        InputStream inputStream = getClass().getResourceAsStream("/Player1.png");
        try {
            allPlayer1Sprites = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int spriteSize = 72;
        int spriteXpos = 0;
        for (int i = 0; i < 4; i++) {
            playerSpriteDOWN[i] = allPlayer1Sprites.getSubimage(spriteXpos, 0, spriteSize, spriteSize);
            playerSpriteLEFT[i] = allPlayer1Sprites.getSubimage(spriteXpos, 72, spriteSize, spriteSize);
            playerSpriteRIGHT[i] = allPlayer1Sprites.getSubimage(spriteXpos, 144, spriteSize, spriteSize);
            playerSpriteUP[i] = allPlayer1Sprites.getSubimage(spriteXpos, 216, spriteSize, spriteSize);
            spriteXpos += 72;
        }


    }
}
