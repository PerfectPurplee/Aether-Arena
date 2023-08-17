package inputs;

import entities.playercharacters.Player1;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class PlayerMouseInputs implements MouseListener {

    Player1 player1;


    public PlayerMouseInputs(Player1 player1) {
        this.player1 = player1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("mouse pressed");
//        player1.mouseClickXPos = e.getX();
//        player1.mouseClickYPos = e.getY();
//
//        int vectorX = player1.mouseClickXPos - player1.playerPosX;
//        int vectorY = player1.mouseClickYPos - player1.playerPosY;
//        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
//
//        player1.normalizedVectorX = vectorX / magnitude;
//        player1.normalizedVectorY = vectorY / magnitude;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        player1.playerMovementStartingPosition(player1.playerPosX, player1.playerPosY);
        player1.mouseClickXPos = e.getX();
        player1.mouseClickYPos = e.getY();

        float vectorX = player1.mouseClickXPos - player1.playerPosX;
        float vectorY = player1.mouseClickYPos - player1.playerPosY;
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);

        player1.normalizedVectorX =  (vectorX / magnitude);
        player1.normalizedVectorY =  (vectorY / magnitude);

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
}
