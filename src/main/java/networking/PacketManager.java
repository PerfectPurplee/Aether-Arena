package networking;

import entities.playercharacters.LocalPlayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;

import static main.EnumContainer.ServerClientConnectionCopyObjects.PLayer_Champion_Shared;
import static main.EnumContainer.ServerClientConnectionCopyObjects.currentMousePosition;

public abstract class PacketManager {

    public static DatagramPacket LoginPacket(LocalPlayer localPlayer) throws IOException {

        final int packetType = 0;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        PLayer_Champion_Shared = localPlayer.localPlayerChampion;

        try {
            objectOutputStream.writeInt(packetType);
            objectOutputStream.writeObject(PLayer_Champion_Shared);
            objectOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, Client.serverIPaddress, 1337);


        try {
            byteArrayOutputStream.close();
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return datagramPacket;
    }

    public static DatagramPacket movementRequestPacket(int mouseClickXPos, int mouseClickYPos, int clientID) throws IOException {

        final int packetType = 1;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream dataOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeInt(packetType);
            dataOutputStream.writeInt(clientID);
            dataOutputStream.writeInt(mouseClickXPos);
            dataOutputStream.writeInt(mouseClickYPos);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, Client.serverIPaddress, 1337);


        try {
            byteArrayOutputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return datagramPacket;
    }

    public static DatagramPacket spellRequestPacket(
            boolean shouldCreateSpellQ, boolean shouldCreateSpellW,
            boolean shouldCreateSpellE, boolean shouldCreateSpellR) throws IOException {

        final int packetType = 2;
        final int clinetID = Client.ClientID;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream dataOutputStream = new ObjectOutputStream(byteArrayOutputStream);


        try {
            dataOutputStream.writeInt(packetType);
            dataOutputStream.writeInt(clinetID);
            dataOutputStream.writeBoolean(shouldCreateSpellQ);
            dataOutputStream.writeBoolean(shouldCreateSpellW);
            dataOutputStream.writeBoolean(shouldCreateSpellE);
            dataOutputStream.writeBoolean(shouldCreateSpellR);
            dataOutputStream.writeObject(currentMousePosition);
            dataOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, Client.serverIPaddress, 1337);


        try {
            byteArrayOutputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return datagramPacket;
    }

    public static DatagramPacket spellRequestPacket(char spellName, int spellID) throws IOException {

        final int packetType = 2;
        final int clinetID = Client.ClientID;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream dataOutputStream = new ObjectOutputStream(byteArrayOutputStream);


        try {
            dataOutputStream.writeInt(packetType);
            dataOutputStream.writeInt(clinetID);
            dataOutputStream.writeChar(spellName);
            dataOutputStream.writeInt(spellID);
            dataOutputStream.writeObject(currentMousePosition);
            dataOutputStream.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(data, data.length, Client.serverIPaddress, 1337);


        try {
            byteArrayOutputStream.close();
            dataOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return datagramPacket;
    }


}
