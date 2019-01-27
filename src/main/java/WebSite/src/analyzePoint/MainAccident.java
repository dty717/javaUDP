package analyzePoint;

import fileSave.FileSaving;

import java.util.ArrayList;

public class MainAccident {

    public ArrayList<String>reqsURL=new ArrayList();
    public ArrayList<String>reqsRef=new ArrayList();


    public boolean start;
    public boolean mid;

    public boolean end;

    public int times;

    public long now=System.currentTimeMillis();

    private int a0;
    private int a1;
    public void check(String request,String ref){
        reqsURL.add(request);
        reqsRef.add(ref);
        long now =System.currentTimeMillis();

        if(now>this.now+100){
            this.now=now;
            if(a1>2*a0){
                if(!(end||start)) {
                    start = true;
                }
            }
            if(start){
                if(a1-a0<0){
                    start=false;
                    end=true;
                }
                if(now>this.now+5000){
                    end=false;
                    start=false;
                    analyze();
                }
            }
            if(end){
                if(a1-a0>=0){
                    end=false;
                    start=false;
                    analyze();
                }
            }
            a0=a1;
            a1=0;
        }else {
            a1++;
        }

        if(reqsURL.size()>200&&!start){
            reqsRef.clear();
            reqsURL.clear();
        }
    }
    public void analyze(){
        //reqs

        int len=reqsRef.size();
        String []url=new String[len];
        int count[]=new int[len];
        for (int i = 0; i < len; i++) {
            try {
                url[i]=reqsURL.get(i);
            }catch (Exception e){
                System.out.println("异常");
                //System.out.println(reqs.get(i));
               // System.out.println(urls);
            }
            count[i]=0;
        }
        for (int i = 0; i < len; i++) {
            String ref=reqsRef.get(i);
            if(ref==null||ref==""){
                continue;
            }
            for (int j = 0; j < url.length; j++) {

                if(url[j]==null&&url[j].equals(ref)){
                    count[i]++;
                    break;
                }
            }
        }
        int  pos=0;
        for (int i = 0; i < count.length; i++) {
            if(count[i]>count[pos]){
                pos=i;
            }
        }
        StringBuffer buffer=new StringBuffer();
        for (int i = pos; i < url.length; i++) {
            buffer.append(url[i]+"\n");
        }
        buffer.append("\n");
        for (int i = 0; i < len; i++) {
            buffer.append("url:"+reqsURL.get(i));
            buffer.append("\n");
            buffer.append("ref:"+reqsRef.get(i));
            buffer.append("\n");
        }

        FileSaving fileSaving=new FileSaving("C:\\Users\\xqy\\Desktop\\mt\\");

        if(pos>0){
            fileSaving.accidentSave("诱因:"+reqsURL.get(pos-1)+" \n主要事件:"+url[pos]+" \n爆发事件:"+buffer.toString(),url[pos].substring(url[pos].indexOf("://")+2).replace(":","_"));
        }
        else if(url.length>pos)
            fileSaving.accidentSave("主要事件:"+url[pos]+" \n爆发事件:"+buffer.toString(),url[pos].substring(url[pos].indexOf("://")+2).replace(":","_"));

        reqsRef.clear();
        reqsURL.clear();

    }



}
