package scenes.playing;

import entities.playercharacters.PlayerClass;
import main.MainPanel;

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
    public final int Camera_Width = 30 * tileSize;
    public final int Camera_Height = 16 * tileSize;
    public int cameraPosX;
    public int cameraPosY;
    public int cameraSpeed = 2;
    private static int distanceToEdgeToMoveCamera = 50;
    public BufferedImage WHOLE_MAP;

    public Camera() {

        cameraMovingUP = false;
        cameraMovingDown = false;
        cameraMovingLeft = false;
        cameraMovingRight = false;
        getWholeMapImage();
        distanceToEdgeToMoveCamera = 200;

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
        InputStream inputStream = getClass().getResourceAsStream("/Map.png");
        try {
            WHOLE_MAP = ImageIO.read(Objects.requireNonNull(inputStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
