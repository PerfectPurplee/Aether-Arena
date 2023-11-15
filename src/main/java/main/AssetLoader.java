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
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static entities.spells.basicspells.QSpell.NUMBER_OF_SPRITES;

public class AssetLoader {


    private Graphics2D g2d;
    private final int NUMBER_OF_PLAYABLE_CHARACTERS = 4;

    private final double scaleForPlayerSprites = 0.125;
    private final double scaleForSpellSprites = 1.5;
    private final double ScaleForUltimateSpells = 3;

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

    public BufferedImage[][] playerSpriteCAST_SPELL_LEFT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][5];
    public BufferedImage[][] playerSpriteCAST_SPELL_RIGHT = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS][5];


//    Map Objects Sprites
    public BufferedImage ground2White;
    public BufferedImage ground3White;
    public BufferedImage rock1;
    public BufferedImage rock2;
    public BufferedImage rock3;

//    USER INTEFACE
    public BufferedImage[] QSpellICON;
    public BufferedImage[] WSpellICON;
    public BufferedImage[] ESpellICON;
    public BufferedImage[] UltimateSpellICON;

    //    Spells Sprites OLD
    public BufferedImage BasicSpellsSpriteSheetViolet;
    public static BufferedImage[] QSpellViolet;
    //    Spells Sprites NEW
    //    Sprites For Q Spell:
    //    fireBall
    public static BufferedImage[] QSpellFireBallCastStart;
    public static BufferedImage[] QSpellFireballCastFlying;
    public static BufferedImage[] QSpellFireBallCastEnd;
    //    waterBall
    public static BufferedImage[] QSpellWaterBallCastStart;
    public static BufferedImage[] QSpellWaterBallCastFlying;
    public static BufferedImage[] QSpellWaterBallCastEnd;
    //    rockBall
    public static BufferedImage[] QSpellRockBallCastStart;
    public static BufferedImage[] QSpellRockBallCastFlying;
    public static BufferedImage[] QSpellRockBallCastEnd;
    //    windBall
    public static BufferedImage[] QSpellWindBallCastStart;
    public static BufferedImage[] QSpellWindBallCastFlying;
    public static BufferedImage[] QSpellWindBallCastEnd;

    //    Spell Sprites For Ultimate
    //    Fire
    public static BufferedImage[] ULTFireSpellCastStart;
    public static BufferedImage[] ULTFireSpellCastFlying;
    public static BufferedImage[] ULTFireSpellCastEnd;
    //    Water
    public static BufferedImage[] ULTWaterSpellCastStart;
    public static BufferedImage[] ULTWaterSpellCastFlying;
    public static BufferedImage[] ULTWaterSpellCastEnd;
    //    Rock
    public static BufferedImage[] ULTRockSpellCastStart;
    public static BufferedImage[] ULTRockSpellCastFlying;
    public static BufferedImage[] ULTRockSpellCastEnd;
    //    Wind
    public static BufferedImage[] ULTWindSpellCastStart;
    public static BufferedImage[] ULTWindSpellCastFlying;
    public static BufferedImage[] ULTWindSpellCastEnd;

    AssetLoader() {
        getPlayerSprites2Directional();
        getAllBasicSpellsSpriteSheet();
        getSpritesForSpells();
        getSpriteForQSpellViolet();
        getMapObjects();
        getSpritesForUltimateSpells();
    }

    private void getUserInterfaceIcons() {

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
                    playerSpriteDEATH_RIGHT[i][k] = addShadowToPlayerSprite(scaleImage(spriteImages[j], scaleForPlayerSprites));
                    playerSpriteDEATH_LEFT[i][k] = flipImageHorizontally(spriteImages[j], scaleForPlayerSprites);

                } else if (spriteImages[j].getName().startsWith("idle")) {
                    playerSpriteIDLE_RIGHT[i][k] = addShadowToPlayerSprite(scaleImage(spriteImages[j], scaleForPlayerSprites));
                    playerSpriteIDLE_LEFT[i][k] = addShadowToPlayerSprite(flipImageHorizontally(spriteImages[j], scaleForPlayerSprites));

                } else if (spriteImages[j].getName().startsWith("roll")) {
                    playerSpriteROLL_RIGHT[i][k] = addShadowToPlayerSprite(scaleImage(spriteImages[j], scaleForPlayerSprites));
                    playerSpriteROLL_LEFT[i][k] = addShadowToPlayerSprite(flipImageHorizontally(spriteImages[j], scaleForPlayerSprites));

                } else if (spriteImages[j].getName().startsWith("walk")) {
                    playerSpriteMOVE_RIGHT[i][k] = addShadowToPlayerSprite(scaleImage(spriteImages[j], scaleForPlayerSprites));
                    playerSpriteMOVE_LEFT[i][k] = addShadowToPlayerSprite(flipImageHorizontally(spriteImages[j], scaleForPlayerSprites));

                } else if (spriteImages[j].getName().startsWith("hit")) {
                    playerSpriteTAKE_DMG_RIGHT[i][k] = addShadowToPlayerSprite(scaleImage(spriteImages[j], scaleForPlayerSprites));
                    playerSpriteTAKE_DMG_LEFT[i][k] = addShadowToPlayerSprite(flipImageHorizontally(spriteImages[j], scaleForPlayerSprites));

                } else if (spriteImages[j].getName().startsWith("jump")) {
                    playerSpriteCAST_SPELL_RIGHT[i][k] = addShadowToPlayerSprite(scaleImage(spriteImages[j], scaleForPlayerSprites));
                    playerSpriteCAST_SPELL_LEFT[i][k] = addShadowToPlayerSprite(flipImageHorizontally(spriteImages[j], scaleForPlayerSprites));
                }
            }
        }

    }

    private void getAllBasicSpellsSpriteSheet() {
        InputStream inputStream = getClass().getResourceAsStream("/AttackSprites.png");
        try {
            BasicSpellsSpriteSheetViolet = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getSpriteForQSpellViolet() {
        QSpellViolet = new BufferedImage[NUMBER_OF_SPRITES];
        int xSpriteStartPos = 0;
        for (int i = 0; i < NUMBER_OF_SPRITES; i++) {
            QSpellViolet[i] = addShadowToQSpell(BasicSpellsSpriteSheetViolet.getSubimage(xSpriteStartPos, 18, 16, 16));
            xSpriteStartPos += 16;
        }
    }

    private void getSpritesForSpells() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource;
        File folder;
        File[] spriteImagesCast_Start;
        File[] spriteImagesCast_Flying;
        File[] spriteImagesCast_End;


        int numberOfUniqueSpells = 4;

        for (int i = 0; i < numberOfUniqueSpells; i++) {
            switch (i) {
//                FireBall
                case 0 -> {
                    resource = classLoader.getResource("SpellSprites/Fire_Ball/cast_start");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_Start = folder.listFiles();

                    resource = classLoader.getResource("SpellSprites/Fire_Ball/cast_flying");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_Flying = folder.listFiles();

                    resource = classLoader.getResource("SpellSprites/Fire_Ball/cast_end");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_End = folder.listFiles();

                    QSpellFireBallCastStart = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_Start), scaleForSpellSprites);
                    QSpellFireballCastFlying = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_Flying), scaleForSpellSprites);
                    QSpellFireBallCastEnd = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_End), scaleForSpellSprites);
                }
//                WaterBall
                case 1 -> {
                    resource = classLoader.getResource("SpellSprites/Water_Ball/cast_start");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_Start = folder.listFiles();

                    resource = classLoader.getResource("SpellSprites/Water_Ball/cast_flying");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_Flying = folder.listFiles();

                    resource = classLoader.getResource("SpellSprites/Water_Ball/cast_end");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_End = folder.listFiles();

                    QSpellWaterBallCastStart = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_Start), scaleForSpellSprites);
                    QSpellWaterBallCastFlying = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_Flying), scaleForSpellSprites);
                    QSpellWaterBallCastEnd = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_End), scaleForSpellSprites);
                }
//                RockBall
                case 2 -> {
                    resource = classLoader.getResource("SpellSprites/Rock_Ball/cast_start");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_Start = folder.listFiles();

                    resource = classLoader.getResource("SpellSprites/Rock_Ball/cast_flying");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_Flying = folder.listFiles();

                    resource = classLoader.getResource("SpellSprites/Rock_Ball/cast_end");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_End = folder.listFiles();

                    QSpellRockBallCastStart = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_Start), scaleForSpellSprites);
                    QSpellRockBallCastFlying = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_Flying), scaleForSpellSprites);
                    QSpellRockBallCastEnd = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_End), scaleForSpellSprites);
                }
//                WindBall
                case 3 -> {
                    resource = classLoader.getResource("SpellSprites/Wind_Ball/cast_start");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_Start = folder.listFiles();

                    resource = classLoader.getResource("SpellSprites/Wind_Ball/cast_flying");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_Flying = folder.listFiles();

                    resource = classLoader.getResource("SpellSprites/Wind_Ball/cast_end");
                    folder = new File(Objects.requireNonNull(resource).getFile());
                    spriteImagesCast_End = folder.listFiles();

                    QSpellWindBallCastStart = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_Start), scaleForSpellSprites);
                    QSpellWindBallCastFlying = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_Flying), scaleForSpellSprites);
                    QSpellWindBallCastEnd = loadImagesFromFolder(Objects.requireNonNull(spriteImagesCast_End), scaleForSpellSprites);
                    System.out.println("BASIC: " + " W " + QSpellFireballCastFlying[1].getWidth() + " H " + QSpellFireballCastFlying[1].getHeight());
                    System.out.println("BASIC: " + " W " + QSpellWindBallCastFlying[1].getWidth() + " H " + QSpellWaterBallCastFlying[1].getHeight());
                }
            }


        }


    }

    private void getSpritesForUltimateSpells() {

        for (int i = 0; i < 5; i++) {
//            fireball
            ULTFireSpellCastStart = scaleImage(QSpellFireBallCastStart, ScaleForUltimateSpells);
            ULTFireSpellCastFlying = scaleImage(QSpellFireballCastFlying, ScaleForUltimateSpells);
            ULTFireSpellCastEnd = scaleImage(QSpellFireBallCastEnd, ScaleForUltimateSpells);
            //    waterBall
            ULTWaterSpellCastStart = scaleImage(QSpellWaterBallCastStart, ScaleForUltimateSpells);
            ULTWaterSpellCastFlying = scaleImage(QSpellWaterBallCastFlying, ScaleForUltimateSpells);
            ULTWaterSpellCastEnd = scaleImage(QSpellWaterBallCastEnd, ScaleForUltimateSpells);
            //    rockBall
            ULTRockSpellCastStart = scaleImage(QSpellRockBallCastStart, ScaleForUltimateSpells);
            ULTRockSpellCastFlying = scaleImage(QSpellRockBallCastFlying, ScaleForUltimateSpells);
            ULTRockSpellCastEnd = scaleImage(QSpellRockBallCastEnd, ScaleForUltimateSpells);
            //    windBall
            ULTWindSpellCastStart = scaleImage(QSpellWindBallCastStart, ScaleForUltimateSpells);
            ULTWindSpellCastFlying = scaleImage(QSpellWindBallCastFlying, ScaleForUltimateSpells);
            ULTWindSpellCastEnd = scaleImage(QSpellWindBallCastEnd, ScaleForUltimateSpells);
        }
        System.out.println("ULTIMATE: " + " W " + ULTFireSpellCastFlying[1].getWidth() + " H " + ULTFireSpellCastFlying[1].getHeight());
        System.out.println("ULTIMATE: " + " W " + ULTRockSpellCastEnd[1].getWidth() + " H " + ULTRockSpellCastEnd[1].getHeight());
    }

    private BufferedImage[] loadImagesFromFolder(File[] folderWithImages) {
        Arrays.sort(folderWithImages, Comparator.comparing(File::getName));
        BufferedImage[] sprites = new BufferedImage[folderWithImages.length];
        for (int i = 0; i < folderWithImages.length; i++) {
            try {
                sprites[i] = ImageIO.read(folderWithImages[i]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return sprites;
    }

    // Not working yes
    private BufferedImage[] loadImagesFromFolder(File[] folderWithImages, double scale) {
        Arrays.sort(folderWithImages, Comparator.comparing(File::getName));
        BufferedImage[] sprites = new BufferedImage[folderWithImages.length];
        for (int i = 0; i < folderWithImages.length; i++) {
            sprites[i] = scaleImage(folderWithImages[i], scale);
        }
        return sprites;
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

    private BufferedImage scaleImage(File imageFile, double scale) {
        try {
            BufferedImage originalImage = ImageIO.read(imageFile);

            int newWidth = (int) (originalImage.getWidth() * scale);
            int newHeight = (int) (originalImage.getHeight() * scale);

            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            // Use RenderingHints to improve image quality
            RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            AffineTransformOp transform = new AffineTransformOp(
                    AffineTransform.getScaleInstance(scale, scale),
                    hints
            );

            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHints(hints);
            g2d.drawImage(originalImage, transform, 0, 0);
            g2d.dispose();

            return scaledImage;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage[] scaleImage(BufferedImage[] imageArray, double scale) {
        BufferedImage[] scaledImageArray = new BufferedImage[imageArray.length];
        for (int i = 0; i < imageArray.length; i++) {

            int newWidth = (int) (imageArray[i].getWidth() * scale);
            int newHeight = (int) (imageArray[i].getHeight() * scale);

            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

            RenderingHints hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            AffineTransformOp transform = new AffineTransformOp(
                    AffineTransform.getScaleInstance(scale, scale),
                    hints
            );

            Graphics2D g2d = scaledImage.createGraphics();
            g2d.setRenderingHints(hints);
            g2d.drawImage(imageArray[i], transform, 0, 0);
            g2d.dispose();

            scaledImageArray[i] = scaledImage;
        }

        return scaledImageArray;

    }

    private BufferedImage flipImageHorizontally(File imageFile, double ScaleForFlip) {
        BufferedImage image = scaleImage(imageFile, ScaleForFlip);
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        g2d = flippedImage.createGraphics();
        g2d.drawImage(image, width, 0, -width, height, null);

        return flippedImage;
    }


}
