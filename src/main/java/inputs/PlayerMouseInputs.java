package inputs;

import entities.playercharacters.PlayerClass;

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
        playerClass.setPlayerMovementStartingPosition(PlayerClass.playerPosX, PlayerClass.playerPosY);
        playerClass.mouseClickXPos = e.getX();
        playerClass.mouseClickYPos = e.getY();

        float vectorX = playerClass.mouseClickXPos - PlayerClass.playerPosX;
        float vectorY = playerClass.mouseClickYPos - PlayerClass.playerPosY;
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
        playerClass.setPlayerMovementStartingPosition(PlayerClass.playerPosX, PlayerClass.playerPosY);
        playerClass.mouseClickXPos = e.getX();
        playerClass.mouseClickYPos = e.getY();


        float vectorX = playerClass.mouseClickXPos - PlayerClass.playerPosX;
        float vectorY = playerClass.mouseClickYPos - PlayerClass.playerPosY;
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        playerClass.normalizedVectorX = (vectorX / magnitude);
        playerClass.normalizedVectorY = (vectorY / magnitude);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        CurrentMousePosition = e.getPoint();
    }
}
