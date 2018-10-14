package UDP;

import java.io.IOException;
import java.net.*;

public class ClientA {
    /*
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public ClientA() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("106.14.118.135");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    */

    /*
    public String sendEcho(String msg) {
        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 17000);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
    */
}