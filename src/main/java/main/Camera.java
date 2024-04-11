package main;

import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


public class Camera {

    private static boolean cameraMovingUP;
    private static boolean cameraMovingDown;
    private static boolean cameraMovingLeft;
    private static boolean cameraMovingRight;

    private final int tileSize = 16;
    public final int Camera_Width = MainPanel.gameSize.width;
    public final int Camera_Height = MainPanel.gameSize.height;
    public static int cameraPosX;
    public static int cameraPosY;
    public int cameraSpeed = 4;
    private static int distanceToEdgeToMoveCamera;

    public static BufferedImage WHOLE_MAP;
    public static BufferedImage WHOLE_MAP_OBJECTS;

    public BufferedImage currentCameraPosition;
    public BufferedImage currentObjectsPosition;

    public Camera(MapGenerator mapGenerator) {
        cameraPosX = 0;
        cameraPosY = 0;

        cameraMovingUP = false;
        cameraMovingDown = false;
        cameraMovingLeft = false;
        cameraMovingRight = false;
        WHOLE_MAP = mapGenerator.Whole_Map;
//        getWholeMapImage();
        distanceToEdgeToMoveCamera = 100;

    }

    public void updateCameraPosition() {
        if (cameraMovingUP && cameraPosY >= cameraSpeed) {
            cameraPosY -= cameraSpeed;

        }
        if (cameraMovingDown && cameraPosY + Camera_Height < WHOLE_MAP.getHeight() - cameraSpeed) {
            cameraPosY += cameraSpeed;


        }
        if (cameraMovingLeft && cameraPosX >= cameraSpeed) {
            cameraPosX -= cameraSpeed;


        }
        if (cameraMovingRight && cameraPosX + Camera_Width < WHOLE_MAP.getWidth() - cameraSpeed) {
            cameraPosX += cameraSpeed;


        }
    }

    //Not used in game loop, rather on MouseEvent
    public static void updateCameraState(MouseEvent e) {
        cameraMovingDown = e.getY() >= MainPanel.gameSize.getHeight() - distanceToEdgeToMoveCamera;
        cameraMovingUP = e.getY() <= distanceToEdgeToMoveCamera;
        cameraMovingLeft = e.getX() <= distanceToEdgeToMoveCamera;
        cameraMovingRight = e.getX() >= MainPanel.gameSize.getWidth() - distanceToEdgeToMoveCamera;


    }

    //    Implement in future if app will not be fullscreen
    public void mouseRestriction(MouseEvent e) {

    }

    public void getWholeMapImage() {
        InputStream inputStream = getClass().getResourceAsStream("/NewMap.png");
        try {
            WHOLE_MAP = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public void getWholeMapObjectsImage() {
//        InputStream inputStream = getClass().getResourceAsStream("/asset_map_version_smaller_objects.png");
//        try {
//            WHOLE_MAP_OBJECTS = ImageIO.read(Objects.requireNonNull(inputStream));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void updateCurrentCameraPosition() {
        currentCameraPosition = WHOLE_MAP.getSubimage(Camera.cameraPosX, Camera.cameraPosY, Camera_Width,
                Camera_Height);
    }

//    public void updateMapObjectsPosition() {
//        currentObjectsPosition = WHOLE_MAP_OBJECTS.getSubimage(Camera.cameraPosX, Camera.cameraPosY, Camera_Width,
//                Camera_Height);
//    }


    public void updateEverythingForCamera() {
        updateCameraPosition();
        updateCurrentCameraPosition();
//        updateMapObjectsPosition();
    }
}
