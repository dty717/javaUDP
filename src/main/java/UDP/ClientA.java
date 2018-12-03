//package UDP;

import java.io.IOException;
import java.net.*;

public class ClientA {

    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public ClientA() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("192.168.2.101");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ClientA clientA=new ClientA();
        while(true){
            clientA.sendEcho("init");
        }
        
    }


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
        System.out.println(received);
        buf = "BHello World!".getBytes();
        packet
                = new DatagramPacket(buf, buf.length, address, 17000);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return received;
    }

    public void close() {
        socket.close();
    }

}