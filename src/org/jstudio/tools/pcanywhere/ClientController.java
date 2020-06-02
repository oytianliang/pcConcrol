package org.jstudio.tools.pcanywhere;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;

/**
 * <p>Title: 被控端</p>
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
public class ClientController {

    public static void ControlledResponse(InetAddress ip,String command )throws Exception{

        if (command.startsWith(CommandHandle.Command_Connection)) { //建立连接，返回被控端基本信息
            sendControlledstatus(ip, command);
        }
        else if (command.startsWith(CommandHandle.Command_Screen)) { //发送被控端屏幕图像
            startScreen(ip, command);
        }
        else if (command.startsWith(CommandHandle.Command_Control)) { //启动控制响应线程
            startScreenControlResponse(ip, command);
        }
        else if (command.startsWith(CommandHandle.Command_Disconnection)) { //断开连接
            ThreadManager.quit(ControlledScreenImageThread.class);
            ThreadManager.quit(ControlledWindowResponseThread.class);
        }
    }

    private static void sendControlledstatus(InetAddress ip, String command) throws IOException {

        Socket socket =
                new Socket(ip, Integer.parseInt(getValue(command)));
        ObjectOutputStream send =
                new ObjectOutputStream(socket.getOutputStream()); //封装流
        Dimension dim =
                Toolkit.getDefaultToolkit().getScreenSize(); //获取屏幕高和宽

        ControlledStatus cs = ControlledStatus.getControlledstatus(ip, Environment.UDP_PORT, dim.getHeight(),
                                                                   dim.getWidth()); //产生要发送的状态对象
        send.writeObject(cs); //发送
        send.close();
        socket.close();
    }

    private static String getValue(String commandString) {
        if (commandString == null || commandString.equals(""))
            return null;

        StringTokenizer toke =
                new StringTokenizer(commandString, Environment.COMMAND_SEPRATER);
        toke.nextToken();

        return toke.nextToken();
    }

    private static void startScreen(InetAddress ip,String command) throws Exception{
        Class[] argTypes = new Class[]{InetAddress.class,Integer.class};
        Object[] args = new Object[]{ip,new Integer(getValue(command))};
        ThreadManager.start(ControlledScreenImageThread.class,argTypes,args);
    }

    /**
     * 被控端屏幕控制响应
     * @param serverip InetAddress
     * @param order String
     * @throws IOException
     * @throws AWTException
     */
    private static void startScreenControlResponse(InetAddress ip,String command) throws Exception{
        Socket socket=new Socket(ip,Integer.parseInt(getValue(command)));//连接服务器

        Class[] argTypes = new Class[] {Socket.class};
        Object[] args = new Object[] {socket};
        ThreadManager.start(ControlledWindowResponseThread.class, argTypes, args);

    }
}
