package socket.web.server;

import com.sun.org.apache.regexp.internal.RE;

import java.net.ServerSocket;
import java.net.Socket;

public class socket {
    public static void main(String[] args) throws Exception{
        //服务端在20006端口监听客户端请求的TCP连接
        ServerSocket server = new ServerSocket(18000);
        Socket client = null;
        boolean f = true;
        while(f){
            //等待客户端的连接，如果没有获取连接
            client = server.accept();
            System.out.println("与客户端连接成功！");
            String ReAddr=client.getRemoteSocketAddress().toString();
            System.out.print(client.getInetAddress()+ReAddr);
            client.getOutputStream().write(ReAddr.getBytes());
            client.getOutputStream().close();
            Thread.sleep(30000);
            Socket socket=new Socket(ReAddr.substring(1).split(":")[0],Integer.parseInt(ReAddr.split(":")[1]));
            socket.getOutputStream().write("Hello World".getBytes());
            socket.getOutputStream().close();
            socket.close();
            client.close();
            //为每个客户端连接开启一个线程
//            new Thread(new thread(client)).start();
        }
        server.close();
    }
}