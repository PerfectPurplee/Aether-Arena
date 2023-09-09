package inputs;

import entities.playercharacters.LocalPlayer;
import networking.Client;
import networking.PacketManager;
import scenes.playing.Camera;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;


public class PlayerMouseInputs implements MouseListener, MouseMotionListener {

    LocalPlayer localPlayer;
    public static Point CurrentMousePosition;
    public Client client;

    public PlayerMouseInputs(LocalPlayer localPlayer) {
        this.localPlayer = localPlayer;

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        localPlayer.setPlayerMovementStartingPosition(LocalPlayer.playerPosXWorld, LocalPlayer.playerPosYWorld);
        localPlayer.mouseClickXPos = e.getX() + Camera.cameraPosX;
        localPlayer.mouseClickYPos = e.getY() + Camera.cameraPosY;

        float vectorX = localPlayer.mouseClickXPos - (LocalPlayer.playerPosXWorld + localPlayer.playerFeetX);
        float vectorY = localPlayer.mouseClickYPos - (LocalPlayer.playerPosYWorld + localPlayer.playerFeetY);
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        localPlayer.distanceToTravel = magnitude;


        localPlayer.normalizedVectorX = (vectorX / magnitude);
        localPlayer.normalizedVectorY = (vectorY / magnitude);

        try {
            DatagramPacket packet = PacketManager.movementRequestPacket(localPlayer.mouseClickXPos, localPlayer.mouseClickYPos, client.ClientID);
            client.socket.send(packet);

//            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
//            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
//
//            System.out.println("packet type: " + dataInputStream.readInt() + " Send X: " + dataInputStream.readInt() + " Send Y: " + dataInputStream.readInt());
//            System.out.println("Local X: " + localPlayer.mouseClickXPos + "Local Y" + localPlayer.mouseClickYPos);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
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
        CurrentMousePosition = e.getPoint();
        localPlayer.setPlayerMovementStartingPosition(LocalPlayer.playerPosXWorld, LocalPlayer.playerPosYWorld);
        localPlayer.mouseClickXPos = e.getX() + Camera.cameraPosX;
        localPlayer.mouseClickYPos = e.getY() + Camera.cameraPosY;

        float vectorX = localPlayer.mouseClickXPos - (LocalPlayer.playerPosXWorld + localPlayer.playerFeetX);
        float vectorY = localPlayer.mouseClickYPos - (LocalPlayer.playerPosYWorld + localPlayer.playerFeetY);
        float magnitude = (float) Math.sqrt(vectorX * vectorX + vectorY * vectorY);
        localPlayer.distanceToTravel = magnitude;


        localPlayer.normalizedVectorX = (vectorX / magnitude);
        localPlayer.normalizedVectorY = (vectorY / magnitude);

        Camera.updateCameraState(e);

        try {
            client.socket.send(PacketManager.movementRequestPacket(localPlayer.mouseClickXPos, localPlayer.mouseClickYPos, client.ClientID));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        CurrentMousePosition = e.getPoint();
        Camera.updateCameraState(e);
    }
}
