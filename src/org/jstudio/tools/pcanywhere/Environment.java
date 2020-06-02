package org.jstudio.tools.pcanywhere;

/**
 * <p>Title: 配置</p>
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
public class Environment {
    public static final int UDP_PORT = 2345;  //UDP端口
    public static final int TCP_TIME_OUT = 500;  //套接字超时时间
    public static final int RECEIVER_COUNT = 5 ;  //接收控制端信息线程
    public static final int EVENT_CACHE = 1024 * 5; //事件发送，接收缓存大小
    public static final int IMAGE_CACHE = 1024 ; //图象缓存
    public static final int IMAGE_GETTIME = 300; //采集图象时间间隔

    public static final String COMMAND_SEPRATER = ":"; //命令名和值分隔符

}
