package inputs;

import entities.playercharacters.Player1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.Key;

import static entities.playercharacters.Player1.*;

public class PlayerInputs implements KeyListener, MouseListener {


    Player1 player1;

    public PlayerInputs(Player1 player1) {
        this.player1 = player1;
    }

    //    Keys
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == (KeyEvent.VK_W)) {
            player1.Current_Player_State = PlayerState.MOVING_UP;

        }
        if (e.getKeyCode() == (KeyEvent.VK_S)) {
            player1.Current_Player_State = PlayerState.MOVING_DOWN;

        }
        if (e.getKeyCode() == (KeyEvent.VK_A)) {
            player1.Current_Player_State = PlayerState.MOVING_LEFT;

        }
        if (e.getKeyCode() == (KeyEvent.VK_D)) {
            player1.Current_Player_State = PlayerState.MOVING_RIGHT;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == (KeyEvent.VK_W)) {
            player1.Current_Player_State = PlayerState.IDLE_UP;

        }
        if (e.getKeyCode() == (KeyEvent.VK_S)) {
            player1.Current_Player_State = PlayerState.IDLE_DOWN;

        }
        if (e.getKeyCode() == (KeyEvent.VK_A)) {
            player1.Current_Player_State = PlayerState.IDLE_LEFT;

        }
        if (e.getKeyCode() == (KeyEvent.VK_D)) {
            player1.Current_Player_State = PlayerState.IDLE_RIGHT;

        }

    }


    //    Mouse
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

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
