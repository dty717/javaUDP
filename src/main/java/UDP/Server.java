package UDP;

import java.io.IOException;
import java.net.*;

public class Server extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public Server() {
        try {
            socket = new DatagramSocket(17000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void run() {
        running = true;
        while (running) {
            buf=new byte[1024];
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handleRecive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            packet = new DatagramPacket(send.getBytes(), send.getBytes().length, address, port);
            if (receive.equals("end")) {
                running = false;
                continue;
            }

            try {
                if(!receive.equals("A"))
                    socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }

    private String send;
    private String before;
    String receive;

    void handleRecive(DatagramPacket pac) {

        receive = new String(pac.getData(),pac.getOffset(),pac.getLength());
        if (receive.equals("A")) {
            buf = ("___________"+pac.getAddress().getHostName() + ":" + pac.getPort()).getBytes();
            send="___________"+pac.getAddress().getHostName() + ":" + pac.getPort();

        } else {
            before = pac.getAddress().getHostName() + ":" + pac.getPort();
        }
        System.out.println(pac.getAddress().getHostName() + ":" + pac.getPort());

    }
}
