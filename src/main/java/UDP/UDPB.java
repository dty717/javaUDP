//package UDP;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public class UDPB {

    public final static int bandWidth=256;

    private DatagramSocket socket;
    private InetAddress address;

    private int test_port=8882;
    private DatagramSocket test_socket;
    private InetAddress test_address;

    private byte[] buf;

    public UDPB() {
        try {
            socket = new DatagramSocket();
            test_socket = new DatagramSocket();
            address = InetAddress.getByName("dty717.com");//127.0.0.1 106.14.118.135
            test_address = InetAddress.getByName("127.0.0.1");
            socket.setSoTimeout(50000);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        System.out.println("it's UPDB");

        UDPB UDPb=new UDPB();
        while(true){
            UDPb.sendEcho("init");
        }
        
    }
    private  byte[] wrap(byte[] url){
      if(j==9999){
          j=1000;
      }else{
          j++;
      }
      byte[]a1=null;
      try {
          a1=("B"+j).getBytes("UTF-8");
      } catch(Exception e) {
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
    
    private  byte[] encrypt(byte[]bits){
      char keys[]=new char[]{'a','d','p'};
      int m=0;
      for(int i=0;i<bits.length;i++){
          bits[i]+=(byte)keys[m++];
          if(m==keys.length){
              m=0;
          }
      }
      return bits;
  }
  private String decrypt(byte[]bits){
       
      char keys[]=new char[]{'d','1','m','1','1','7'};//'d','t','y','7','1','7'};//
      int m=0;
      for(int i=0;i<bits.length;i++){
          bits[i]-=(byte)keys[m++];
          if(m==keys.length){
              m=0;
          }
      }
      return new String(bits, StandardCharsets.UTF_8);
      
  }
     int j=1000;

    public String sendEcho(String msg) {
      try{
        buf = msg.getBytes("UTF-8");
      }catch(Exception e){
      }
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, address, 17000);
        
        try {
            send(buf);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //String received=null;
        while(true){
            
        buf=new byte[10000000];

        packet = new DatagramPacket(buf, buf.length);
        
        try {
            receive(packet);
        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        }
        byte[]received = Arrays.copyOf(reciveBuffer,reciveBufferIndex);
        
        //System.out.println(new String(received));
        
        int id=Integer.parseInt(new String(Arrays.copyOfRange(reciveBuffer,1,5)));
        byte[] tem=Arrays.copyOfRange(reciveBuffer,5,reciveBufferIndex);
        
        
        try{
        buf = wrap(request(decrypt(tem)));
      }catch(Exception e){
      }
        packet
                = new DatagramPacket(buf, buf.length, address, 17000);
        try {
            send(buf);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        }
    }
    private byte[]reciveBuffer=new byte[100000000];
    private int reciveBufferIndex;
    public void receive(DatagramPacket packet)throws IOException{
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
            //test_packet= new DatagramPacket(copy, copy.length<30?buf.length:30, test_address, test_port);
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
    URL url;
    
    public byte[] request(String urlStr){
        try{
        String []urls=urlStr.split("\r\n",2);
        //String _url= java.net.URLDecoder.decode(urlStr, "GBK");
        url = new URL("http://127.0.0.1:8080"+urls[0]);
        
        try {
            DatagramPacket test_packet
                = new DatagramPacket(url.toString().getBytes("UTF-8"), url.toString().getBytes("UTF-8").length<400?url.toString().getBytes("UTF-8").length:400, test_address, test_port);
            test_socket.send(test_packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection =(HttpURLConnection)url.openConnection();
        if(urls.length>1){
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "ISO-8859-1");
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os,"ISO-8859-1");   
            
            /*
            String urlParameters  = "param1=a&param2=b&param3=c";
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            String request        = "http://example.com/index.php";
            URL    url            = new URL( request );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
               wr.write( postData );
            }
            */
            for(int i=1;i<urls.length;i++){
                osw.write(urls[i]);
                //System.out.println(URLDecoder.decode(urls[i], "UTF-8"));
                if(i!=urls.length-1){
                   osw.write("\r\n"); 
                }
            }
            osw.flush();
            osw.close();
            os.close();  //don't forget to close the OutputStream
        }else{
            connection.setRequestProperty("Accept-Charset", "UTF-8");
        }
        InputStream  reader = connection.getInputStream();
          //StringBuffer buffer=new StringBuffer();
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] chunk = new byte[4096];
            int bytesRead;

            while ((bytesRead = reader.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }
            reader.close();
        /*if(urls[0].indexOf("monitor")!=-1){
            System.out.println(urls[0]);
            System.out.println(outputStream.toByteArray().length);
            FileOutputStream  writer=new FileOutputStream ("C:/Users/xqy/Desktop/github/javaUDP/src/main/java/UDP/test.jpg");
            writer.write(outputStream.toByteArray());
            writer.close();
            return "error".getBytes();
        }*/
            return outputStream.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
            return "error".getBytes();            
        }
    }
    public void close() {
        socket.close();
    }

}