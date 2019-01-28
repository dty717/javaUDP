package test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Testjsoup {
    public static void main(String[] args) throws IOException {
        Connection  con=Jsoup.connect("http://192.168.2.100");
        Connection.Response res=con.method(Connection.Method.GET).execute();
        System.out.print(res.body());
    }

}
