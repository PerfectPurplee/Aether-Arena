package main;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MapGenerator {


    public BufferedImage Whole_Map;
    private final Graphics2D g2d;
    private final AssetLoader assetLoader;
    private AffineTransform affineTransform;

    private final int MAP_WIDTH = 3840;
    private final int MAP_HEIGHT = 2160;
    private final int NUMBER_OF_OBJECTS = 100;

    private BufferedImage ground3White;
    private BufferedImage rock1;
    private BufferedImage rock2;
    private BufferedImage rock3;

    public MapGenerator(AssetLoader assetLoader) {
        this.assetLoader = assetLoader;
        affineTransform = new AffineTransform();
        Whole_Map = new BufferedImage(MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = Whole_Map.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        setObjectImages();
        initMapBaseGround();
        generateMapObjects();

    }

    private void initMapBaseGround() {
        g2d.setColor(new Color(128, 128, 128, 255));
        g2d.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
    }


    private void generateMapObjects() {
        Random random = new Random();

        for (int i = 0; i < NUMBER_OF_OBJECTS; i++) {
            int x = random.nextInt(MAP_WIDTH);
            int y = random.nextInt(MAP_HEIGHT);
            int randomObject = random.nextInt(1, 5);
            int size = random.nextInt(64, 256);
            double angle = Math.atan2(x, y);


            switch (randomObject) {
                case 1 -> {
                    affineTransform.rotate(
                            angle,(double) ground3White.getWidth() / 2, (double) ground3White.getHeight() / 2);
                    g2d.setTransform(affineTransform);
                    g2d.drawImage(ground3White, x, y, size, size, null);
                    affineTransform.setToIdentity();
                    g2d.setTransform(affineTransform);
                }
                case 2 -> g2d.drawImage(rock1, x, y, size, size, null);
                case 3 -> g2d.drawImage(rock2, x, y, size, size, null);
                case 4 -> g2d.drawImage(rock3, x, y, size, size, null);
                default -> System.err.println("Couldnt find object to draw on map");
            }

        }
    }

    private void setObjectImages() {
        ground3White = assetLoader.ground3White;
        rock1 = assetLoader.rock1;
        rock2 = assetLoader.rock2;
        rock3 = assetLoader.rock3;
    }
}

