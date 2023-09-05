package networking;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;

import java.io.*;
import java.net.*;

public class Server extends Thread {


    private DatagramSocket serverSocket;
    private InetAddress ipAddress;
    private OnlinePlayer onlinePlayer;

    //    dane podłączonego klienta
    private InetAddress clientIpAddress;
    private int port;

    public Server(OnlinePlayer onlinePlayer) {

        this.onlinePlayer = onlinePlayer;

        try {
            this.serverSocket = new DatagramSocket(1337);
            this.ipAddress = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
            System.out.println(ipAddress.getHostAddress());
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.start();


    }

    public Server() {
        try {
            this.serverSocket = new DatagramSocket(1337);
            this.ipAddress = InetAddress.getLocalHost();
            System.out.println(ipAddress.getHostAddress());
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.start();


    }

    @Override
    public void run() {

        while (true) {
            receiveDataFromClient();
            sendDataToClient();

        }
    }

    private void receiveDataFromClient() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            serverSocket.receive(packet);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

            onlinePlayer.playerPosXWorld = dataInputStream.readFloat();
            onlinePlayer.playerPosYWorld = dataInputStream.readFloat();


            port = packet.getPort();
            clientIpAddress = packet.getAddress();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendDataToClient() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeFloat(LocalPlayer.playerPosXWorld);
            dataOutputStream.writeFloat(LocalPlayer.playerPosYWorld);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, clientIpAddress, port);

        try {
            serverSocket.send(datagramPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
