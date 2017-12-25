package UDP;

import java.io.IOException;
import java.net.*;

public class Server  extends Thread {

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
        Server server=new Server();
        server.start();
    }
        public void run() {
            running = true;

            while (running) {
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received
                        = new String(packet.getData(), 0, packet.getLength());
                System.out.println(address+"\n"+port+"\n"+received);
                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.close();
        }
    }