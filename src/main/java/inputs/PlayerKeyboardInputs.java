package inputs;

import entities.playercharacters.LocalPlayer;
import entities.spells.basicspells.QSpell;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

public class PlayerKeyboardInputs implements KeyListener {


    LocalPlayer localPlayer;

    public static boolean Q_Pressed;
    public static boolean W_Pressed;
    public static boolean E_Pressed;
    public static boolean R_Pressed;
    public static boolean SHIFT_Pressed;


    public PlayerKeyboardInputs(LocalPlayer localPlayer) {
        this.localPlayer = localPlayer;
    }

    //    Keys
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_Q)
            Q_Pressed = true;
        if (e.getKeyCode() == KeyEvent.VK_W)
            W_Pressed = true;
        if (e.getKeyCode() == KeyEvent.VK_E)
            E_Pressed = true;
        if (e.getKeyCode() == KeyEvent.VK_R)
            R_Pressed = true;
        if (e.getKeyCode() == KeyEvent.VK_SHIFT)
            SHIFT_Pressed = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {
//        this one not used, but could be if you want to create only one spell on one key press.
        QSpell.QSpellCreatedOnThisMousePress = false;

        if (e.getKeyCode() == KeyEvent.VK_Q)
            Q_Pressed = false;
        if (e.getKeyCode() == KeyEvent.VK_W)
            W_Pressed = false;
        if (e.getKeyCode() == KeyEvent.VK_E)
            E_Pressed = false;
        if (e.getKeyCode() == KeyEvent.VK_R)
            R_Pressed = false;
        if (e.getKeyCode() == KeyEvent.VK_SHIFT)
            SHIFT_Pressed = false;
    }

}
