package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class AssetLoader {


    private Graphics2D g2d;
    private final int NUMBER_OF_PLAYABLE_CHARACTERS = 4;

//    Player Sprites

    public BufferedImage[][] playerSpriteIDLE_RIGHT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][6];
    public BufferedImage[][] playerSpriteIDLE_LEFT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][6];

    public BufferedImage[][] playerSpriteMOVE_LEFT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][8];
    public BufferedImage[][] playerSpriteMOVE_RIGHT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][8];

    public BufferedImage[][] playerSpriteDEATH_RIGHT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][10];
    public BufferedImage[][] playerSpriteDEATH_LEFT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][10];

    public BufferedImage[][] playerSpriteTAKE_DMG_RIGHT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][3];
    public BufferedImage[][] playerSpriteTAKE_DMG_LEFT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][3];

    public BufferedImage[][] playerSpriteROLL_RIGHT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][5];
    public BufferedImage[][] playerSpriteROLL_LEFT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][5];


    AssetLoader() {

        getPlayerSprites2Directional();
    }


    private void getPlayerSprites2Directional() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource;
        File folder;
        File[] spriteImages = null;

        for (int i = 0; i < EnumContainer.AllPlayableChampions.values().length; i++) {

            if (EnumContainer.AllPlayableChampions.values()[i] == EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE) {
                resource = classLoader.getResource("NewAssets/FullChar/Char1/withHands");
                folder = new File(Objects.requireNonNull(resource).getFile());
                spriteImages = folder.listFiles();
            } else if (EnumContainer.AllPlayableChampions.values()[i] == EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL) {
                resource = classLoader.getResource("NewAssets/FullChar/Char2/withHands");
                folder = new File(Objects.requireNonNull(resource).getFile());
                spriteImages = folder.listFiles();
            } else if (EnumContainer.AllPlayableChampions.values()[i] == EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE) {
                resource = classLoader.getResource("NewAssets/FullChar/Char3/withHands");
                folder = new File(Objects.requireNonNull(resource).getFile());
                spriteImages = folder.listFiles();
            } else if (EnumContainer.AllPlayableChampions.values()[i] == EnumContainer.AllPlayableChampions.CAPE_BALDY_DUDE) {
                resource = classLoader.getResource("NewAssets/FullChar/Char4/withHands");
                folder = new File(Objects.requireNonNull(resource).getFile());
                spriteImages = folder.listFiles();
            }


            String fileNameTemp = null;
            for (int j = 0, k = 0; j < Objects.requireNonNull(spriteImages).length; j++, k++) {
                if (j != 0) {
                    if (!spriteImages[j].getName().startsWith(String.valueOf(fileNameTemp.charAt(0)))) {
                        k = 0;
                    }
                }
                fileNameTemp = spriteImages[j].getName();
                if (spriteImages[j].getName().startsWith("death")) {
                    playerSpriteDEATH_RIGHT[i][k] = scaleImage(spriteImages[j]);
                    playerSpriteDEATH_LEFT[i][k] = flipImageHorizontally(spriteImages[j]);
                } else if (spriteImages[j].getName().startsWith("idle")) {
                    playerSpriteIDLE_RIGHT[i][k] = scaleImage(spriteImages[j]);
                    playerSpriteIDLE_LEFT[i][k] = flipImageHorizontally(spriteImages[j]);
                } else if (spriteImages[j].getName().startsWith("roll")) {
                    playerSpriteROLL_RIGHT[i][k] = scaleImage(spriteImages[j]);
                    playerSpriteROLL_LEFT[i][k] = flipImageHorizontally(spriteImages[j]);
                } else if (spriteImages[j].getName().startsWith("walk")) {
                    playerSpriteMOVE_RIGHT[i][k] = scaleImage(spriteImages[j]);
                    playerSpriteMOVE_LEFT[i][k] = flipImageHorizontally(spriteImages[j]);
                } else if (spriteImages[j].getName().startsWith("hit")) {
                    playerSpriteTAKE_DMG_RIGHT[i][k] = scaleImage(spriteImages[j]);
                    playerSpriteTAKE_DMG_LEFT[i][k] = flipImageHorizontally(spriteImages[j]);
                }
            }
        }
    }

    private BufferedImage scaleImage(File imageFile) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);

            double scale = 0.125;
            int newWidth = (int) (originalImage.getWidth() * scale);
            int newHeight = (int) (originalImage.getHeight() * scale);

            AffineTransformOp transform = new AffineTransformOp(AffineTransform.getScaleInstance(scale, scale), AffineTransformOp.TYPE_BICUBIC);
            return transform.filter(originalImage, new BufferedImage(newWidth, newHeight, originalImage.getType()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private BufferedImage flipImageHorizontally(File imageFile) {
        BufferedImage image = scaleImage(imageFile);
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        g2d = flippedImage.createGraphics();
        g2d.drawImage(image, width, 0, -width, height, null);

        return flippedImage;
    }


}
