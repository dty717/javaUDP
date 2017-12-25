//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
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
            this.address = InetAddress.getByName("106.14.118.135");
        } catch (SocketException var2) {
            var2.printStackTrace();
        } catch (UnknownHostException var3) {
            var3.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        String msg = client.sendEcho("ssssssssssssssssssssssssssssssssssssssssssssssa");
        client.close();
       // System.out.println(msg);
    }

    public String sendEcho(String msg) {
        this.buf = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(this.buf, this.buf.length, this.address, 17000);

        try {
            this.socket.send(packet);
        } catch (IOException var10) {
            var10.printStackTrace();
        }

        packet = new DatagramPacket(this.buf, this.buf.length);

        String received;
        try {
            this.socket.receive(packet);

            received = "Hello World!\n";

            for(int i = 0; i < 10; ++i) {
                received = received + i * i;
            }

            this.buf = received.getBytes();

            DatagramPacket pac = new DatagramPacket(this.buf, this.buf.length, packet.getAddress(), packet.getPort());
            this.socket.send(pac);
            Scanner in=new Scanner(System.in);
            int i=0;
            boolean flag=true;
            while(true){
                buf=new byte[1024];
                packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                System.out.println(new String(packet.getData()));
                this.buf=(in.nextLine()).getBytes();
                System.out.println("buf len:"+this.buf.length);
                pac = new DatagramPacket(this.buf, this.buf.length, packet.getAddress(), packet.getPort());
                this.socket.send(pac);
                if(!flag){
                    break;
                }
            }

            this.socket.send(pac);
            pac = new DatagramPacket(this.buf, this.buf.length, packet.getAddress(), packet.getPort());
            this.socket.send(pac);
        } catch (IOException var11) {
            var11.printStackTrace();
        }


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

        received = new String(packet.getData(), 0, packet.getLength());
        return received;
    }

    public void close() {
        this.socket.close();
    }
}
