package inputs;

import entities.playercharacters.LocalPlayer;
import scenes.playing.Camera;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class PlayerMouseInputs implements MouseListener, MouseMotionListener {

    LocalPlayer localPlayer;
    public static Point CurrentMousePosition;

    public PlayerMouseInputs(LocalPlayer localPlayer) {
        this.localPlayer = localPlayer;

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        localPlayer.setPlayerMovementStartingPosition(LocalPlayer.playerPosXWorld, LocalPlayer.playerPosYWorld);
        localPlayer.mouseClickXPos = e.getX() + Camera.cameraPosX;
        localPlayer.mouseClickYPos = e.getY() + Camera.cameraPosY;

        float vectorX = localPlayer.mouseClickXPos - LocalPlayer.playerPosXWorld;
        float vectorY = localPlayer.mouseClickYPos - LocalPlayer.playerPosYWorld;
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        localPlayer.normalizedVectorX = (vectorX / magnitude);
        localPlayer.normalizedVectorY = (vectorY / magnitude);

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        CurrentMousePosition = e.getPoint();
        localPlayer.setPlayerMovementStartingPosition(LocalPlayer.playerPosXWorld, LocalPlayer.playerPosYWorld);
        localPlayer.mouseClickXPos = e.getX() + Camera.cameraPosX;
        localPlayer.mouseClickYPos = e.getY() + Camera.cameraPosY;


        float vectorX = localPlayer.mouseClickXPos - LocalPlayer.playerPosXWorld;
        float vectorY = localPlayer.mouseClickYPos - LocalPlayer.playerPosYWorld;
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        localPlayer.normalizedVectorX = (vectorX / magnitude);
        localPlayer.normalizedVectorY = (vectorY / magnitude);

        Camera.updateCameraState(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        CurrentMousePosition = e.getPoint();
        Camera.updateCameraState(e);
    }
}
