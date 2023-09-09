package inputs;

import main.GameEngine;
import main.MainPanel;
import scenes.menu.Menu;

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

//                if (e.getSource() == Menu.startGameWithServer) {
////                    gameEngine.createServer();
//                    mainPanel.changeScene(PLAYING);
//                }

                if (e.getSource() == Menu.joinExistingGame) {

                    gameEngine.createClient(gameEngine.getHostIpAddress());
                    mainPanel.changeScene(PLAYING);

                }

            }
            case PAUSE -> {
            }
            case MAP_EDITOR -> {
            }
        }

    }
}
