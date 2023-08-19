package scenes.playing;

import main.MainFrame;
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

    public static final int Camera_Width = MainFrame.SCREEN_WIDTH;
    public static final int Camera_Height = MainFrame.SCREEN_HEIGHT;
    public int cameraPosX;
    public int cameraPosY;
    public int cameraSpeed = 2;
    private static int distanceToEdgeToMoveCamera;

    public Camera() {

        cameraMovingUP = false;
        cameraMovingDown = false;
        cameraMovingLeft = false;
        cameraMovingRight = false;
        distanceToEdgeToMoveCamera = 200;
        cameraPosX = 0;
        cameraPosY = 0;

    }

    public void updateCameraPosition() {
        if (cameraMovingUP && cameraPosY >= cameraSpeed) {
            cameraPosY -= cameraSpeed;

        }
        if (cameraMovingDown && cameraPosY + Camera_Height < MainPanel.worldSize.height - cameraSpeed) {
            cameraPosY += cameraSpeed;


        }
        if (cameraMovingLeft && cameraPosX >= cameraSpeed) {
            cameraPosX -= cameraSpeed;


        }
        if (cameraMovingRight && cameraPosX + Camera_Width < MainPanel.worldSize.width - cameraSpeed) {
            cameraPosX += cameraSpeed;


        }
    }

    public static void updateCameraState(MouseEvent e) {
        cameraMovingDown = e.getY() >= Camera_Height - distanceToEdgeToMoveCamera;
        cameraMovingUP = e.getY() <= distanceToEdgeToMoveCamera;
        cameraMovingLeft = e.getX() <= distanceToEdgeToMoveCamera;
        cameraMovingRight = e.getX() >= Camera_Width - distanceToEdgeToMoveCamera;


    }

    //    Implement in future if app will not be fullscreen
    public void mouseRestriction(MouseEvent e) {

    }

}
