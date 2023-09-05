package networking;

import java.io.IOException;
import java.net.*;

public class Server extends Thread{


    private DatagramSocket serverSocket;
    private InetAddress ipAddress;

    //    dane podłączonego klienta
    private InetAddress clientIpAddress;
    private int port;

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
            receiveData();

        }
    }

    private void receiveData() {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            serverSocket.receive(packet);
            port = packet.getPort();
            clientIpAddress = packet.getAddress();
            System.out.println("Data received");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendData() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length, clientIpAddress, port);
        try {
            serverSocket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }
}
