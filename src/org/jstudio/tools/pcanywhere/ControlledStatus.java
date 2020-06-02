package org.jstudio.tools.pcanywhere;

import java.io.*;
import java.net.*;

/**
 * <p>Title: 被控端信息</p>
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
public class ControlledStatus implements Serializable {
    private InetAddress controlled ;                 //被监视端IP地址
    private InetAddress main ;
    private int port ;                       //端口号
    private double screen_height ;           //屏幕高
    private double screen_width  ;           //屏幕宽

    public ControlledStatus(InetAddress controlled,InetAddress main){
        this.controlled = controlled ;
        this.main = main ;
        port = -1;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getControlled() {
        return controlled;
    }

    public void setControlled(InetAddress inet){
        controlled = inet ;
    }

    public InetAddress getMain(){
        return main;
    }

    public void setMain(InetAddress inet){
        main = inet ;
    }

    public double getScreenHeight() {
        return screen_height;
    }

    public void setScreenHeight(double screen_height) {
        this.screen_height = screen_height;
    }

    public double getScreenWidth() {
        return screen_width;
    }

    public void setScreenWidth(double screen_width) {
        this.screen_width = screen_width;
    }

    /*
     * 被控端当前状态信息
     */
    public static ControlledStatus getControlledstatus(InetAddress main, int udpport, double hei, double wid) {
        ControlledStatus status = new ControlledStatus(null,main);
        status.setPort(udpport);
        status.setScreenHeight(hei);
        status.setScreenWidth(wid);
        return status;
    }
}
