package org.jstudio.tools.pcanywhere;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>Title: 被控端卓面响应线程</p>
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
public class ControlledWindowResponseThread extends ThreadServer {
    private Socket socket ;
    private ObjectInputStream in ;
    private Robot action ;

    private boolean isRun = true;

    public ControlledWindowResponseThread(Socket socket) throws IOException, AWTException {
        action = new Robot();
        this.socket = socket;
        this.socket.setReceiveBufferSize(Environment.EVENT_CACHE);
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void run(){
        while (isRun) {
            try {
                Object obj = in.readObject();
                if (obj != null) {
                    handleEvent( (InputEvent) obj); //处理
                }
            }
            catch (ClassNotFoundException e) { continue; }
            catch (IOException ex) { break; }
        }

        try {
            if (in != null) { in.close(); in = null; }
            if (socket != null) { socket.close(); socket = null; }
            ThreadManager.remove(this.getClass());
        }catch (Exception e) {}

    }

    /*-事件处理-*/
    private void handleEvent(InputEvent event){
        MouseEvent mevent = null ; //鼠标事件
        MouseWheelEvent mwevent = null ;//鼠标滚动事件
        KeyEvent kevent = null ; //键盘事件
        int mousebuttonmask = -100; //鼠标按键

        switch (event.getID()){
            case MouseEvent.MOUSE_MOVED :                                      //鼠标移动
                mevent = ( MouseEvent )event ;
                action.mouseMove( mevent.getX() , mevent.getY() );
                break ;

            case MouseEvent.MOUSE_PRESSED :                                    //鼠标键按下
                mevent = ( MouseEvent ) event;
                action.mouseMove( mevent.getX() , mevent.getY() );
                mousebuttonmask = getMouseClick( mevent.getButton() );
                if(mousebuttonmask != -100)
                    action.mousePress(mousebuttonmask);
                break;

            case MouseEvent.MOUSE_RELEASED :                                   //鼠标键松开
                mevent = ( MouseEvent ) event;
                action.mouseMove( mevent.getX() , mevent.getY() );
                mousebuttonmask = getMouseClick( mevent.getButton() );
                if(mousebuttonmask != -100)
                    action.mouseRelease( mousebuttonmask );
                break ;

            case MouseEvent.MOUSE_WHEEL :                                      //鼠标滚动
                mwevent = ( MouseWheelEvent ) event ;
                action.mouseWheel(mwevent.getWheelRotation());
                break ;

            case MouseEvent.MOUSE_DRAGGED :                                    //鼠标拖拽
                mevent = ( MouseEvent ) event ;
                action.mouseMove( mevent.getX(), mevent.getY() );
                break ;

            case KeyEvent.KEY_PRESSED :                                        //按键
                kevent = ( KeyEvent ) event;
                action.keyPress( kevent.getKeyCode() );
                break ;

            case KeyEvent.KEY_RELEASED :                                       //松键
                kevent= ( KeyEvent ) event ;
                action.keyRelease( kevent.getKeyCode() );
                break ;

            default: break ;
        }
    }

    /*
     * 鼠标按键翻译
     */
    private int getMouseClick(int button) {
        if (button == MouseEvent.BUTTON1) //左键 ,中间键为BUTTON2
            return InputEvent.BUTTON1_MASK;
        if (button == MouseEvent.BUTTON3) //右键
            return InputEvent.BUTTON3_MASK;
        return -100;
    }

    /*
     * 鼠标单击
     */
    public void mouseClick(int mousebuttonmask) {
        action.mousePress(mousebuttonmask);
        action.mouseRelease(mousebuttonmask);
    }

    public void quit() {
        isRun = false;
    }

}
