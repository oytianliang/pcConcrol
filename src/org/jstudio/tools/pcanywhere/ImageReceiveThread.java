package org.jstudio.tools.pcanywhere;

import java.io.*;
import java.net.*;
import java.awt.image.*;
import com.sun.image.codec.jpeg.*;

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
public class ImageReceiveThread extends ThreadServer {
    private Socket socket ;
    private MainFrame frame ;
    JPEGImageDecoder de = null;
    BufferedImage image = null;

    private boolean isRun = true;

    public ImageReceiveThread(Socket socket,MainFrame frame) throws Exception{
        this.socket = socket ;
        this.frame = frame ;
        socket.setReceiveBufferSize(Environment.IMAGE_CACHE * 3); //接收缓冲
        de = JPEGCodec.createJPEGDecoder(socket.getInputStream());
    }

    public void run(){
        try {
            while (isRun) {
                image = de.decodeAsBufferedImage();
                if (image != null) {
                    frame.showClientScreen(image);
                }
            }
        }
        catch (Exception e) { }
        finally{
            try {
                socket.close();
                de = null ;
                image = null ;
                ThreadManager.remove(this.getClass());
            }
            catch (Exception e) {}
        }
    }

    public void quit(){
        isRun = false;
    }

}
