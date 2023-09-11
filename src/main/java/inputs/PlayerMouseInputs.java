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


public class PlayerMouseInputs implements MouseListener, MouseMotionListener {

    public static Point CurrentMousePosition;
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

        switch (EnumContainer.AllScenes.Current_Scene) {

            case MENU -> {
            }
            case CHAMPION_SELECT -> {
                if (e.getSource() == championSelect.championChoice1) {
                    localPlayer.setPlayerChampion(EnumContainer.AllPlayableChampions.DON_OHL);
                    gameEngine.changeScene(EnumContainer.AllScenes.PLAYING);
                } else if (e.getSource() == championSelect.championChoice2) {
                    localPlayer.setPlayerChampion(EnumContainer.AllPlayableChampions.BIG_HAIRY_SWEATY_DUDE);
                    gameEngine.changeScene(EnumContainer.AllScenes.PLAYING);
                }

            }
            case PLAYING -> {
                CurrentMousePosition = e.getPoint();
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

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

        switch (EnumContainer.AllScenes.Current_Scene) {

            case MENU -> {
            }
            case CHAMPION_SELECT -> {
            }
            case PLAYING -> {
                CurrentMousePosition = e.getPoint();
                localPlayer.getVectorForPlayerMovement(e);
                Camera.updateCameraState(e);

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
    public void mouseMoved(MouseEvent e) {

        switch (EnumContainer.AllScenes.Current_Scene) {

            case PLAYING -> {
                CurrentMousePosition = e.getPoint();
                Camera.updateCameraState(e);
            }
        }

    }
}
