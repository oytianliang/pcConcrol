package org.jstudio.tools.pcanywhere;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
public class MainToolBar extends JMenuBar implements ActionListener{
    MainFrame main ;
    JButton connect ;
    JButton disconnect ;
    JButton address ;

    public MainToolBar(MainFrame frame){
        main = frame ;
        try {
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        connect = makeNavigationButton("Connect", "Connect", "连接被控端", "连接");
        disconnect = makeNavigationButton("Disconnect", "Disconnect", "断开被控端", "断开");
        address = makeNavigationButton("Address", "Address", "我的博客", "网址");
        add(connect);
        add(disconnect);
        add(address);
        disconnect.setEnabled(false);
    }

/*
    private void handevent(JButton button) {
        KeyListener[] _arKL = main.screen.getKeyListeners();
        for (int i = 0; i < _arKL.length; i++) {
            button.addKeyListener(_arKL[i]);
        }
    }
*/
    /**
     * 构造工具栏上的按钮
     * @param imageName String
     * @param actionCommand String
     * @param toolTipText String
     * @param altText String
     * @return JButton
     */
    protected JButton makeNavigationButton(String imageName,String actionCommand,String toolTipText,String altText){
        //初始化工具按钮
        JButton button = new JButton();
        //搜索图片
        String imgLocation = "images/" + imageName + ".gif";
        URL imageURL = MainToolBar.class.getResource(imgLocation);
        //设置按钮的命令
        button.setActionCommand(actionCommand);
        //设置提示信息
        button.setToolTipText(toolTipText);
        button.addActionListener(this);

        if (imageURL != null) {
            button.setIcon(new ImageIcon(imageURL));
            button.setBorder(BorderFactory.createMatteBorder(1,   1,   1,   1,   new   Color(0,   0,   0)));
        }
        else {
            button.setText(altText);
        }
        //handevent(button);
        return button;
    }

    /*-事件监听-*/
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if(command.equals("Connect")){
            ConnectionDialog dialog = new ConnectionDialog(main,"被控制端主机IP地址",true);
            dialog.show();
        }
        else if(command.equals("Disconnect")){
            connect.setEnabled(true);
            disconnect.setEnabled(false);
            MainController.Disconnect(main.getControlledStatus().getControlled());
            ThreadManager.quit(ImageReceiveThread.class);
            main.setDisConnection();
        }
        else if(command.equals("Address")){
            VisitSite();
        }
    }

    public void VisitSite(){
        String   cmd="rundll32   url.dll   FileProtocolHandler   http://jstudiocn.blog.sohu.com";
        try{
            Runtime.getRuntime().exec(cmd);
        }catch(Exception e){}
    }
}
