package udp;

import java.io.IOException;
import java.net.*;

public class Client {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public Client() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName("106.14.118.135");

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client=new Client();
        String msg=client.sendEcho("ssssssssssssssssssssssssssssssssssssssssssssssa");
        client.close();
        System.out.println(msg);
    }
    private DatagramSocket nextSocket;
    private InetAddress nextAddress;
    private int nextPort;
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
        System.out.println(packet.getAddress()+"\n"+packet.getPort());
        try {
            nextSocket=new DatagramSocket();
            nextAddress = packet.getAddress();
            nextPort=packet.getPort();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
        /*
        buf = "qwer".getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, nextAddress, nextPort);
        try {
            nextSocket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        packet = new DatagramPacket(buf, buf.length);
        try {
            nextSocket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}