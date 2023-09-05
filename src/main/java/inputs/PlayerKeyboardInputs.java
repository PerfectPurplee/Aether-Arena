package inputs;

import entities.playercharacters.LocalPlayer;
import entities.spells.basicspells.FirstSpell;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class PlayerKeyboardInputs implements KeyListener {


    LocalPlayer localPlayer;
    private HashSet<Integer> activeKeys = new HashSet<>();

    public PlayerKeyboardInputs(LocalPlayer localPlayer) {
        this.localPlayer = localPlayer;
    }

    //    Keys
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            new FirstSpell();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
