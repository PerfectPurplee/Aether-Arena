package inputs;

import main.GameEngine;
import main.MainPanel;

import java.awt.event.ActionEvent;

import static main.EnumContainer.AllScenes.*;

public class ActionListener implements java.awt.event.ActionListener {

    MainPanel mainPanel;
    GameEngine gameEngine;

    public ActionListener(MainPanel mainPanel, GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.mainPanel = mainPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (Current_Scene) {

            case PLAYING -> {
            }
            case MENU -> {

            }
            case PAUSE -> {
            }
            case MAP_EDITOR -> {
            }
        }

    }
}
