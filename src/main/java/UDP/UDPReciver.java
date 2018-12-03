
import java.io.IOException;
import java.net.*;
import java.io.OutputStream;
import java.io.FileReader;

import java.util.Arrays;

public class UDPReciver extends Thread {
    
    /*!!!
    public int testPortA=8881;
    public int testPortB=8882;
    public int testPortServer=8883;
    public int testRequest=8809;
    */
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    InetAddress address;
    
    public UDPReciver() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    private final Object lock = new Object();
    int index=0;
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
            synchronized (lock) {
                handleRecive(packet);
                index++;
            }
            System.out.println("??")
        }
        socket.close();
    }
    byte[]recBytes=new byte[100000000];
    int recBytesIndex=0;
    void handleRecive(DatagramPacket pac) {
        byte[]bits=Arrays.copyOfRange(pac.getData(),pac.getOffset(),pac.getOffset()+pac.getLength());
        for(int i=0;i<bits.length;i++){
            recBytes[i+index*1024]=bits[i];
        }
        if(bits.length!=1024){
            running=false;
            recBytesIndex=index*1024+bits.length;
        }
    }
}    
