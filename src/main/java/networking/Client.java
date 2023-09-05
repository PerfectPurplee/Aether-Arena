package networking;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client extends Thread {


   private DatagramSocket socket;
   private InetAddress serverIPaddress;
    public Client() {

        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void run() {

    }
}
