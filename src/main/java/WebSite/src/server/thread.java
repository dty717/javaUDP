package server;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * 该类为多线程类，用于服务端
 */
public class thread implements Runnable {

    private Socket client = null;
    public thread(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        try{
            //获取Socket的输出流，用来向客户端发送数据
            PrintStream out=new PrintStream(client.getOutputStream());
            //获取Socket的输入流，用来接收从客户端发送过来的数据
            DataInputStream in=new DataInputStream(client.getInputStream());

            int k;
            while((k=in.read())!=-1){
                System.out.print((char)k);
            }
            out.println("传输成功");
            in.close();
//            outfile.close();
//            while(flag){
//                //接收从客户端发送过来的数据
//                String str =  buf.readLine();
//                if(str == null || "".equals(str)){
//                    flag = false;
//                }else{
//                    if("bye".equals(str)){
//                        flag = false;
//                    }else{
//                        //将接收到的字符串前面加上echo，发送到对应的客户端
//                        out.println("echo:" + str+Math.random());
//                    }
//                }
//            }
            out.close();
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}