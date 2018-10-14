package UDP;

//import org.jsoup.Connection;
//import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ClientB {
    private DatagramSocket socket;
    private java.net.InetAddress address;
    private byte[] buf;
    private java.net.DatagramSocket nextSocket;
    private java.net.InetAddress nextAddress;
    private int nextPort;

    public ClientB() {

        try { socket = new java.net.DatagramSocket();
            address = java.net.InetAddress.getByName(InetAddress);
            socket.setSoTimeout(800);
        }
        catch (java.net.SocketException e) {
            e.printStackTrace();
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
        buf=new byte[1024];
    }

    public static void main(String[] args) {
        if(args.length>1) {
            InetAddress = args[0];
            port = Integer.parseInt(args[1]);
        }

        ClientB client = new ClientB();
        for (int i = 0; i < 1000; i++) {
            String msg = client.sendEcho("From b");
            System.out.println(msg);
        }

        client.close();
    }
    static String InetAddress="106.14.118.135";
    static int port=17000;

    public String sendEcho(String msg)
    {
        byte[]buf_send = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf_send, buf_send.length, address, port);
        DatagramPacket pac = new DatagramPacket(buf, buf.length, address, port);

        try
        {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.receive(pac);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String received = new String(pac.getData(),pac.getOffset(),pac.getLength());
        handleReceive(received);
        try {
            if(enableSend)
            while (true) {
                //buf=new byte[1024];
                packet = new DatagramPacket(buf, buf.length, java.net.InetAddress.getByName(AIp),APort);
                socket.send(packet);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(true)
                    break;
                
                socket.receive(packet);
                handleReceiveA(packet);
            }
        } catch (SocketTimeoutException s){
            socket.close();
            return null;
        }catch (IOException e) {
            e.printStackTrace();
        }

        //received = new String(packet.getData(), 0, packet.getLength());
        return received;
    }

    private void handleReceiveA(DatagramPacket pac) {
        System.out.println("receive A:"+new String(pac.getData(),pac.getOffset(),pac.getLength()));
    }

    private boolean enableSend;
    private String AIp;
    private int APort;

    private void handleReceive(String received) {
        int x=received.lastIndexOf("_");
        if(x!=-1&&x<received.length()-1){
            received=received.substring(x+1);
            String[]tem=received.split(":");
            if(tem.length==2){
                enableSend=true;
                AIp=tem[0];
                APort= Integer.parseInt(tem[1]);
                buf="Hi A".getBytes();
                return;
                //System.out.println(AIp+":"+APort);
            }
        }
        enableSend=false;
    }

    public void close() {
        socket.close();
    }
}