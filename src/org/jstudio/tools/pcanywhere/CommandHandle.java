package org.jstudio.tools.pcanywhere;

/**
 * <p>Title: 被控端命令处理 </p>
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
public class CommandHandle {
    public static final String Command_Connection = "connection" ; //建立连接
    public static final String Command_Disconnection = "disconnection" ; //断开连接
    public static final String Command_Screen = "screen";   //显示被控端屏幕
    public static final String Command_Control="control";   //建立控制套接

    /*
     * 命令以name:value为格式
     */
    public static String toCommand(String name, String value) {
        return name + ":" + value;

    }

    public static String toCommand(String name,int value){
        return name + ":" + value ;
    }
}
