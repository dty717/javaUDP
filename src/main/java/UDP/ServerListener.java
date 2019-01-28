
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ServerListener extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    private int test_port=8883;
    private DatagramSocket test_socket;
    
    public ServerListener() {
        try {
            socket = new DatagramSocket(17000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("it's ServerListener");
        ServerListener serverListener = new ServerListener();
        serverListener.start();
    }
    boolean init;
    public void run() {
        running = true;
        while (running) {

            buf=new byte[10000000];
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            handleRecive(packet);
            
            byte[]transmit=Arrays.copyOfRange(packet.getData(),0,packet.getOffset()+packet.getLength());
           //System.out.println("all:"+transmit.length);
            
            if(targetA){
                DatagramPacket test_packet
                    = new DatagramPacket(transmit, transmit.length<400?transmit.length:400, addressA, test_port);
                    
                packet = new DatagramPacket(transmit, transmit.length, addressA, portA);
                try {
                    send(transmit,addressA, portA);
                    System.out.println("send A :"+transmit.length);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(targetB){
                DatagramPacket test_packet
                    = new DatagramPacket(transmit, transmit.length<400?transmit.length:400, addressB, test_port);   
                packet = new DatagramPacket(transmit, transmit.length, addressB, portB);
                try {
                    send(transmit,addressB, portB);
                    System.out.println("send B:"+transmit.length);

                    //test_socket.send(test_packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
                
            
        }
        socket.close();
    }
    public void receive(DatagramPacket packet)throws IOException{
        reciveBufferIndex=0;
        
        socket.receive(packet);
        byte[] tem = Arrays.copyOfRange(packet.getData(),packet.getOffset(), packet.getOffset()+packet.getLength()); 
        for(int i=0;i<tem.length;i++){
            reciveBuffer[reciveBufferIndex++]=tem[i];
        }
        
        
    }
    public final static bandWidth=512;
    public void send(byte[]bits,InetAddress address,int port )throws IOException{
        if(bits.length!=bandWidth){
            isTransmit=false;
            System.out.println("len:"+bits.length);
            
        }
        
        DatagramPacket packet;
        int i=0;
        for(;(i+1)*bandWidth<bits.length;i++){
            byte[] copy = Arrays.copyOfRange(buf, i*bandWidth, (i+1)*bandWidth); 
            packet
                = new DatagramPacket(copy, copy.length, address, port);
            socket.send(packet);
        }
        
        byte[] copy = Arrays.copyOfRange(bits,i*bandWidth, bits.length); 
        packet
            = new DatagramPacket(copy, copy.length, address, port);
        socket.send(packet);
        if(copy.length==bandWidth){
            copy = new byte[]{' '}; 
            packet
                = new DatagramPacket(copy, copy.length, address, port);
            socket.send(packet);
        }
        
    }
    private byte[]reciveBuffer=new byte[10000000];
    private int reciveBufferIndex;
    private String send;
    private String before;
    String receive;
    InetAddress addressA ;
    int portA ;
    InetAddress addressB ;
    int portB ;
    boolean targetA;
    boolean targetB;
    boolean isTransmit;
    void handleRecive(DatagramPacket pac) {
        if(isTransmit){
            return;
        }
        receive = new String(reciveBuffer,0,reciveBufferIndex);
        //System.out.println(receive);
        if(receive.startsWith("A")){
            addressA= pac.getAddress();
            portA = pac.getPort();
            targetA=false;
            targetB=true;
            System.out.println("get from A");
            isTransmit=true;
        }else if(receive.startsWith("B")){
            addressB= pac.getAddress();
            portB = pac.getPort();
            targetB=false;
            targetA=true;
            System.out.println("get from B");
            isTransmit=true;
        }else{
            targetA=false;
            targetB=false;
            addressB= pac.getAddress();
            portB = pac.getPort();
            System.out.println("get init");
        }
        //System.out.println(pac.getAddress().getHostName() + ":" + pac.getPort());

    }
}
