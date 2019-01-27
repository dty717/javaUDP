package client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

public class Port18000 {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket client = new Socket("106.14.118.135", 18000);
        client.setSoTimeout(10000);
        int k;
        int i=0;
        Thread.sleep(1000000);

        client.close();
    }
}
