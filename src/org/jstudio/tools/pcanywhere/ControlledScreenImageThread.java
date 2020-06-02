package org.jstudio.tools.pcanywhere;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;
import com.sun.image.codec.jpeg.*;

/**
 * <p>Title: 被控端屏幕图像采集传送线程</p>
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
public class ControlledScreenImageThread extends ThreadServer {
    ImageProvider provider ;
    Socket client ;
    OutputStream out ;
    BufferedImage image ;
    JPEGImageEncoder encoder ;

    private boolean isRun = true;

    public ControlledScreenImageThread(InetAddress ip , Integer port)throws IOException, AWTException{
        client = new Socket(ip,port.intValue());
        client.setSendBufferSize(1024);
        provider = new ImageProvider();
        out = client.getOutputStream();
        encoder = JPEGCodec.createJPEGEncoder(out);
    }

    public void run(){
        int i = 0;
        while(isRun){
            image = provider.CopyScreen();

            try {
                encoder.encode(image);
                Thread.sleep(Environment.IMAGE_GETTIME);
            }
            catch (IOException e) {
                i++;
                System.out.println("无法写入对象 " + i);
                if (i == 5){   //尝试5次后放弃
                    break;
                }
            }
            catch (InterruptedException e2) {
                System.out.println("INTERRUPTED~");
            }
        }
        ThreadManager.remove(this.getClass());
    }

    public void quit() {
        isRun = false;
    }
}

/*-屏幕图像采集-*/
class ImageProvider {
    private Robot robot ;
    private Rectangle rect ;

    public ImageProvider()throws AWTException{
        rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());//全屏区域
        robot=new Robot();
    }

    /*
     * 复制全屏幕图像
     */
    public BufferedImage CopyScreen() {
        BufferedImage image = robot.createScreenCapture(rect);
        return image;
    }
    }
