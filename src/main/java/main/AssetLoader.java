package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import static entities.spells.basicspells.Spell01.NUMBER_OF_SPRITES;

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

//    Map Objects Sprites

    public BufferedImage ground2White;
    public BufferedImage ground3White;
    public BufferedImage rock1;
    public BufferedImage rock2;
    public BufferedImage rock3;

    //    Spells Sprites
    public BufferedImage BasicSpellsSpriteSheetViolet;

    public static  BufferedImage[] QSpellViolet;


    AssetLoader() {
        getPlayerSprites2Directional();
        getAllBasicSpellsSpriteSheet();
        getSpriteForQSpellViolet();
        getMapObjects();
    }

    private void getMapObjects() {
        InputStream is1 = getClass().getResourceAsStream("/NewAssets/Environment/ground2_white.png");
        InputStream is2 = getClass().getResourceAsStream("/NewAssets/Environment/ground3_white.png");
        InputStream is3 = getClass().getResourceAsStream("/NewAssets/Environment/rock1.png");
        InputStream is4 = getClass().getResourceAsStream("/NewAssets/Environment/rock2.png");
        InputStream is5 = getClass().getResourceAsStream("/NewAssets/Environment/rock3.png");
        try {
            ground2White = ImageIO.read(Objects.requireNonNull(is1));
            ground3White = ImageIO.read(Objects.requireNonNull(is2));
            rock1 = ImageIO.read(Objects.requireNonNull(is3));
            rock2 = ImageIO.read(Objects.requireNonNull(is4));
            rock3 = ImageIO.read(Objects.requireNonNull(is5));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            is1.close();
            is2.close();
            is3.close();
            is4.close();
            is5.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    private BufferedImage addShadowToPlayerSprite(BufferedImage image) {
        BufferedImage shadowImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shadowImage.createGraphics();

        try {
            RadialGradientPaint gradient = new RadialGradientPaint(
                    (float) image.getWidth() / 2, image.getHeight(), (float) image.getWidth() / 2,
                    new float[]{0.0f, 1.0f},
                    new Color[]{new Color(0, 0, 0, 100), new Color(80, 80, 80, 0)});

            g2d.setPaint(gradient);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int shadowX = 90;
            int shadowY = image.getHeight() - 45;
            int shadowWidth = image.getWidth() - 90 * 2;
            int shadowHeight = 20;

            g2d.fill(new Ellipse2D.Double(shadowX, shadowY, shadowWidth, shadowHeight));
            g2d.drawImage(image, 0, 0, null);
        } finally {
            g2d.dispose();
        }

        return shadowImage;
    }

    private BufferedImage addShadowToQSpell(BufferedImage image) {
        BufferedImage shadowImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = shadowImage.createGraphics();

        try {
            RadialGradientPaint gradient = new RadialGradientPaint(
                    (float) image.getWidth() / 2, image.getHeight(), (float) image.getWidth() / 2,
                    new float[]{0.0f, 1.0f},
                    new Color[]{new Color(0, 0, 0, 255), new Color(0, 0, 0, 0)});

            g2d.setPaint(gradient);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int shadowX = 0;
            int shadowY = image.getHeight() - 20;
            int shadowWidth = image.getWidth();
            int shadowHeight = 20;

            g2d.fill(new Ellipse2D.Double(shadowX, shadowY, shadowWidth, shadowHeight));
            g2d.drawImage(image, 0, 0, null);
        } finally {
            g2d.dispose();
        }

        return shadowImage;
    }

    private BufferedImage scaleImage(File imageFile) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);

            double scale = 0.125;
            int newWidth = (int) (originalImage.getWidth() * scale);
            int newHeight = (int) (originalImage.getHeight() * scale);

            AffineTransformOp transform = new AffineTransformOp(AffineTransform.getScaleInstance(scale, scale), AffineTransformOp.TYPE_BICUBIC);
            originalImage = transform.filter(originalImage, new BufferedImage(newWidth, newHeight, originalImage.getType()));
            return addShadowToPlayerSprite(originalImage);

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

    private void getAllBasicSpellsSpriteSheet() {
        InputStream inputStream = getClass().getResourceAsStream("/AttackSprites.png");
        try {
            BasicSpellsSpriteSheetViolet = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage getSpriteForQSpellViolet() {
        QSpellViolet = new BufferedImage[NUMBER_OF_SPRITES];
        int xSpriteStartPos = 0;
        for (int i = 0; i < NUMBER_OF_SPRITES; i++) {
            QSpellViolet[i] = addShadowToQSpell(BasicSpellsSpriteSheetViolet.getSubimage(xSpriteStartPos, 18, 16, 16));
            xSpriteStartPos += 16;
        }
        return null;
    }


}
