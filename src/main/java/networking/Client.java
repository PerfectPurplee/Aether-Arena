package networking;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;

import java.io.*;
import java.net.*;
import java.util.Optional;

public class Client extends Thread {


    public final DatagramSocket socket;
    public static InetAddress serverIPaddress;
    LocalPlayer localPlayer;

    private int ClientID;


    public Client(LocalPlayer localPlayer, String serverIPaddress) {
        this.localPlayer = localPlayer;

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


//           PACKET TYPE 0 IS SERVER ANSWER FOR LOGIN PACKET
            if (packetType == 0) {
                ClientID = dataInputStream.readInt();
                LocalPlayer.playerPosXWorld = dataInputStream.readFloat();
                LocalPlayer.playerPosYWorld = dataInputStream.readFloat();
            }
//          PACKET TYPE 1 IS FOR PLAYERS MOVEMENT
            if (packetType == 1) {
                int howManyPlayersToUpdate = dataInputStream.readInt();
                for (int i = 0; i < howManyPlayersToUpdate; i++) {

//                    Checks if received update is for local player
                    int serverClientID = dataInputStream.readInt();
                    Optional<OnlinePlayer> OptionalOnlinePlayer = OnlinePlayer.listOfAllConnectedOnlinePLayers.stream()
                            .filter(element -> element.onlinePlayerID == serverClientID).findFirst();
                    if (serverClientID == ClientID) {
                        LocalPlayer.playerPosXWorld = dataInputStream.readFloat();
                        LocalPlayer.playerPosYWorld = dataInputStream.readFloat();
                    } else if (OptionalOnlinePlayer.isPresent()) {
                        OptionalOnlinePlayer.get().playerPosXWorld = dataInputStream.readFloat();
                        OptionalOnlinePlayer.get().playerPosYWorld = dataInputStream.readFloat();

                    } else {
                        OnlinePlayer onlinePlayer = new OnlinePlayer(serverClientID);
                        onlinePlayer.playerPosXWorld = dataInputStream.readFloat();
                        onlinePlayer.playerPosYWorld = dataInputStream.readFloat();

                    }

                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
