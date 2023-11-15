package networking;

import datatransferobjects.Spell01DTO;
import entities.playercharacters.LocalPlayer;
import entities.playercharacters.OnlinePlayer;
import entities.spells.basicspells.QSpell;
import entities.spells.basicspells.Ultimate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
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

    private void receiveDataFromServer() {
        byte[] buffer = new byte[2048];
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
                        localPlayer.healthbar.currentHealth = objectInputStream.readInt();

//                    Online player
                    } else if (OptionalOnlinePlayer.isPresent()) {
                        ServerClientConnectionCopyObjects.Current_Player_State_Shared = (AllPlayerStates) objectInputStream.readObject();
                        ServerClientConnectionCopyObjects.PLayer_Champion_Shared = (AllPlayableChampions) objectInputStream.readObject();
                        if (!OptionalOnlinePlayer.get().isPlayerStateLocked
                                || ServerClientConnectionCopyObjects.Current_Player_State_Shared == AllPlayerStates.DASHING_LEFT
                                || ServerClientConnectionCopyObjects.Current_Player_State_Shared == AllPlayerStates.DASHING_RIGHT)
                            OptionalOnlinePlayer.get().Current_Player_State_Online_Player = ServerClientConnectionCopyObjects.Current_Player_State_Shared;

                        OptionalOnlinePlayer.get().playerPosXWorld = objectInputStream.readFloat();
                        OptionalOnlinePlayer.get().playerPosYWorld = objectInputStream.readFloat();
                        OptionalOnlinePlayer.get().healthbar.currentHealth = objectInputStream.readInt();
//                    New online player
                    } else {
                        ServerClientConnectionCopyObjects.Current_Player_State_Shared = (AllPlayerStates) objectInputStream.readObject();
                        ServerClientConnectionCopyObjects.PLayer_Champion_Shared = (AllPlayableChampions) objectInputStream.readObject();
                        OnlinePlayer onlinePlayer = new OnlinePlayer(serverClientID);
                        if (!onlinePlayer.isPlayerStateLocked)
                            onlinePlayer.Current_Player_State_Online_Player = ServerClientConnectionCopyObjects.Current_Player_State_Shared;
                        onlinePlayer.playerPosXWorld = objectInputStream.readFloat();
                        onlinePlayer.playerPosYWorld = objectInputStream.readFloat();
                        onlinePlayer.healthbar.currentHealth = objectInputStream.readInt();

                    }

                }
            }
            if (packetType == 2) {
                int clientID = objectInputStream.readInt();

                while (objectInputStream.available() > 0) {
                    int spellID = objectInputStream.readInt();
                    int spellCasterClientID = objectInputStream.readInt();
                    float normalizedVectorX = objectInputStream.readFloat();
                    float normalizedVectorY = objectInputStream.readFloat();
                    float spellPosXWorld = objectInputStream.readFloat();
                    float spellPosYWorld = objectInputStream.readFloat();
                    double spriteAngle = objectInputStream.readDouble();
                    int spellType = objectInputStream.readInt();
                    Optional<QSpell> optionalSpell01 = QSpell.listOfActiveQSpells.stream().filter(element ->
                            (element.spellCasterClientID == spellCasterClientID) && (element.spellID == spellID)).findFirst();
                    if (optionalSpell01.isPresent()) {
                        optionalSpell01.get().spellPosXWorld = spellPosXWorld;
                        optionalSpell01.get().spellPosYWorld = spellPosYWorld;
                    } else {
                        if (spellCasterClientID != ClientID) {
                            if (spellType == 0)
                                new QSpell(new Spell01DTO(spellPosXWorld, spellPosYWorld, normalizedVectorX,
                                        normalizedVectorY, spellID, spellCasterClientID, spriteAngle));
                            else if (spellType == 1) {
                                new Ultimate(new Spell01DTO(spellPosXWorld, spellPosYWorld, normalizedVectorX,
                                        normalizedVectorY, spellID, spellCasterClientID, spriteAngle));
                            }
                        }
                    }

                }
            }

            objectInputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

}
