package inputs;

import entities.playercharacters.Player1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.Key;
import java.util.HashSet;

import static entities.playercharacters.Player1.*;

public class PlayerInputs implements KeyListener, MouseListener {


    Player1 player1;
    private HashSet<Integer> activeKeys = new HashSet<>();

    public PlayerInputs(Player1 player1) {
        this.player1 = player1;
    }

    //    Keys
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();
        activeKeys.add(key); // Add the pressed key to the activeKeys set

        // Update player velocity based on activeKeys
        player1.playerVelX = 0;
        player1.playerVelY = 0;

        if (activeKeys.contains(KeyEvent.VK_W))
            player1.playerVelY -= 1;

        if (activeKeys.contains(KeyEvent.VK_S))
            player1.playerVelY += 1;

        if (activeKeys.contains(KeyEvent.VK_A))
            player1.playerVelX -= 1;

        if (activeKeys.contains(KeyEvent.VK_D))
            player1.playerVelX += 1;

        // Set the current player state based on the velocity
        if (player1.playerVelY < 0)
            player1.Current_Player_State = PlayerState.MOVING_UP;
        else if (player1.playerVelY > 0)
            player1.Current_Player_State = PlayerState.MOVING_DOWN;
        else if (player1.playerVelX < 0)
            player1.Current_Player_State = PlayerState.MOVING_LEFT;
        else if (player1.playerVelX > 0)
            player1.Current_Player_State = PlayerState.MOVING_RIGHT;
    }


    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();
        activeKeys.remove(key); // Remove the released key from the activeKeys set

        // Update player velocity based on activeKeys (same as in keyPressed)
        player1.playerVelX = 0;
        player1.playerVelY = 0;

        if (activeKeys.contains(KeyEvent.VK_W))
            player1.playerVelY -= 1;

        if (activeKeys.contains(KeyEvent.VK_S))
            player1.playerVelY += 1;

        if (activeKeys.contains(KeyEvent.VK_A))
            player1.playerVelX -= 1;

        if (activeKeys.contains(KeyEvent.VK_D))
            player1.playerVelX += 1;

        // Set the current player state based on the velocity (same as in keyPressed)
        if (player1.playerVelY < 0)
            player1.Current_Player_State = PlayerState.MOVING_UP;
        else if (player1.playerVelY > 0)
            player1.Current_Player_State = PlayerState.MOVING_DOWN;
        else if (player1.playerVelX < 0)
            player1.Current_Player_State = PlayerState.MOVING_LEFT;
        else if (player1.playerVelX > 0)
            player1.Current_Player_State = PlayerState.MOVING_RIGHT;
        else if (key == KeyEvent.VK_W)
            player1.Current_Player_State = PlayerState.IDLE_UP;
        else if (key == KeyEvent.VK_S)
            player1.Current_Player_State = PlayerState.IDLE_DOWN;
        else if (key == KeyEvent.VK_A)
            player1.Current_Player_State = PlayerState.IDLE_LEFT;
        else if (key == KeyEvent.VK_D)
            player1.Current_Player_State = PlayerState.IDLE_RIGHT;
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
