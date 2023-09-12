package networking;

import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import sharedObjects.Spell01;


import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Optional;

import static main.EnumContainer.*;

public class Client extends Thread {


    public static DatagramSocket socket;
    public static InetAddress serverIPaddress;
    LocalPlayer localPlayer;

    public static int ClientID;


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
            socket.send(PacketManager.LoginPacket(localPlayer));
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

    private synchronized void receiveDataFromServer() {
        byte[] buffer = new byte[100000];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(packet);


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            int packetType = objectInputStream.readInt();
//            System.out.println("Client received packet of TYPE: " + packetType);

//           PACKET TYPE 0 IS SERVER ANSWER FOR LOGIN PACKET
            if (packetType == 0) {
                ClientID = objectInputStream.readInt();
                LocalPlayer.playerPosXWorld = objectInputStream.readFloat();
                LocalPlayer.playerPosYWorld = objectInputStream.readFloat();
            }
//          PACKET TYPE 1 IS FOR PLAYERS MOVEMENT
            if (packetType == 1) {

                int howManyPlayersToUpdate = objectInputStream.readInt();
                for (int i = 0; i < howManyPlayersToUpdate; i++) {

//                    Checks if received update is for local player
                    int serverClientID = objectInputStream.readInt();
                    Optional<OnlinePlayer> OptionalOnlinePlayer = OnlinePlayer.listOfAllConnectedOnlinePLayers.stream()
                            .filter(element -> element.onlinePlayerID == serverClientID).findFirst();

//                    Local player
                    if (serverClientID == ClientID) {
                        ServerClientConnectionCopyObjects.Current_Player_State_Shared = (AllPlayerStates) objectInputStream.readObject();
                        ServerClientConnectionCopyObjects.PLayer_Champion_Shared = (AllPlayableChampions) objectInputStream.readObject();

                        LocalPlayer.playerPosXWorld = objectInputStream.readFloat();
                        LocalPlayer.playerPosYWorld = objectInputStream.readFloat();

//                    Online player
                    } else if (OptionalOnlinePlayer.isPresent()) {
                        ServerClientConnectionCopyObjects.Current_Player_State_Shared = (AllPlayerStates) objectInputStream.readObject();
                        ServerClientConnectionCopyObjects.PLayer_Champion_Shared = (AllPlayableChampions) objectInputStream.readObject();
                        OptionalOnlinePlayer.get().Current_Player_State_Online_Player = ServerClientConnectionCopyObjects.Current_Player_State_Shared;

                        OptionalOnlinePlayer.get().playerPosXWorld = objectInputStream.readFloat();
                        OptionalOnlinePlayer.get().playerPosYWorld = objectInputStream.readFloat();
//                    New online player
                    } else {
                        ServerClientConnectionCopyObjects.Current_Player_State_Shared = (AllPlayerStates) objectInputStream.readObject();
                        ServerClientConnectionCopyObjects.PLayer_Champion_Shared = (AllPlayableChampions) objectInputStream.readObject();
                        OnlinePlayer onlinePlayer = new OnlinePlayer(serverClientID);

                        onlinePlayer.Current_Player_State_Online_Player = ServerClientConnectionCopyObjects.Current_Player_State_Shared;
                        onlinePlayer.playerPosXWorld = objectInputStream.readFloat();
                        onlinePlayer.playerPosYWorld = objectInputStream.readFloat();

                    }

                }
            }
            if (packetType == 2)   {
//                clientID na razie do niczego nie uzyte
               int clientID = objectInputStream.readInt();
                ServerClientConnectionCopyObjects.listOfAllActiveSpellsCopy = (List<Spell01>) objectInputStream.readObject();
                Spell01.listOfActiveSpell01s = ServerClientConnectionCopyObjects.listOfAllActiveSpellsCopy;
            }

            objectInputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
