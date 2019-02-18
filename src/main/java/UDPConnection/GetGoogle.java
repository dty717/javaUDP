package UDPConnection;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.io.FileInputStream;
import java.io.OutputStream;

public class GetGoogle extends Thread{

    public final static int bandWidth=64;

    private DatagramSocket socket;
    private InetAddress address;
    
    private byte[] buf;
    
    private static int test_port=8881;
    private static DatagramSocket test_socket;
    private static InetAddress test_address;

    public GetGoogle() {
        try {
            socket = new DatagramSocket();
            test_socket = new DatagramSocket();
            address = InetAddress.getByName("138.128.199.177");//106.14.118.135 dty717.com
            test_address = InetAddress.getByName("127.0.0.1");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    static String readFile(String path) 
    {
      try{
          //byte[] encoded = Files.readAllBytes(Paths.get(path));
          Charset charset = Charset.forName("UTF8");
          InputStreamReader reader = new InputStreamReader(new FileInputStream(path), charset);
            
          char[]tem=new char[bandWidth];
          int k=reader.read(tem);
          StringBuffer buffer=new StringBuffer();
          while(k==bandWidth){
              buffer.append(String.valueOf(tem));
              k=reader.read(tem);
          }
          if(k>0){
              buffer.append(String.valueOf(Arrays.copyOf(tem,k)));
          }
          reader.close();
          //System.out.println(buffer.toString());
          return buffer.toString();
      }catch(IOException e){
          e.printStackTrace();

            return null;      
      }
    }
    static byte [][]contain=new byte[10000][];
    static String libPath="lib";//C:/Users/xqy/Desktop/github/javaUDP/src/main/java/UDP/
    public static String getBody(InputStream inputStream) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        
        try {
            if (inputStream != null) {
                Charset charset = Charset.forName("UTF8");
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream,charset));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
        return body;
    }
    private static void handleRequest(HttpExchange exchange)  {
        try{
            URI requestURI = exchange.getRequestURI();
            String url = requestURI.toString();//URLDecoder.decode(,"UTF-8");
            //     if(url.length()<30)
            //     else{
            //       System.out.println(url.substring(0,30));
            //     }
            String content=url.replace("/","");
            byte[]send_content;
            if(content!=null){
                //getRequestMethod()
                //System.out.println();
                System.out.println(content);
                send_content=getGoogle.sendEcho(url.getBytes());
                exchange.sendResponseHeaders(200, send_content.length);//response code and length
              
                OutputStream os = exchange.getResponseBody();
                os.write(send_content);
                os.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static byte[] wrap(String url){
        if(j==9999){
            j=1000;
        }else{
            j++;
        }
        idTask=j;
        byte[]a1=null;
        try{
            a1=("A"+j).getBytes("UTF-8");
        }catch(Exception e){
        }
        byte[]a2=encrypt(url);
        byte[]all=new byte[a1.length+a2.length];
        for(int i=0;i<a1.length;i++){
            all[i]=a1[i];
        }
        for(int i=a1.length;i<a2.length+a1.length;i++){
            all[i]=a2[i-a1.length];
        }
        
        return all;
    }
    static int idTask;
    
    private static byte[] encrypt(String url){
        byte[]bits=null;
        try{
          bits=url.getBytes("UTF-8");   
        }catch(Exception e){
        }      
        char keys[]=new char[]{'d','1','m','1','1','7'};//};//
        int m=0;
        for(int i=0;i<bits.length;i++){
            bits[i]+=(byte)keys[m++];
            if(m==keys.length){
                m=0;
            }
        }
        return bits;
    }
    
    // a (d*) (1+)
    private static byte[] decrypt(byte[]bits){
        char keys[]=new char[]{'a','d','p'};
        int m=0;
        for(int i=0;i<bits.length;i++){
            bits[i]-=(byte)keys[m++];
            if(m==keys.length){
                m=0;
            }
        }
        return bits;
        
    }
    static int j=1000;
    static GetGoogle getGoogle=new GetGoogle();
    public static void main(String[] args) throws IOException{
        if(args.length==1){
            libPath=args[0];
        }
        System.out.println("it's UPDA");
        HttpServer server = HttpServer.create(new InetSocketAddress(8088), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(GetGoogle::handleRequest);
        server.start();
        
    }
    private byte[]reciveBuffer=new byte[100000000];
    private int reciveBufferIndex;


    public void receive(DatagramPacket packet)throws Exception{
        reciveBufferIndex=0;
        while(true){
            buf=new byte[bandWidth];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            if(packet.getLength()==1){
                continue;
            }
            byte[] tem = Arrays.copyOfRange(packet.getData(),0, packet.getOffset()+packet.getLength()); 
            for(int i=0;i<tem.length;i++){
                reciveBuffer[reciveBufferIndex++]=tem[i];
            }
            if(tem.length!=bandWidth){
                break;
            }
        }
    }
    public void send(byte[]bits)throws IOException{
        DatagramPacket packet;
        int i=0;
        for(;(i+1)*bandWidth<bits.length;i++){
            byte[] copy = Arrays.copyOfRange(buf, i*bandWidth, (i+1)*bandWidth); 
            packet
                = new DatagramPacket(copy, copy.length, address, 17000);
            socket.send(packet);
        }
        
        byte[] copy = Arrays.copyOfRange(bits,i*bandWidth, bits.length); 
        packet
            = new DatagramPacket(copy, copy.length, address, 17000);
        socket.send(packet);
        if(copy.length==bandWidth){
            copy = new byte[]{' '}; 
            packet
                = new DatagramPacket(copy, copy.length, address, 17000);
            socket.send(packet);
        }
        
    }
    private boolean isGetting;
    public byte[] sendEcho(byte[] msg) {
        
        buf = msg;

        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 17000);
        try {
            send(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            receive(packet);
            //socket.receive(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(new String(reciveBuffer,0,reciveBufferIndex));
        return Arrays.copyOfRange(reciveBuffer,0,reciveBufferIndex);
    }
    
    public void close() {
        socket.close();
    }

}