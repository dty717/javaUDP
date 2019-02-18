package UDPConnection;

import java.io.IOException;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

public class GoogleApi extends Thread{
    public final static int bandWidth=64;

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[64];
    
    public GoogleApi() {
        try {
            socket = new DatagramSocket(17000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("it's GoogleApi");
        //System.setProperty("https.proxyHost","127.0.0.1");
        //System.setProperty("https.proxyPort","1080");
        GoogleApi googleApi = new GoogleApi();
        googleApi.start();
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
            
            byte[]transmit=returnBuffer;
            //Arrays.copyOfRange(packet.getData(),0,packet.getOffset()+packet.getLength());
            //System.out.println("all:"+transmit.length);
            
            packet = new DatagramPacket(transmit, transmit.length, addressA, portA);
            try {
                send(transmit,addressA, portA);
            } catch (IOException e) {
                e.printStackTrace();
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
    public void send(byte[]bits,InetAddress address,int port )throws IOException{
        System.out.println(bits.length);
        DatagramPacket packet;
        int i=0;
        for(;(i+1)*bandWidth<bits.length;i++){
            byte[] copy = Arrays.copyOfRange(buf, i*bandWidth, (i+1)*bandWidth); 
            packet
                = new DatagramPacket(copy, copy.length, address, port);
            socket.send(packet);
            System.out.println(new String(copy));
        }
        byte[] copy = Arrays.copyOfRange(bits,i*bandWidth, bits.length); 
        packet
            = new DatagramPacket(copy, copy.length, address, 17000);
        socket.send(packet);
        System.out.println(i+"\nend");
    }
    private byte[]reciveBuffer=new byte[10000000];
    private byte[]returnBuffer=new byte[10000000];
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
        receive = new String(reciveBuffer,0,reciveBufferIndex);
        addressA= pac.getAddress();
        portA = pac.getPort();
        try {
            returnBuffer=getResultAmount(receive).getBytes();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static String getResultAmount(String query) throws IOException {
        URLConnection connection = new URL("https://www.google.com/search?q=" + query).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.connect();

        BufferedReader r  = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            sb.append(line);
        }
        //Pattern regex = Pattern.compile("<!--a-->.*<!--z-->");
        //Matcher matcher = regex.matcher(sb.toString());
        int start=sb.toString().indexOf("<!--a-->");
        int end=sb.toString().indexOf("<!--z-->");
        String link = "error";
        if(start!=-1&&end!=-1)
            link=sb.toString().substring(start+8,end);
        return link;
    }
}