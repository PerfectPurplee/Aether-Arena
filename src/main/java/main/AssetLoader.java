package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import static entities.spells.basicspells.QSpell.NUMBER_OF_SPRITES;

public class AssetLoader {


    private Graphics2D g2d;
    private static final int NUMBER_OF_PLAYABLE_CHARACTERS = 4;

    private final double scaleForPlayerSprites = 0.125;
    private final double scaleForSpellSprites = 1.5;
    private final double ScaleForUltimateSpells = 3;

    private int k = 0;
    private String previousName;

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
    public static BufferedImage[] QSpellICON = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS];
    public static BufferedImage[] WSpellICON = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS];
    public static BufferedImage[] ESpellICON = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS];
    public static BufferedImage[] UltimateSpellICON = new BufferedImage[NUMBER_OF_PLAYABLE_CHARACTERS];

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
        getUserInterfaceIcons();
        championLoader();
        deBugger();
        getAllBasicSpellsSpriteSheet();
        getSpritesForSpells();
        getSpriteForQSpellViolet();
        getMapObjects();
        getSpritesForUltimateSpells();

    }

    private void getUserInterfaceIcons() {
        for (EnumContainer.AllPlayableChampions champion : EnumContainer.AllPlayableChampions.values()) {
            String filePathsForQIcons = getFilePathsForQIcons(champion);
            String filePathsForEIcons = getFilePathsForEIcons(champion);

            QSpellICON[champion.ordinal()] = scaleImage(Objects.requireNonNull(loadImage(filePathsForQIcons)), scaleForSpellSprites);
            ESpellICON[champion.ordinal()] = scaleImage(Objects.requireNonNull(loadImage(filePathsForEIcons)), scaleForSpellSprites);
            UltimateSpellICON[champion.ordinal()] = scaleImage(Objects.requireNonNull(loadImage(filePathsForQIcons)), scaleForSpellSprites);

        }
    }

    private String getFilePathsForEIcons(EnumContainer.AllPlayableChampions champion) {
        String result;
        switch (champion) {

            case BLUE_HAIR_DUDE -> result = "SpellSprites/Icons/tile008.png";
            case PINK_HAIR_GIRL -> result = "SpellSprites/Icons/tile006.png";
            case BLOND_MOHAWK_DUDE -> result = "SpellSprites/Icons/tile007.png";
            case CAPE_BALDY_DUDE -> result = "SpellSprites/Icons/tile009.png";
            default -> throw new IllegalStateException("Unexpected value: " + champion);
        }
        return result;
    }

    private String getFilePathsForQIcons(EnumContainer.AllPlayableChampions champion) {
        String result;
        switch (champion) {

            case BLUE_HAIR_DUDE -> result = "SpellSprites/icons/tile002.png";
            case PINK_HAIR_GIRL -> result = "SpellSprites/Icons/tile000.png";
            case BLOND_MOHAWK_DUDE -> result = "SpellSprites/Icons/tile001.png";
            case CAPE_BALDY_DUDE -> result = "SpellSprites/Icons/tile003.png";
            default -> throw new IllegalStateException("Unexpected value: " + champion);
        }
        return result;
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

    public ArrayList<String> getResourceListing(String path) {
        ArrayList<String> result = new ArrayList<>();
        try (
                InputStream stream = getClass().getClassLoader().getResourceAsStream(path)
        ) {
            assert stream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))
            ) {
                String resource;
                while ((resource = reader.readLine()) != null) {
                    result.add(resource);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
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

        String spriteImagesCast_Start;
        String spriteImagesCast_Flying;
        String spriteImagesCast_End;


        int numberOfUniqueSpells = 4;

        for (int i = 0; i < numberOfUniqueSpells; i++) {
            switch (i) {
//                FireBall
                case 0 -> {

                    spriteImagesCast_Start = "SpellSprites/Fire_Ball/cast_start/";
                    spriteImagesCast_Flying = "SpellSprites/Fire_Ball/cast_flying/";
                    spriteImagesCast_End = "SpellSprites/Fire_Ball/cast_end/";

                    BufferedImage[] sprite_Start = new BufferedImage[5];
                    BufferedImage[] sprite_Flying = new BufferedImage[5];
                    BufferedImage[] sprite_End = new BufferedImage[5];

                    for (int j = 1; j < 6; j++) {
                        sprite_Start[j - 1] = loadImage(spriteImagesCast_Start + j + ".png");
                        sprite_Flying[j - 1] = loadImage(spriteImagesCast_Flying + j + ".png");
                        sprite_End[j - 1] = loadImage(spriteImagesCast_End + j + ".png");
                    }

                    QSpellFireBallCastStart = scaleImage(sprite_Start, scaleForSpellSprites);
                    QSpellFireballCastFlying = scaleImage(sprite_Flying, scaleForSpellSprites);
                    QSpellFireBallCastEnd = scaleImage(sprite_End, scaleForSpellSprites);
                }
//                WaterBall
                case 1 -> {
                    spriteImagesCast_Start = "SpellSprites/Water_Ball/cast_start/";
                    spriteImagesCast_Flying = "SpellSprites/Water_Ball/cast_flying/";
                    spriteImagesCast_End = "SpellSprites/Water_Ball/cast_end/";

                    BufferedImage[] sprite_Start = new BufferedImage[5];
                    BufferedImage[] sprite_Flying = new BufferedImage[5];
                    BufferedImage[] sprite_End = new BufferedImage[5];

                    for (int j = 1; j < 6; j++) {
                        sprite_Start[j - 1] = loadImage(spriteImagesCast_Start + j + ".png");
                        sprite_Flying[j - 1] = loadImage(spriteImagesCast_Flying + j + ".png");
                        sprite_End[j - 1] = loadImage(spriteImagesCast_End + j + ".png");
                    }

                    QSpellWaterBallCastStart = scaleImage(sprite_Start, scaleForSpellSprites);
                    QSpellWaterBallCastFlying = scaleImage(sprite_Flying, scaleForSpellSprites);
                    QSpellWaterBallCastEnd = scaleImage(sprite_End, scaleForSpellSprites);
                }
//                RockBall
                case 2 -> {
                    spriteImagesCast_Start = "SpellSprites/Rock_Ball/cast_start/";
                    spriteImagesCast_Flying = "SpellSprites/Rock_Ball/cast_flying/";
                    spriteImagesCast_End = "SpellSprites/Rock_Ball/cast_end/";

                    BufferedImage[] sprite_Start = new BufferedImage[5];
                    BufferedImage[] sprite_Flying = new BufferedImage[5];
                    BufferedImage[] sprite_End = new BufferedImage[5];

                    for (int j = 1; j < 6; j++) {
                        sprite_Start[j - 1] = loadImage(spriteImagesCast_Start + j + ".png");
                        sprite_Flying[j - 1] = loadImage(spriteImagesCast_Flying + j + ".png");
                        sprite_End[j - 1] = loadImage(spriteImagesCast_End + j + ".png");
                    }

                    QSpellRockBallCastStart = scaleImage(sprite_Start, scaleForSpellSprites);
                    QSpellRockBallCastFlying = scaleImage(sprite_Flying, scaleForSpellSprites);
                    QSpellRockBallCastEnd = scaleImage(sprite_End, scaleForSpellSprites);
                }
//                WindBall
                case 3 -> {
                    spriteImagesCast_Start = "SpellSprites/Wind_Ball/cast_start/";
                    spriteImagesCast_Flying = "SpellSprites/Wind_Ball/cast_flying/";
                    spriteImagesCast_End = "SpellSprites/Wind_Ball/cast_end/";

                    BufferedImage[] sprite_Start = new BufferedImage[5];
                    BufferedImage[] sprite_Flying = new BufferedImage[5];
                    BufferedImage[] sprite_End = new BufferedImage[5];

                    for (int j = 1; j < 6; j++) {
                        sprite_Start[j - 1] = loadImage(spriteImagesCast_Start + j + ".png");
                        sprite_Flying[j - 1] = loadImage(spriteImagesCast_Flying + j + ".png");
                        sprite_End[j - 1] = loadImage(spriteImagesCast_End + j + ".png");
                    }

                    QSpellWindBallCastStart = scaleImage(sprite_Start, scaleForSpellSprites);
                    QSpellWindBallCastFlying = scaleImage(sprite_Flying, scaleForSpellSprites);
                    QSpellWindBallCastEnd = scaleImage(sprite_End, scaleForSpellSprites);
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
//        System.out.println("ULTIMATE: " + " W " + ULTFireSpellCastFlying[1].getWidth() + " H " + ULTFireSpellCastFlying[1].getHeight());
//        System.out.println("ULTIMATE: " + " W " + ULTRockSpellCastEnd[1].getWidth() + " H " + ULTRockSpellCastEnd[1].getHeight());
    }

    private BufferedImage[] loadImagesFromFolder(String folderPath, ArrayList<String> imagesNames) {
        BufferedImage[] result = new BufferedImage[imagesNames.size()];
        for (int i = 0; i < imagesNames.size(); i++) {
            InputStream is = getClass().getClassLoader().getResourceAsStream(folderPath + "/" + imagesNames.get(i));
            try {
                result[i] = ImageIO.read(Objects.requireNonNull(is));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    // Not working yes
//    private BufferedImage[] loadImagesFromFolder(File[] folderWithImages, double scale) {
//        Arrays.sort(folderWithImages, Comparator.comparing(File::getName));
//        BufferedImage[] sprites = new BufferedImage[folderWithImages.length];
//        for (int i = 0; i < folderWithImages.length; i++) {
//            sprites[i] = scaleImage(folderWithImages[i], scale);
//        }
//        return sprites;
//    }

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

    private BufferedImage scaleImage(InputStream inputStream, double scale) {
        try {
            BufferedImage originalImage = ImageIO.read(Objects.requireNonNull(inputStream));


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

    private BufferedImage scaleImage(BufferedImage originalImage, double scale) {

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

    private BufferedImage flipImageHorizontally(InputStream inputStreamWithImage, double ScaleForFlip) {
        BufferedImage image = scaleImage(inputStreamWithImage, ScaleForFlip);
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        g2d = flippedImage.createGraphics();
        g2d.drawImage(image, width, 0, -width, height, null);

        return flippedImage;
    }

    private BufferedImage flipImageHorizontally(BufferedImage originalImage, double ScaleForFlip) {
        BufferedImage image = scaleImage(originalImage, ScaleForFlip);
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage flippedImage = new BufferedImage(width, height, image.getType());
        Graphics2D g2d;
        g2d = flippedImage.createGraphics();
        g2d.drawImage(image, width, 0, -width, height, null);

        return flippedImage;
    }


    private void deBugger() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.println("IDLE RIGHT: " + playerSpriteIDLE_RIGHT[i][j].getWidth());
                System.out.println("IDLE LEFT: " + playerSpriteIDLE_LEFT[i][j].getWidth());

                System.out.println("ROLL RIGHT: " + playerSpriteROLL_RIGHT[i][j].getWidth());
                System.out.println("ROLL LEFT: " + playerSpriteROLL_LEFT[i][j].getWidth());


            }
        }
    }

    private String getChampionFolder(EnumContainer.AllPlayableChampions champion) {
        return switch (champion) {
            case BLUE_HAIR_DUDE -> "NewAssets/FullChar/Char1/withHands/";
            case PINK_HAIR_GIRL -> "NewAssets/FullChar/Char2/withHands/";
            case BLOND_MOHAWK_DUDE -> "NewAssets/FullChar/Char3/withHands/";
            case CAPE_BALDY_DUDE -> "NewAssets/FullChar/Char4/withHands/";
            default -> throw new IllegalArgumentException("Unknown champion: " + champion);
        };
    }

    private static BufferedImage loadImage(String path) {
        try {
            URL imageURL = AssetLoader.class.getClassLoader().getResource(path);
            if (imageURL != null) {
                return ImageIO.read(imageURL);
            } else {
                System.err.println("Image not found: " + path);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            return null;
        }
    }

    private String setFileBeginName(int folderIndex) {
        String fileNameBeginsWith;
        switch (folderIndex) {
            case 0 -> {
                fileNameBeginsWith = "death_";
            }
            case 1 -> {
                fileNameBeginsWith = "idle_";
            }
            case 2 -> {
                fileNameBeginsWith = "cast_";
            }
            case 3 -> {
                fileNameBeginsWith = "roll_";
            }
            case 4 -> {
                fileNameBeginsWith = "walk_";
            }
            case 5 -> {
                fileNameBeginsWith = "hit_";
            }
            default -> throw new IllegalStateException("Unexpected value: " + folderIndex);
        }
        return fileNameBeginsWith;
    }

    private int NumberOfIndexesForAnimation(String fileNameBeginsWith) {
        int result;
        switch (fileNameBeginsWith) {
            case "death_" -> result = 10;
            case "idle_" -> result = 6;
            case "cast_" -> result = 5;
            case "roll_" -> result = 5;
            case "walk_" -> result = 8;
            case "hit_" -> result = 3;
            default -> throw new IllegalStateException("Unexpected value: " + fileNameBeginsWith);
        }
        return result;
    }

    private void championLoader() {
        for (int i = 0; i < EnumContainer.AllPlayableChampions.values().length; i++) {
            String championFolder = getChampionFolder(EnumContainer.AllPlayableChampions.values()[i]);

            for (int typeOfAnimation = 0; typeOfAnimation < 6; typeOfAnimation++) {
                String fileNameBeginsWith = setFileBeginName(typeOfAnimation);
                for (int imageIndex = 0; imageIndex < NumberOfIndexesForAnimation(fileNameBeginsWith); imageIndex++) {
                    BufferedImage image = loadImage
                            (championFolder + fileNameBeginsWith + imageIndex + ".png");
                    if (image == null) {
                        System.out.println("Not WORKING IMG NULL");
                    }
                    assert image != null;
                    switch (fileNameBeginsWith) {
                        case "death_" -> {
                            playerSpriteDEATH_RIGHT[i][imageIndex] = addShadowToPlayerSprite(scaleImage(image, scaleForPlayerSprites));
                            playerSpriteDEATH_LEFT[i][imageIndex] = addShadowToPlayerSprite(flipImageHorizontally(image, scaleForPlayerSprites));
                        }
                        case "idle_" -> {
                            playerSpriteIDLE_RIGHT[i][imageIndex] = addShadowToPlayerSprite(scaleImage(image, scaleForPlayerSprites));
                            playerSpriteIDLE_LEFT[i][imageIndex] = addShadowToPlayerSprite(flipImageHorizontally(image, scaleForPlayerSprites));
                        }
                        case "cast_" -> {
                            playerSpriteCAST_SPELL_RIGHT[i][imageIndex] = addShadowToPlayerSprite(scaleImage(image, scaleForPlayerSprites));
                            playerSpriteCAST_SPELL_LEFT[i][imageIndex] = addShadowToPlayerSprite(flipImageHorizontally(image, scaleForPlayerSprites));
                        }
                        case "roll_" -> {
                            playerSpriteROLL_RIGHT[i][imageIndex] = addShadowToPlayerSprite(scaleImage(image, scaleForPlayerSprites));
                            playerSpriteROLL_LEFT[i][imageIndex] = addShadowToPlayerSprite(flipImageHorizontally(image, scaleForPlayerSprites));
                        }
                        case "walk_" -> {
                            playerSpriteMOVE_RIGHT[i][imageIndex] = addShadowToPlayerSprite(scaleImage(image, scaleForPlayerSprites));
                            playerSpriteMOVE_LEFT[i][imageIndex] = addShadowToPlayerSprite(flipImageHorizontally(image, scaleForPlayerSprites));
                        }
                        case "hit_" -> {
                            playerSpriteTAKE_DMG_RIGHT[i][imageIndex] = addShadowToPlayerSprite(scaleImage(image, scaleForPlayerSprites));
                            playerSpriteTAKE_DMG_LEFT[i][imageIndex] = addShadowToPlayerSprite(flipImageHorizontally(image, scaleForPlayerSprites));
                        }
                    }

                }
            }
        }
    }


}
