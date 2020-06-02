package org.jstudio.tools.pcanywhere;

import java.io.*;
import java.net.*;
import java.awt.event.InputEvent;

/**
 * <p>Title: 涓绘帶绔�/p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MainController {

    /*-杩炴帴琚帶绔�*/
    public static ControlledStatus Connect(InetAddress inet) throws Exception{
        ControlledStatus status = null;
        ServerSocket server = openNewPort();
        Socket socket = null ;
        try {
            server.setSoTimeout(Environment.TCP_TIME_OUT); //璁剧疆瓒呮椂
            sendCommand(inet, CommandHandle.toCommand(CommandHandle.Command_Connection, server.getLocalPort()));
            socket = server.accept(); //寮�惎
            ObjectInputStream read = new ObjectInputStream(socket.getInputStream()); //灏佽娴侊紝鍑嗗璇诲彇涓�釜瀵硅薄
            Object obj = read.readObject();
            if (obj != null) {
                status = (ControlledStatus) obj;
                status.setControlled(inet);
            }
            else {
                throw new IOException("杩炴帴澶辫触:\n  鐩爣鏈哄櫒涓嶅彲杈�");
            }
        }
        catch (IOException e) {
            throw new Exception("杩炴帴澶辫触:\n  鐩爣鏈哄櫒涓嶅彲杈�");
        }
        finally {
            try {
                if (socket != null)
                    socket.close();
                server.close();
            }
            catch (IOException e) {
            }
        }
        return status;
    }

    /*-鏂紑琚帶绔�*/
    public static void Disconnect(InetAddress inet){
        try{
            sendCommand(inet, CommandHandle.toCommand(CommandHandle.Command_Disconnection, ""));
        }catch(IOException e){}
    }

    /**
     * 寮�惎涓绘帶绔竴涓彲鐢ㄩ殢鏈虹鍙ｇ殑ServerSocket
     * @return ServerSocket
     * @throws Exception
     */
    public static ServerSocket openNewPort()throws IOException{
        ServerSocket socket = null;
        boolean sucess = true;
        int count=0;
        while(sucess){
            count++;
            //灏濊瘯10娆★紝杩炴帴涓嶆垚鍔熷氨鏀惧純
            if(count>=10)throw new IOException("杩炴帴澶辫触,鏃犳硶寮�惎鏈満鍣ㄧ鍙�");
            int c=(int)(Math.random()*10000);
            int port=(int)(c-65530*(c/65530));

            try {
                socket = new ServerSocket(port);
                sucess = false;
            }
            catch (IOException e) {
                continue ;
            }
        }
        return socket ;
    }

    /*-鍚戣鎺х鍙戦�鎸囦护-*/
    public static void sendCommand(InetAddress inet,String command)throws IOException{
        byte sp[] = command.getBytes();
        DatagramPacket packet = new DatagramPacket(sp, sp.length, inet, Environment.UDP_PORT);
        try {
            DatagramSocket sd = new DatagramSocket();
            sd.send(packet);
        }
        catch (Exception e) {
            throw new IOException("鍚戣鎺х璇锋眰澶辫触!");
        }
    }

    /**
     * 寮�惎鑾峰彇琚帶绔睆骞曠殑鐩戝惉绾跨▼
     * @param clientstatus ClientStatus
     * @throws Exception
     */
    public static void startClientScreen(ControlledStatus clientstatus,MainFrame frame) throws Exception{
        ServerSocket server = openNewPort();
        server.setSoTimeout(Environment.TCP_TIME_OUT);//璁剧疆瓒呮椂

        sendCommand(clientstatus.getControlled(),
                    CommandHandle.toCommand(CommandHandle.Command_Screen,server.getLocalPort()));

        Socket socket=server.accept();
        System.out.println(socket.getRemoteSocketAddress()+" 宸茬粡杩炴帴绔彛锛�"+socket.getLocalPort()+" 绛夊緟杩涜鍥惧舰浼犻�");

        //new ImageReceiveThread(socket,frame).start();//鍚姩鍥捐薄鏄剧ず绾跨▼锛屾洿鏂板浘鍍�

        Class[] argTypes = new Class[]{Socket.class,MainFrame.class};
        Object[] args = new Object[]{socket,frame};
        ThreadManager.start(ImageReceiveThread.class,argTypes,args);

    }

    /*-寮�惎鎺у埗socket-*/
    public static MainControlSocket startControlSocket(ControlledStatus clientstatus)throws IOException{
        MainControlSocket control = null ;
        try {
            ServerSocket server = openNewPort();
            server.setSoTimeout(Environment.TCP_TIME_OUT);//璁剧疆瓒呮椂

            sendCommand(clientstatus.getControlled(),
                    CommandHandle.toCommand(CommandHandle.Command_Control,server.getLocalPort()));

            Socket socket=server.accept();
            System.out.println(socket.getRemoteSocketAddress()+" 宸茬粡杩炴帴绔彛锛�"+socket.getLocalPort()+" 鎺у埗濂楁帴寮�惎");

            control = new MainControlSocket(socket);
        }
        catch (IOException e) {
            throw new IOException("缃戠粶鏁呴殰");
        }
        return control ;
    }
}

class MainControlSocket {

    private Socket socket = null;
    private ObjectOutputStream out = null; //浜嬩欢瀵硅薄鍙戦�灏佽

    public MainControlSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setSendBufferSize(Environment.EVENT_CACHE);
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    /*
     * 鍙戦�浜嬩欢
     */
    public void sendControlledAction(InputEvent event) {
        try {
            out.writeObject(event);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            if(out!=null){
                out.close();
                out = null ;
            }
            if(socket!=null){
                socket.close();
                socket = null ;
            }
        }
        catch (IOException e) {
        }
    }
}
