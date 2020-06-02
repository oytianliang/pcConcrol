package org.jstudio.tools.pcanywhere;

import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * <p>Title: </p>
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
public class MainFrame extends JFrame {
    ControlledScreenPanel screen ;
    JPanel panel ;

    int flag = 0 ;  //0初始显示panel面板 1显示screen面板

    MainToolBar jToolBar1 ;
    public MainFrame() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        screen = new ControlledScreenPanel();
        panel = new JPanel();
        jToolBar1 = new MainToolBar(this);

        this.setSize(new Dimension(600, 400));
        this.setTitle("远程控制系统1.0");
        getContentPane().add(jToolBar1, java.awt.BorderLayout.PAGE_START);
        getContentPane().add(panel,java.awt.BorderLayout.CENTER);
        this.setFocusable(true);
    }

    public void switchPanel(int f){
        switch(f){
            case 1 :
                this.remove(panel);
                screen.bind(this);
                break ;
            case 2 :
                screen.remove();
                getContentPane().add(panel,java.awt.BorderLayout.CENTER);
                break ;
        }
        this.flag = f ;
    }

    /**
     * 显示被控端屏幕
     */
    public void showClientScreen(BufferedImage image) {
        if (image == null)return;
        screen.setBufferedImage(image);
    }

    //--------------------------------------------------------------------------
    private ControlledStatus status = null; //被监视端状态信息
    private boolean isConnection = false ; //是否连接被控端

    public boolean isConnection(){
        return isConnection ;
    }

    public ControlledStatus getControlledStatus(){
        return status;
    }

    public void setConnection(ControlledStatus status){
        this.status = status ;
        switchPanel(1);
        setSize((int)(status.getScreenWidth()>1024?1024:status.getScreenWidth()),(int)(status.getScreenHeight()>768?768:status.getScreenHeight()));
        setVisible(false);setVisible(true);
    }

    public void setDisConnection(){
        switchPanel(2) ;
        this.setSize(new Dimension(600, 400));
        setVisible(false);setVisible(true);
        status = null ;
    }

}
