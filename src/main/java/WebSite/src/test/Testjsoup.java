package test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Testjsoup {
    public static void main(String[] args) throws IOException {
        Connection  con=Jsoup.connect("http://www.google.com?q=Hello+World");
        Connection.Response res=con.method(Connection.Method.GET).execute();
        System.out.print(res.body());
    }

}
