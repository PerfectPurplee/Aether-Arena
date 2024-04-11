package inputs;

import entities.playercharacters.LocalPlayer;
import entities.spells.basicspells.QSpell;
import main.EnumContainer;
import main.GameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerKeyboardInputs implements KeyListener {


    private final GameEngine gameEngine;
    LocalPlayer localPlayer;

    public static boolean Q_Pressed;
    public static boolean W_Pressed;
    public static boolean E_Pressed;
    public static boolean R_Pressed;
    public static boolean SHIFT_Pressed;


    public PlayerKeyboardInputs(LocalPlayer localPlayer, GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.localPlayer = localPlayer;
    }

    //    Keys
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (EnumContainer.AllScenes.Current_Scene) {

            case MENU -> {
            }
            case CHAMPION_SELECT -> {
            }
            case PLAYING -> {
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
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    gameEngine.changeScene(EnumContainer.AllScenes.PAUSE);
            }
            case PAUSE -> {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    gameEngine.changeScene(EnumContainer.AllScenes.PLAYING);
            }
            case SETTINGS -> {
            }
            case MAP_EDITOR -> {
            }
        }


    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (EnumContainer.AllScenes.Current_Scene) {

            case MENU -> {
            }
            case CHAMPION_SELECT -> {
            }
            case PLAYING -> {
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
            case PAUSE -> {
            }
            case SETTINGS -> {
            }
            case MAP_EDITOR -> {
            }
        }

    }

}
