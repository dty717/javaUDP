
import java.io.IOException;
import java.net.*;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Arrays;

public class Test extends Thread {
    
    /*!!!
    public int testPortA=8881;
    public int testPortB=8882;
    public int testPortServer=8883;
    public int testRequest=8809;
    */
    private String name;
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];
    public int port=8881;


    public Test(String name,int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.name=name;
        this.port=port;
    }
    static String readFile(String path) 
    {
      try{
          //byte[] encoded = Files.readAllBytes(Paths.get(path));
          Charset charset = Charset.forName("UTF8");
          InputStreamReader reader = new InputStreamReader(new FIleInputStream(path), charset);
            
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
          System.out.println(buffer.toString());
          return buffer.toString();
      }catch(IOException e){
          e.printStackTrace();

            return null;      
      }
    }
    public static void main(String[]args){
        
        try{
            System.out.println(readFile("lib/index.html"));;
            System.exit(1);
            //String _url= java.net.URLDecoder.decode(urlStr, "GBK");
            URL url = new URL("http://127.0.0.1:8080");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            Charset charset = Charset.forName("UTF8");
            InputStreamReader reader = new InputStreamReader(connection.getInputStream(), charset);
            char[]data=new char[1024];
            int k=reader.read(data);
            while(k==1024){
                //System.out.println(k);
                System.out.print(new String(data));
                k=reader.read(data);
            }
            if(k!=-1){
                System.out.println(new String(Arrays.copyOf(data,k)));                
            }
            System.out.println("Hello World!-3");
            
        }catch(Exception e){
            e.printStackTrace();
        }
                
        /*
        int testPortA=8881;
        int testPortB=8882;
        int testPortServer=8883;
        Test testA=new Test("A",testPortA);
        Test testB=new Test("B",testPortB);
        Test testServer=new Test("Server",testPortServer);
        testA.start();
        testB.start();
        testServer.start();
        */
        
    }
    public void run() {
        running = true;
        while (running) {
            buf=new byte[10000000];
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            handleRecive(packet);
        }
        socket.close();
    }
    void handleRecive(DatagramPacket pac) {
        byte[]bits=Arrays.copyOfRange(pac.getData(),pac.getOffset(),pac.getOffset()+pac.getLength());
        if(name.equals("A")){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("from "+name+":");
        if(bits.length>400){
            System.out.println("...(400+)");
            return;
        }
        for(int i=0;i<bits.length;i++){
            System.out.print(bits[i]+"\t");
        }
        System.out.println();
        //System.out.println(pac.getAddress().getHostName() + ":" + pac.getPort());
    }
}    
