package inputs;

import entities.playercharacters.LocalPlayer;
import main.EnumContainer;
import main.GameEngine;
import networking.Client;
import networking.PacketManager;
import scenes.championselect.ChampionSelect;
import scenes.playing.Camera;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.DatagramPacket;

import static main.EnumContainer.*;


public class PlayerMouseInputs implements MouseListener, MouseMotionListener {


    public static boolean mouseDragging;
    LocalPlayer localPlayer;
    ChampionSelect championSelect;
    public Client client;
    public GameEngine gameEngine;

    public PlayerMouseInputs(LocalPlayer localPlayer, ChampionSelect championSelect) {
        this.localPlayer = localPlayer;
        this.championSelect = championSelect;


    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        switch (AllScenes.Current_Scene) {

            case MENU -> {
            }
            case CHAMPION_SELECT -> {
                if (e.getSource() == championSelect.championChoice1) {
                    localPlayer.setPlayerChampion(AllPlayableChampions.DON_OHL);
                    gameEngine.changeScene(AllScenes.PLAYING);
                } else if (e.getSource() == championSelect.championChoice2) {
                    localPlayer.setPlayerChampion(AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE);
                    gameEngine.changeScene(AllScenes.PLAYING);
                }

            }
            case PLAYING -> {
                setCurrentMouseWorldPosition(e);
                localPlayer.getVectorForPlayerMovement(e);
                try {
                    client.socket.send(PacketManager.movementRequestPacket(localPlayer.mouseClickXPos, localPlayer.mouseClickYPos, client.ClientID));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case PAUSE -> {
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

    }

    @Override
    public void mouseExited(MouseEvent e) {

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
