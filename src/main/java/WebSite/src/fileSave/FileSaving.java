package fileSave;

import analyzePoint.MainAccident;
import org.jsoup.Connection;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class FileSaving {
    String filePath;
    public FileSaving(String filePath){
        this.filePath=filePath;
    }

    public void accidentSave(String str,String name){
        FileOutputStream writer= null;
        File file=null;
        try {
            file=new File(filePath+"/accident/"+name);
            if(!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            createDirs(file);
        }
        try {
            writer = new FileOutputStream(file);
            writer.write(str.getBytes());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void createDirs(File file){
        File parent=file.getParentFile();
        if (!parent.exists()){
            parent.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void staticHtml(String urlStr,byte[]bytes){
        //accident.check(urlStr);

        urlStr=urlStr.substring(urlStr.indexOf("://")+2);
        urlStr=urlStr.replace(":","_");
        if(urlStr.endsWith(".com")||urlStr.endsWith(".us")||urlStr.endsWith(".cn")){
            urlStr=urlStr+"/index.html";
        }
        if(urlStr.endsWith("/")){
           urlStr=urlStr+"index.html";
        }
        File file=new File(filePath+"/"+urlStr);

        try {
            if(file.exists())
                return;
            file.createNewFile();
        } catch (IOException e) {
            createDirs(file);
        }
        try {
            FileOutputStream writer=new FileOutputStream(file);
            writer.write(bytes);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    List<Long>times=new ArrayList<>();
    public void dynamicHtml(String urlStr, HttpServletRequest request, Connection.Response response){
        //accident.check(request,urlStr);
        urlStr=urlStr.substring(urlStr.indexOf("://")+2);
        urlStr=urlStr.replace(":","_");

        File file=new File(filePath+"/link/"+urlStr);
        try {
            if(!file.exists())
                file.createNewFile();
        } catch (IOException e) {
            createDirs(file);
        }
        try {
            FileOutputStream writer=new FileOutputStream(file,false);
            //writer.write(("request: url "+request.getRequestURL()+"\n\t").getBytes());
            Enumeration<String> next=request.getHeaderNames();

            while (next.hasMoreElements()){
                String name=next.nextElement();
                //writer.write((name+":"+request.getHeader(name)+"\n").getBytes());
            }
            writer.write("Data:\n".getBytes());
            for (Map.Entry req:request.getParameterMap().entrySet()) {
                String str="";
                String[] tem=(String[])req.getValue();
                for (int i = 0; i < tem.length-1; i++) {
                    str+=tem[i]+",";
                }
                if(tem.length>0){
                    str+=tem[0];
                }
                writer.write(("\t"+req.getKey()+":"+str+"\n").getBytes());
            }

            writer.write("\n\n\nresponse:".getBytes());

            for (Map.Entry<String,String> entry:response.headers().entrySet()) {
                //writer.write(("\t"+entry.getKey()+":"+entry.getValue()+"\n").getBytes());
            }
            writer.write(response.bodyAsBytes());
            writer.write("\n\n\n".getBytes());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
