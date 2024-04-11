package inputs;

import entities.playercharacters.LocalPlayer;
import main.Camera;
import main.EnumContainer;
import main.GameEngine;
import main.MainFrame;
import networking.Client;
import networking.PacketManager;
import scenes.ChampionSelect;
import scenes.Menu;
import scenes.Pause;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import static main.EnumContainer.AllScenes;
import static main.EnumContainer.AllScenes.CHAMPION_SELECT;
import static main.EnumContainer.ServerClientConnectionCopyObjects;


public class PlayerMouseInputs implements MouseListener, MouseMotionListener {


    public static boolean mouseDragging;
    LocalPlayer localPlayer;
    ChampionSelect championSelect;
    public Client client;

    //  Instantiated in GameEngine
    public GameEngine gameEngine;
    public MainFrame mainFrame;
    public Pause pause;

    private int startingPosX;
    private int startingPosY;

    public PlayerMouseInputs(LocalPlayer localPlayer, ChampionSelect championSelect, GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        this.localPlayer = localPlayer;
        this.championSelect = championSelect;


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
//        Window moving
        if (e.getY() < 30) {
            startingPosX = e.getX();
            startingPosY = e.getY();
        } else {
            startingPosX = -1;
            startingPosY = -1;
        }
        switch (AllScenes.Current_Scene) {

            case MENU -> {
//                if (e.getSource() == Menu.startGameWithServer) {
//                   gameEngine.createServer();
//                    mainPanel.changeScene(PLAYING);
//                }
                if (e.getSource() == Menu.joinExistingGame) {
                    gameEngine.changeScene(CHAMPION_SELECT);

                }

                if (e.getSource() == Menu.settings) {


                }

                if (e.getSource() == Menu.exit) {
                    System.exit(0);

                }

            }
            case CHAMPION_SELECT -> {
                if (e.getSource() == championSelect.championLabels[0]) {
                    localPlayer.setPlayerChampion(EnumContainer.AllPlayableChampions.BLUE_HAIR_DUDE);
                    localPlayer.setScoreboardICON();
                    gameEngine.changeScene(AllScenes.PLAYING);
                } else if (e.getSource() == championSelect.championLabels[1]) {
                    localPlayer.setPlayerChampion(EnumContainer.AllPlayableChampions.PINK_HAIR_GIRL);
                    localPlayer.setScoreboardICON();
                    gameEngine.changeScene(AllScenes.PLAYING);
                } else if (e.getSource() == championSelect.championLabels[2]) {
                    localPlayer.setPlayerChampion(EnumContainer.AllPlayableChampions.BLOND_MOHAWK_DUDE);
                    localPlayer.setScoreboardICON();
                    gameEngine.changeScene(AllScenes.PLAYING);
                } else if (e.getSource() == championSelect.championLabels[3]) {
                    localPlayer.setPlayerChampion(EnumContainer.AllPlayableChampions.CAPE_BALDY_DUDE);
                    localPlayer.setScoreboardICON();
                    gameEngine.changeScene(AllScenes.PLAYING);
                }

                if (e.getSource() == championSelect.backToMenu) {
                    gameEngine.changeScene(AllScenes.MENU);
                }

            }
            case PLAYING -> {
                setCurrentMouseWorldPosition(e);
                if (!localPlayer.isPlayerDead) {
                    localPlayer.getVectorForPlayerMovement(e);
                    try {
                        client.socket.send(PacketManager.movementRequestPacket(localPlayer.mouseClickXPos, localPlayer.mouseClickYPos, client.ClientID));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case PAUSE -> {
                if (e.getSource() == pause.changeYourHeroButton) {
                    gameEngine.changeScene(CHAMPION_SELECT);
                }
                if (e.getSource() == pause.settingsButton) {

                }
                if (e.getSource() == pause.exitToMainMenuButton) {
                    gameEngine.changeScene(AllScenes.MENU);
                }
            }
            case SETTINGS -> {
            }
            case MAP_EDITOR -> {
            }
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDragging = false;

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        switch (AllScenes.Current_Scene) {
            case MENU -> {
                if (e.getSource() == Menu.startGameWithServer) {
                    Menu.startGameWithServer.setFont(Menu.googleExo2.deriveFont(28F));
                }
                if (e.getSource() == Menu.joinExistingGame) {
                    Menu.joinExistingGame.setFont(Menu.googleExo2.deriveFont(28F));
                }
                if (e.getSource() == Menu.settings) {
                    Menu.settings.setFont(Menu.googleExo2.deriveFont(28F));
                }
                if (e.getSource() == Menu.exit) {
                    Menu.exit.setFont(Menu.googleExo2.deriveFont(28F));
                }
            }
            case CHAMPION_SELECT -> {
                if (e.getSource() == championSelect.championLabels[0]) {
                    championSelect.isChampionBeingMouseHovered.put(0, true);
                }
                if (e.getSource() == championSelect.championLabels[1]) {
                    championSelect.isChampionBeingMouseHovered.put(1, true);
                }
                if (e.getSource() == championSelect.championLabels[2]) {
                    championSelect.isChampionBeingMouseHovered.put(2, true);
                }
                if (e.getSource() == championSelect.championLabels[3]) {
                    championSelect.isChampionBeingMouseHovered.put(3, true);
                }
                if (e.getSource() == championSelect.backToMenu) {
                    championSelect.backToMenu.setFont(Menu.googleExo2.deriveFont(32F));

                }

            }
            case PAUSE -> {
                if (e.getSource() == pause.changeYourHeroButton) {
                    pause.changeYourHeroButton.setFont(Menu.googleExo2.deriveFont(28F));
                }
                if (e.getSource() == pause.settingsButton) {
                    pause.settingsButton.setFont(Menu.googleExo2.deriveFont(28F));
                }
                if (e.getSource() == pause.exitToMainMenuButton) {
                    pause.exitToMainMenuButton.setFont(Menu.googleExo2.deriveFont(28F));
                }
            }
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {
        switch (AllScenes.Current_Scene) {
            case MENU -> {
                if (e.getSource() == Menu.startGameWithServer) {
                    Menu.startGameWithServer.setFont(Menu.googleExo2.deriveFont(26F));
                }
                if (e.getSource() == Menu.joinExistingGame) {
                    Menu.joinExistingGame.setFont(Menu.googleExo2.deriveFont(26F));
                }
                if (e.getSource() == Menu.settings) {
                    Menu.settings.setFont(Menu.googleExo2.deriveFont(26F));
                }
                if (e.getSource() == Menu.exit) {
                    Menu.exit.setFont(Menu.googleExo2.deriveFont(26F));
                }
            }
            case CHAMPION_SELECT -> {
                if (e.getSource() == championSelect.backToMenu) {
                    championSelect.backToMenu.setFont(Menu.googleExo2.deriveFont(30F));
                }
            }
            case PAUSE -> {
                if (e.getSource() == pause.changeYourHeroButton) {
                    pause.changeYourHeroButton.setFont(Menu.googleExo2.deriveFont(26F));
                }
                if (e.getSource() == pause.settingsButton) {
                    pause.settingsButton.setFont(Menu.googleExo2.deriveFont(26F));
                }
                if (e.getSource() == pause.exitToMainMenuButton) {
                    pause.exitToMainMenuButton.setFont(Menu.googleExo2.deriveFont(26F));
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        switch (AllScenes.Current_Scene) {

            case MENU -> {
            }
            case CHAMPION_SELECT -> {
            }
            case PLAYING -> {
                mouseDragging = true;
                setCurrentMouseWorldPosition(e);
                localPlayer.getVectorForPlayerMovement(e);
                Camera.updateCameraState(e);

            }
            case PAUSE -> {
            }
            case SETTINGS -> {
            }
            case MAP_EDITOR -> {
            }
        }

//        DRAGGING WINDOW
        if (e.getY() < 30 && startingPosX != -1 || startingPosY != -1) {
            mainFrame.setLocation(mainFrame.getX() + e.getX() - startingPosX, mainFrame.getY() + e.getY() - startingPosY);

        }

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        switch (AllScenes.Current_Scene) {

            case PLAYING -> {
                setCurrentMouseWorldPosition(e);
                Camera.updateCameraState(e);
            }
        }

    }

    private void setCurrentMouseWorldPosition(MouseEvent e) {
        ServerClientConnectionCopyObjects.currentMousePosition.setLocation(e.getX() + Camera.cameraPosX, e.getY() + Camera.cameraPosY);

    }
}
