//package UDP;

import java.io.IOException;
import java.net.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.io.BufferedReader;
import java.util.Arrays;

public class UDPA {

    private DatagramSocket socket;
    private InetAddress address;
    
    private byte[] buf;
    
    private static int test_port=8881;
    private static DatagramSocket test_socket;
    private static InetAddress test_address;

    public UDPA() {
        try {
            socket = new DatagramSocket();
            test_socket = new DatagramSocket();
            address = InetAddress.getByName("127.0.0.1");//106.14.118.135
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
            
          char[]tem=new char[1024];
          int k=reader.read(tem);
          StringBuffer buffer=new StringBuffer();
          while(k==1024){
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
    static String contain[]=new String[10000];
    static String libPath="lib";//C:/Users/xqy/Desktop/github/javaUDP/src/main/java/UDP/
    private static String specialURL(String url){
        if(url.contains("jquery-3.3.1.min.js")){
            return readFile(libPath+"/jquery-3.3.1.min.js");
        }else if(url.contains("src-noconflict")){
            String file= url.substring(url.indexOf("src-noconflict")+"src-noconflict".length());
            return readFile(libPath+file);
        }
        return null;
    }
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

      String content=specialURL(url);
        //System.out.println("url:"+url);

      if(content==null){
        //getRequestMethod()
        //System.out.println();
        if(exchange.getRequestMethod().equals("POST")){
            url+="\r\n"+getBody(exchange.getRequestBody());
        }
        try {
            DatagramPacket test_packet
                = new DatagramPacket(url.getBytes("UTF-8"), url.getBytes("UTF-8").length<400?url.getBytes("UTF-8").length:400, test_address, test_port);
            test_socket.send(test_packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
      String response = UDPa.sendEcho(wrap(url));
      int id=idTask;
      int j=0;
      while(contain[id]==null&&j<10000){
          j++;
      }
      if(j>=1000){
          return;
      }
      content=contain[id];
      contain[id]=null;
      }
      
      exchange.sendResponseHeaders(200, content.getBytes("UTF-8").length);//response code and length
      
      OutputStream os = exchange.getResponseBody();
      os.write(content.getBytes("UTF-8"));
      os.close();}
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
      char keys[]=new char[]{'d','t','y','7','1','7'};//};//
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
  private static String decrypt(byte[]bits){
      char keys[]=new char[]{'d','a','p'};
      int m=0;
      for(int i=0;i<bits.length;i++){
          bits[i]-=(byte)keys[m++];
          if(m==keys.length){
              m=0;
          }
      }
      return new String(bits,StandardCharsets.UTF_8);
      
  }
    static int j=1000;
    static UDPA UDPa=new UDPA();
    public static void main(String[] args) throws IOException{
        if(args.length==1){
            libPath=args[0];
        }
        System.out.println("it's UPDA");
        HttpServer server = HttpServer.create(new InetSocketAddress(8088), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(UDPA::handleRequest);
        server.start();
        
    }
    private byte[]reciveBuffer=new byte[100000000];
    private int reciveBufferIndex;


    public void receive(DatagramPacket packet)throws Exception{
        reciveBufferIndex=0;
        while(true){
            buf=new byte[1024];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            if(packet.getLength()==1){
                continue;
            }
            byte[] tem = Arrays.copyOfRange(packet.getData(),0, packet.getOffset()+packet.getLength()); 
            for(int i=0;i<tem.length;i++){
                reciveBuffer[reciveBufferIndex++]=tem[i];
            }
            if(tem.length!=1024){
                break;
            }
        }
    }
    public void send(byte[]bits)throws IOException{
        DatagramPacket packet;
        int i=0;
        for(;(i+1)*1024<bits.length;i++){
            byte[] copy = Arrays.copyOfRange(buf, i*1024, (i+1)*1024); 
            packet
                = new DatagramPacket(copy, copy.length, address, 17000);
            socket.send(packet);
        }
        
        byte[] copy = Arrays.copyOfRange(bits,i*1024, bits.length); 
        packet
            = new DatagramPacket(copy, copy.length, address, 17000);
        socket.send(packet);
        if(copy.length==1024){
            copy = new byte[]{' '}; 
            packet
                = new DatagramPacket(copy, copy.length, address, 17000);
            socket.send(packet);
        }
        
    }
    private boolean isGetting;
    public String sendEcho(byte[] msg) {
        
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
        String received = new String(reciveBuffer,0,reciveBufferIndex);

        int id=Integer.parseInt(received.substring(1,5));
        byte[] tem=Arrays.copyOfRange(reciveBuffer,5,reciveBufferIndex);

        contain[id]=decrypt(tem);
        return received;
    }
    
    public void close() {
        socket.close();
    }

}
