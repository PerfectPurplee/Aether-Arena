package networking;

import entities.playercharacters.LocalPlayer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Client extends Thread {


    private DatagramSocket socket;
    private InetAddress serverIPaddress;
    LocalPlayer localPlayer;

    public Client(LocalPlayer localPlayer, String serverIPaddress) {
        this.localPlayer = localPlayer;

        try {
            socket = new DatagramSocket();
            this.serverIPaddress = InetAddress.getByName(serverIPaddress);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.start();

    }

    @Override
    public void run() {

        while (true) {
            sendDataToServer();
        }

    }


    private void sendDataToServer() {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeFloat(LocalPlayer.playerPosXWorld);
            dataOutputStream.writeFloat(LocalPlayer.playerPosYWorld);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, serverIPaddress, 1337);

        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
