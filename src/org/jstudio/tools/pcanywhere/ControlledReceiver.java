package org.jstudio.tools.pcanywhere;

import java.net.*;
import javax.swing.JOptionPane;

/**
 * <p>Title: 被控端接收线程</p>
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
public class ControlledReceiver {

    private DatagramSocket stockUDP ;

    public ControlledReceiver(){
        try{
            stockUDP = new DatagramSocket(Environment.UDP_PORT);
            System.out.println("成功开启UDP端口 "+Environment.UDP_PORT);
            startReceiver();//启动命令接收线程
        }catch(SocketException e){
            JOptionPane.showMessageDialog(null, "开启UDP端口 "+Environment.UDP_PORT+ "失败!");
            System.exit(0);
        }
    }

    /**
     * 启动接收线程
     */
    private void startReceiver() {
        for (int i = 0; i < Environment.RECEIVER_COUNT; i++){
            new ControlledReceiverThread(stockUDP).start();
            System.out.println("被控端启动接收线程 " + (i + 1) + "...");
        }
    }

    /*
     * 获得我的UDP端口
     */
    public int getUDPPort(){
        return this.stockUDP.getLocalPort();
    }

}

class ControlledReceiverThread extends Thread{
    private DatagramSocket stockUDP ;   //指令接受UDP
    DatagramPacket packet ;
    byte command[] = new byte[512];    //用于存放指令

    public ControlledReceiverThread(DatagramSocket socket){
        stockUDP = socket ;
        packet = new DatagramPacket( command , command.length );//接受数据包
    }

    public void run(){
        while(true){
            try {
                stockUDP.receive(packet);
                String message=new String(packet.getData(),0,packet.getLength());
                System.out.println( new java.sql.Timestamp(new java.util.Date().getTime() )+" 接收到指令 : "+message);
                ClientController.ControlledResponse(packet.getAddress(),message);
            }
            catch (Exception e) {
            }
        }
    }
}
