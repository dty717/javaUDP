//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package UDP;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] buf;
    private DatagramSocket nextSocket;
    private InetAddress nextAddress;
    private int nextPort;

    public Client() {
        try {
            this.socket = new DatagramSocket();
            this.socket.setSoTimeout(5000);
            this.address = InetAddress.getByName("106.14.118.135");
        } catch (SocketException var2) {
            var2.printStackTrace();
        } catch (UnknownHostException var3) {
            var3.printStackTrace();
        }

    }
    public static void main(String[] args) {
        Client client=new Client();
        while(true){
            String s_return=client.sendEcho("A");
            if(s_return==null)
                client=new Client();

        }


    }

    /*
        public static void main(String[] args) {
            Client client = new Client();
            String msg = client.sendEcho("ssssssssssssssssssssssssssssssssssssssssssssssa");
            client.close();
           // System.out.println(msg);
        }
    */
    String received = "";
    boolean flag_continue = true;

    public String sendEcho(String msg) {
        this.buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(this.buf, this.buf.length, this.address, 17000);
        DatagramPacket pac = new DatagramPacket(this.buf, this.buf.length);

        try {
            for (int i = 0; i < 40; i++) {
                this.socket.send(packet);
                if(i>10){
                    System.out.println("waiting for response");
                    this.buf=new byte[1024];
                    pac = new DatagramPacket(this.buf, this.buf.length);
                    socket.receive(pac);
                    System.err.println(1);
                    break;
                }else
                    Thread.sleep(300);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("end waiting");
            socket.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        handleReceive(pac);
        if (flag_continue)
            while (true) {
                //buf=(received).getBytes();
                handleReceive(packet);
                pac = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
                try {
                    this.socket.send(pac);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //handleSend(packet.getData());
                packet = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(packet);
                    if (!flag_continue) {
                        break;
                    }
                } catch (SocketTimeoutException e) {
                    socket.close();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            /*
            this.socket.send(pac);
            pac = new DatagramPacket(this.buf, this.buf.length, packet.getAddress(), packet.getPort());
            this.socket.send(pac);
            */



        /*
        try {
            this.nextSocket = new DatagramSocket();
            this.nextAddress = packet.getAddress();
            this.nextPort = packet.getPort();
        } catch (SocketException var9) {
            var9.printStackTrace();
        }

        this.socket.close();

        try {
            Thread.sleep(1000L);
            System.out.print("new \n");
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(this.nextAddress.getHostAddress());
            this.buf = "from A".getBytes();
            DatagramPacket pac = new DatagramPacket(this.buf, this.buf.length, this.address, this.nextPort);
            this.socket.send(pac);
            Thread.sleep(100000L);
        } catch (SocketException var5) {
            var5.printStackTrace();
        } catch (UnknownHostException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        } catch (InterruptedException var8) {
            var8.printStackTrace();
        }

        received = new String(packet.getData(), 0, packet.getLength());*/
        return received;
    }

    private void handleReceive(DatagramPacket packet) {
        byte[]rec=packet.getData();
        if (rec[0] == 1 && rec[1] == 1) {
            flag_continue = true;
        } else
            flag_continue = false;
        received = new String(rec,packet.getOffset(),packet.getLength());
        System.out.println("From "+packet.getAddress().getHostName()+": "+received);
        buf = received.getBytes();
    }



    public void close() {
        this.socket.close();
    }
}
