package UDP;

//import org.jsoup.Connection;
//import org.jsoup.Jsoup;

import bean.SendReq;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ClientB {
    private DatagramSocket socket;
    private InetAddress hostAddress;
    private byte[] buf;
    private java.net.DatagramSocket nextSocket;
    private java.net.InetAddress nextAddress;
    private int nextPort;

    public ClientB() {

        try { socket = new DatagramSocket();
            hostAddress = InetAddress.getByName(hostString);
            socket.setSoTimeout(800);
        }
        catch (java.net.SocketException e) {
            e.printStackTrace();
        } catch (java.net.UnknownHostException e) {
            e.printStackTrace();
        }
        buf=new byte[1024];
    }

    public static void main(String[] args) {
        if(args.length>1) {
            hostString = args[0];
            hostPort = Integer.parseInt(args[1]);
        }

        final ClientB client = new ClientB();
        client.sendEcho(1,"2","22");
        Thread t1 = new Thread(() -> {
            while (true){
                client.sendReqs[client.sendReqIndex++]=new SendReq(12,"ss","ss");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for (int i = 0; i < 1000; i++) {
            if(client.isConnected){
                client.keepConnect();
            }else if(client.initConnect()){
                System.out.println("start");

            }else {
                System.out.println("error");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        client.close();
    }
    private boolean isConnected;


    static String hostString="106.14.118.135";
    static int hostPort=17000;

    private InetAddress connect_target_address;
    private int connect_target_port;
    DatagramPacket pacSend;
    private void keepConnect(){
        while (true){
            if(sendReqIndex!=0){
                sendReqIndex--;
                SendReq reqs=sendReqs[sendReqIndex];
                byte[]sends=sendEcho(reqs.getPriority(),reqs.getContent(),reqs.getType_id());
                sendReqs[sendReqIndex]=null;
                reqs=null;
                pacSend=new DatagramPacket(sends,sends.length, connect_target_address,connect_target_port);
                try {
                    socket.send(pacSend);
                    buf=new byte[1024];
                    pacSend = new DatagramPacket(buf, 1024, hostAddress, hostPort);
                    socket.receive(pacSend);
                    response(pacSend);
                } catch (IOException e) {
                    e.printStackTrace();
                    isConnected=false;
                    break;
                }
            }else {
                try {
                    Thread.sleep(100);
                    pacSend = new DatagramPacket(new byte[]{0}, 1, hostAddress, hostPort);
                    socket.send(pacSend);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    isConnected=false;
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    isConnected=false;
                    break;
                }
            }
        }
    }

    private void response(DatagramPacket pacSend) {
        System.out.println(new String(pacSend.getData()));
    }

    private boolean initConnect(){
        String msg="From b";
        byte[]buf_send = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buf_send, buf_send.length, hostAddress, hostPort);
        buf=new byte[1024];
        DatagramPacket pac = new DatagramPacket(buf, buf.length, hostAddress, hostPort);
        try
        {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            socket.disconnect();
            return false;
        }
        try {
            socket.receive(pac);
        } catch (IOException e) {
            e.printStackTrace();
            socket.disconnect();
            return false;
        }
        String received = new String(pac.getData(),pac.getOffset(),pac.getLength());
        handleReceive(received);
        try {
            if(enableSend)
                while (true) {
                    //buf=new byte[1024];
                    packet = new DatagramPacket(buf, buf.length, connect_target_address,connect_target_port);
                    socket.send(packet);
                    socket.receive(packet);
                    break;
                }
                else{
                    return false;
            }
        } catch (SocketTimeoutException s){
            socket.disconnect();
            return false;
        }catch (IOException e) {
            e.printStackTrace();
            socket.disconnect();
            return false;
        }

        return true;
    }
    SendReq[]sendReqs=new SendReq[128];
    int sendReqIndex=0;
    public byte[] sendEcho(int priority,String content,String type_id)
    {
        StringBuffer buffer=new StringBuffer("q:");
        buffer.append(priority+"\n");
        buffer.append(content);
        buffer.append("\n"+type_id);
        //byte[]sends=Arrays.copyOf(buffer.toString(),buffer.length()+1);
        int len=buffer.toString().length();
        byte[]sends=Arrays.copyOf(buffer.toString().getBytes(),len+1);
        byte check=0;
        for (int i=0;i<sends.length;i++){
            check=(byte) (check+sends[i]);
        }
        sends[len-1]=check;
        return sends;
    }

    private boolean enableSend;


    private void handleReceive(String received) {
        int x=received.lastIndexOf("_");
        if(x!=-1&&x<received.length()-1){
            received=received.substring(x+1);
            String[]tem=received.split(":");
            if(tem.length==2){
                enableSend=true;
                try {
                    connect_target_address=InetAddress.getByName(tem[0]);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                connect_target_port= Integer.parseInt(tem[1]);
                buf="Hi A".getBytes();
                return;
            }
        }
        enableSend=false;
    }

    public void close() {
        socket.close();
    }
}