package inputs;

import entities.playercharacters.PlayerClass;
import scenes.playing.Camera;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


public class PlayerMouseInputs implements MouseListener, MouseMotionListener {

    PlayerClass playerClass;
    public static Point CurrentMousePosition;

    public PlayerMouseInputs(PlayerClass playerClass) {
        this.playerClass = playerClass;

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        playerClass.setPlayerMovementStartingPosition(PlayerClass.playerPosXWorld, PlayerClass.playerPosYWorld);
        playerClass.mouseClickXPos = e.getX() + Camera.cameraPosX;
        playerClass.mouseClickYPos = e.getY() + Camera.cameraPosY;

        float vectorX = playerClass.mouseClickXPos - PlayerClass.playerPosXWorld;
        float vectorY = playerClass.mouseClickYPos - PlayerClass.playerPosYWorld;
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        playerClass.normalizedVectorX = (vectorX / magnitude);
        playerClass.normalizedVectorY = (vectorY / magnitude);

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
        playerClass.setPlayerMovementStartingPosition(PlayerClass.playerPosXWorld, PlayerClass.playerPosYWorld);
        playerClass.mouseClickXPos = e.getX() + Camera.cameraPosX;
        playerClass.mouseClickYPos = e.getY() + Camera.cameraPosY;


        float vectorX = playerClass.mouseClickXPos - PlayerClass.playerPosXWorld;
        float vectorY = playerClass.mouseClickYPos - PlayerClass.playerPosYWorld;
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        playerClass.normalizedVectorX = (vectorX / magnitude);
        playerClass.normalizedVectorY = (vectorY / magnitude);

        Camera.updateCameraState(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        CurrentMousePosition = e.getPoint();
        Camera.updateCameraState(e);
    }
}
