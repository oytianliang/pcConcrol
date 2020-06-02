package org.jstudio.tools.pcanywhere;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;

/**
 * <p>Title: 目标机器ip输入对话框</p>
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
public class ConnectionDialog extends JDialog {
    JPanel panel1 = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JTextField address = new JTextField();
    JButton ok = new JButton();

    public ConnectionDialog(MainFrame owner, String title, boolean modal) {
        super(owner, title, modal);
        try {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            jbInit();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        panel1.setLayout(borderLayout1);
        ok.setText("确定");
        ok.addActionListener(new ConnectionDialog_Ok_actionAdapter(this));
        setContentPane(panel1);
        panel1.add(address, java.awt.BorderLayout.CENTER);
        panel1.add(ok, java.awt.BorderLayout.EAST);

        setSize(200,50);
        setMainFrameCenter(this);
        setResizable(false);
    }

    /**
     * 设置弹出窗口显示位置在主窗口中间
     * @param dialog JDialog
     */
    public static void setMainFrameCenter(JDialog dialog){
        Point point = dialog.getOwner().getLocation();
        Dimension dimension = dialog.getOwner().getSize();
        dialog.setLocation(point.x+dimension.width/2-dialog.getSize().width/2,
                         point.y+dimension.height/2-dialog.getSize().height/2);//设置位置
    }

    public InetAddress getEnterInetAddress() throws UnknownHostException{
        return InetAddress.getByName(address.getText());
    }

}

class ConnectionDialog_Ok_actionAdapter
        implements ActionListener {

    ConnectionDialog adaptee ;

    public ConnectionDialog_Ok_actionAdapter(ConnectionDialog adaptee){
        this.adaptee = adaptee;
    }

    /**
     * 按下"确定"按钮,开始连接被控端
     *
     * @param e ActionEvent
     * @todo Implement this java.awt.event.ActionListener method
     */
    public void actionPerformed(ActionEvent e) {
        InetAddress inet = null;
        adaptee.setVisible(false);
        MainFrame main = ((MainFrame)adaptee.getOwner());
        try{
            main.setEnabled(false);
            inet = adaptee.getEnterInetAddress();

            ControlledStatus client = MainController.Connect(inet);//发送初试化命令

            main.setConnection(client);
            main.setEnabled(true);

            MainController.startClientScreen(client,main);//开启被控端屏幕监听线程
            main.screen.setMainControl(MainController.startControlSocket(client));//开启控制连接
            main.screen.setControlledStatus(client);

            main.jToolBar1.disconnect.setEnabled(true);
            main.jToolBar1.connect.setEnabled(false);

        }catch(UnknownHostException e1){
            JOptionPane.showMessageDialog(main,"被控端地址错误!","错误信息",JOptionPane.WARNING_MESSAGE);
        }catch(Exception e2){
            e2.printStackTrace();
            JOptionPane.showMessageDialog(main,e2.getMessage(),"错误信息",JOptionPane.WARNING_MESSAGE);
        }
        finally{
            adaptee.dispose();
            main.setEnabled(true);
        }
    }
}


