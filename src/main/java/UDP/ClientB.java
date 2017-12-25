package UDP;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.DatagramPacket;
import java.util.Arrays;

public class ClientB {
    private java.net.DatagramSocket socket;
    private java.net.InetAddress address;
    private byte[] buf;
    private java.net.DatagramSocket nextSocket;
    private java.net.InetAddress nextAddress;
    private int nextPort;

    public ClientB() {
        try { socket = new java.net.DatagramSocket();
            address = java.net.InetAddress.getByName(InetAddress);
        }
        catch (java.net.SocketException e) {
            e.printStackTrace();
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if(args.length>1) {

            InetAddress = args[0];
            port = Integer.parseInt(args[1]);
        }else {
            InetAddress = "180.102.218.126";
            port = Integer.parseInt("7104");
        }
        ClientB client = new ClientB();
        String msg = client.sendEcho("From b");
        client.close();
        System.out.println(msg);
    }
    static String InetAddress;
    static int port;

    public String sendEcho(String msg)
    {
        buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try
        {
            socket.send(packet);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        System.out.println(packet.getAddress() + "\n" + packet.getPort());
        try {
            while (true) {
                buf=new byte[1024];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String received = new String(packet.getData());
                System.out.println("debug:dty "+received);
                if(received.startsWith("http")){
                    Connection con= Jsoup.connect(received);
                    buf = (con.get().head().toString()).getBytes();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else
                    buf=("OK").getBytes();
                if(buf.length>2048){
                    buf= Arrays.copyOf(buf,200);
                }
                System.out.println(new String(buf));

                packet = new DatagramPacket(buf, buf.length, address, port);

                socket.send(packet);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        try {
            nextSocket = new java.net.DatagramSocket();
            nextAddress = packet.getAddress();
            nextPort = packet.getPort();
        } catch (java.net.SocketException e) {
            e.printStackTrace();
        }


        String received = new String(packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        socket.close();
    }
}