package socket.service.client;

import java.io.*;
import java.net.Socket;

public class File {
    public static void main(String[] args) throws IOException {
        //客户端请求与本机在20006端口建立TCP连接
        Socket client = new Socket("106.14.118.135", 80);
        client.setSoTimeout(10000);

        //获取Socket的输入流，用来接收从服务端发送过来的数据
        BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
        boolean flag = true;
        DataInputStream inFile=new DataInputStream(new FileInputStream("C:\\Users\\xqy\\Desktop\\名侦探柯南 - Time after time（名侦探柯南主题曲）.mp3"));
        int k;
        DataOutputStream out=new DataOutputStream(client.getOutputStream());
        while ((k=inFile.read())!=-1){
            out.write(k);
        }
    }
}