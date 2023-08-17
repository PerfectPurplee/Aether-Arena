package inputs;

import entities.playercharacters.PlayerClass;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class PlayerKeyboardInputs implements KeyListener {


    PlayerClass playerClass;
    private HashSet<Integer> activeKeys = new HashSet<>();

    public PlayerKeyboardInputs(PlayerClass playerClass) {
        this.playerClass = playerClass;
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
