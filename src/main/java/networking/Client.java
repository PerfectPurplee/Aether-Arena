package networking;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;

import java.io.*;
import java.net.*;

public class Client extends Thread {


    public final DatagramSocket socket;
    public static InetAddress serverIPaddress;
    LocalPlayer localPlayer;
    OnlinePlayer onlinePlayer;

    public Client(LocalPlayer localPlayer, OnlinePlayer onlinePlayer, String serverIPaddress) {
        this.localPlayer = localPlayer;
        this.onlinePlayer = onlinePlayer;
        try {
            socket = new DatagramSocket();
//            this.serverIPaddress = InetAddress.getByName(serverIPaddress);
            Client.serverIPaddress = InetAddress.getLocalHost();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.start();

        try {
            socket.send(PacketManager.LoginPacket());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void run() {

        while (true) {
            receiveDataFromServer();
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

    private void receiveDataFromServer() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(packet);
            System.out.println("Client received packet");

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

            int packetType = dataInputStream.readInt();

//           PACKET TYPE - IS SERVER ANSWER FOR LOGIN PACKET
            if (packetType == 0) {
                LocalPlayer.playerPosXWorld = dataInputStream.readFloat();
                LocalPlayer.playerPosYWorld = dataInputStream.readFloat();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
