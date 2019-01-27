package test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Testjsoup {
    public static void main(String[] args) throws IOException {
        Connection  con=Jsoup.connect("https://www.google.com/search?hl=en&ei=6VxMXOWqK56t0PEP_N2fqAo&q=detect+qrcode&oq=detect+qrcode");
        Connection.Response res=con.method(Connection.Method.GET).execute();
        System.out.print(res.body());
    }

}
