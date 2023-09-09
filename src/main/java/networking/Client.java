package networking;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import main.EnumContainer;
import main.PlayerState;

import java.io.*;
import java.net.*;
import java.util.Optional;

public class Client extends Thread {


    public final DatagramSocket socket;
    public static InetAddress serverIPaddress;
    LocalPlayer localPlayer;

    public int ClientID;


    public Client(LocalPlayer localPlayer, String serverIPaddress) {
        this.localPlayer = localPlayer;

        try {
            socket = new DatagramSocket();
            Client.serverIPaddress = InetAddress.getByName("89.65.216.42");
//            Client.serverIPaddress = InetAddress.getLocalHost();
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

    private synchronized void receiveDataFromServer() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(packet);


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            ObjectInputStream dataInputStream = new ObjectInputStream(byteArrayInputStream);

            int packetType = dataInputStream.readInt();
//            System.out.println("Client received packet of TYPE: " + packetType);

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

//                    Local player
                    if (serverClientID == ClientID) {
                        PlayerState.Current_Player_State_Shared = (EnumContainer.PlayerState) dataInputStream.readObject();
//                        NO need for that just creates more lag, animation does not need to be server accurate
//                        localPlayer.Current_Player_State_Shared = PlayerState.Current_Player_State_Shared;
                        LocalPlayer.playerPosXWorld = dataInputStream.readFloat();
                        LocalPlayer.playerPosYWorld = dataInputStream.readFloat();
//                    Online player
                    } else if (OptionalOnlinePlayer.isPresent()) {
                        PlayerState.Current_Player_State_Shared = (EnumContainer.PlayerState) dataInputStream.readObject();
                        OptionalOnlinePlayer.get().Current_Player_State = PlayerState.Current_Player_State_Shared;
                        OptionalOnlinePlayer.get().playerPosXWorld = dataInputStream.readFloat();
                        OptionalOnlinePlayer.get().playerPosYWorld = dataInputStream.readFloat();
//                    New online player
                    } else {
                        OnlinePlayer onlinePlayer = new OnlinePlayer(serverClientID);
                        PlayerState.Current_Player_State_Shared = (EnumContainer.PlayerState) dataInputStream.readObject();
                        onlinePlayer.Current_Player_State = PlayerState.Current_Player_State_Shared;
                        onlinePlayer.playerPosXWorld = dataInputStream.readFloat();
                        onlinePlayer.playerPosYWorld = dataInputStream.readFloat();

                    }

                }

            }

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
