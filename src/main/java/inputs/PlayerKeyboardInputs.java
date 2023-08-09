package inputs;

import entities.playercharacters.Player1;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import static entities.playercharacters.Player1.*;

public class PlayerKeyboardInputs implements KeyListener {


    Player1 player1;
    private HashSet<Integer> activeKeys = new HashSet<>();

    public PlayerKeyboardInputs(Player1 player1) {
        this.player1 = player1;
    }

    //    Keys
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
