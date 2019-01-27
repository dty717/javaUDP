package server;

import analyzePoint.MainAccident;
import fileSave.FileSaving;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

@WebServlet(name = "Servlet",urlPatterns = "/*")
public class Servlet extends HttpServlet {
    HttpURLConnection con;
    FileSaving fileSaving=new FileSaving("C:\\Users\\xqy\\Desktop\\mt");
    MainAccident accident=new MainAccident();

    public static void main(String[] args) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL("http://qximg.lixin202.com/SSCMember/Scripts/Default/utils.js").openConnection();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        con.getInputStream(),"utf-8"));
        String inputLine;
        StringBuffer buffer=new StringBuffer();
        while ((inputLine = in.readLine()) != null){
            buffer.append(inputLine+"\n");
        }
        System.out.println(buffer);
        in.close();
    }
    private boolean zipType;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        accident.check(request.getRequestURL().toString(),request.getHeader("referer"));
        Connection con=Jsoup.connect(request.getRequestURL().toString());
        Enumeration<String> next=request.getHeaderNames();
        //InputStream body = request.getInputStream();
        /*
        Enumeration<String> body=request.getParameterNames();
        while (body.hasMoreElements()){
            String name=body.nextElement();
            con.data(name,request.getParameter(name));
        }
        */
        while (next.hasMoreElements()){
            String name=next.nextElement();
            con.header(name,request.getHeader(name));
        }

        Connection.Response res=null;

        if(request.getHeader("Origin")!=null&&request.getHeader("X-Requested-With")==null){
            res=con.execute();

            for (Map.Entry<String,String> entry:res.headers().entrySet()) {
                if(response.getHeader(entry.getKey())!=null)
                    response.setHeader(entry.getKey(),entry.getValue());
                System.out.println("port1:"+request.getRequestURL());
            }
            response.getOutputStream().write(res.bodyAsBytes());

            return;
        }
        for (Map.Entry<String ,String[]> entry: request.getParameterMap().entrySet()  ) {

            for (String str:entry.getValue()) {
                con.data(entry.getKey(),str);
            }
        }



        if(request.getHeader("Content-Type")==null&&request.getHeader("X-Requested-With")==null){


            try {
                res=con.ignoreContentType(true).execute();

            }catch (Exception e){
                response.sendError(404);
                e.printStackTrace();

                return;
            }

            System.out.println("port2:"+request.getRequestURL());

            fileSaving.staticHtml(request.getRequestURL().toString(),res.bodyAsBytes());

            //System.out.println(res.headers());
            for (Map.Entry<String,String> entry:res.headers().entrySet()) {
               if(entry.getKey().equals("Transfer-Encoding"))
                    continue;
               if(entry.getKey().equals("Content-Encoding"))
                   continue;
                //String val=response.getHeader(entry.getKey());


                response.setHeader(entry.getKey(),entry.getValue());

                //System.out.print(res.headers());
                //}
            }
            if(res.statusCode()==304){
                response.setStatus(res.statusCode());
                return;
            }
            if(res.bodyAsBytes().length<20480*6)
                response.getOutputStream().write(res.bodyAsBytes());
            else {
                response.setHeader("Content-Encoding","gzip");
                // response.setBufferSize(res.bodyAsBytes().length);
                GZIPOutputStream out=new GZIPOutputStream(response.getOutputStream());
                out.write(res.bodyAsBytes());
                out.finish();
            }

            response.getOutputStream().close();
            return;
        }

        boolean utf=false;
        if(request.getHeader("X-Requested-With")!=null&&request.getHeader("X-Requested-With").indexOf("XMLHttpRequest")!=-1){
            if(request.getHeader("Accept").startsWith("text/html"))
                res=con.method(Connection.Method.valueOf(request.getMethod())).execute();
            else
                res=con.method(Connection.Method.valueOf(request.getMethod())).ignoreContentType(true).execute();
            for (Map.Entry<String,String> entry:res.headers().entrySet()) {
                //if(entry.getKey().equals("Transfer-Encoding"))
                //    continue;
                if(entry.getKey().equals("Content-Encoding")){
                    if(entry.getValue().indexOf("zip")!=-1)
                        zipType=true;
                    continue;
                }
                if(entry.getKey().equals("Content-Type")){
                    if(entry.getValue().indexOf("utf-8")!=-1)
                        utf=true;

                }
                if(entry.getKey().indexOf("Length")!=-1)
                    continue;
                if(entry.getKey().equals("Content-Type")){
                    if(entry.getValue().indexOf("utf-8")!=-1){
                        utf=false;
                    }

                }
                response.setHeader(entry.getKey(),entry.getValue());

            }
            if(request.getRequestURL().indexOf("Index")!=-1){
               // System.out.println("port3:"+request.getRequestURL());
               // System.out.println(res.body());

            }
            if(zipType||request.getHeader("Accept-Encoding").indexOf("zip")!=-1){
                response.setHeader("Content-Encoding","gzip");
                GZIPOutputStream out=new GZIPOutputStream(response.getOutputStream());

                out.write(res.bodyAsBytes());
                out.finish();
                System.out.println("port3:"+request.getRequestURL());
                fileSaving.dynamicHtml(request.getRequestURL().toString(),request,res);

            }
            else{
               if(utf)
                   response.setCharacterEncoding("utf-8");
                //response.he
               response.getOutputStream().write(res.bodyAsBytes());
            }
            return;

            //res=con.method(Connection.Method.POST).execute();
        }

        if(res!=null){
            for (Map.Entry<String,String> entry:res.headers().entrySet()) {
                if(response.getHeader(entry.getKey())!=null)
                    response.setHeader(entry.getKey(),entry.getValue());

            }
            response.getOutputStream().write(res.bodyAsBytes());
            System.out.println("port4:"+request.getRequestURL());
        }
        else{


        }
        /*
        if(request.getMethod().equals("POST")) {
            //con.("POST");
       }
        for (Map.Entry<String ,String[]> entry: request.getParameterMap().entrySet()  ) {
            for (String str:entry.getValue()) {
                System.out.println(str+" "+entry.getValue());
                con.data(entry.getKey(),str);
            }
        }
            while (next.hasMoreElements()){
                String name= (String) next.nextElement();
                String val=request.getHeader(name);
                con.header(name,val);
            }
           //con.cookies(request.getCookies());
        //request.getCookies()[0];




            //con.setFixedLengthStreamingMode(con.getContentLength());
      //  }
*/




    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request,response);
    }



}
