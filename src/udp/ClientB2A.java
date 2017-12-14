package udp;

import java.io.IOException;
import java.net.*;

public class ClientB2A {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;
    String a="222.190.123.154";
    String b="117.88.153.22";
    static int ports;
    public ClientB2A(String as) {
        try {
            if(ports!=0)
                socket=new DatagramSocket(ports);
            else
                socket = new DatagramSocket();
            address = InetAddress.getByName(as);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String a="222.190.123.154";
        String b="117.88.153.22";
        String IPaddress=a;
        int port=56876;

        if(args.length==2){
            IPaddress=args[0];
            port=Integer.parseInt(args[1]);
        }else if(args.length==3){
            IPaddress=args[0];
            port=Integer.parseInt(args[1]);
            ports=Integer.parseInt(args[2]);
        }

        ClientB2A client=new ClientB2A(IPaddress);
        String msg=client.sendEcho("Hello World",port);
    }

    public String sendEcho(String msg,int port) {

        buf = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, port);
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
}