package org.jstudio.tools.pcanywhere;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>Title: 显示被控端图像</p>
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
public class ControlledScreenPanel extends JPanel implements MouseMotionListener ,MouseListener,MouseWheelListener ,KeyListener{
    private JFrame main ;
    private JScrollPane ps ;
    private BufferedImage mJPEGPanelBufferedImage ;

    private ControlledStatus client ;
    private MainControlSocket control ;

   public ControlledScreenPanel(){
       main = null;
       init();
   }

    public ControlledScreenPanel(JFrame frame){
        init();
        bind(frame);
    }

    private void init() {
        ps = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                             JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    protected void bind(JFrame frame){
        main = frame ;
        main.getContentPane().add(ps, BorderLayout.CENTER);
    }

    protected void remove(){
        main.remove(ps);
    }

    public void setControlledStatus(ControlledStatus status){
        client = status ;
    }

    public void setMainControl(MainControlSocket controller){
        control = controller ;
    }

    public void setBufferedImage(BufferedImage bi) {
        mJPEGPanelBufferedImage = bi;
        Dimension d = new Dimension(mJPEGPanelBufferedImage.getWidth(this),mJPEGPanelBufferedImage.getHeight(this));
        setPreferredSize(d);
        revalidate();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();
        g.setColor(getBackground());
        g.fillRect(0, 0, d.width, d.height);
        if (mJPEGPanelBufferedImage != null) {
            g.drawImage(mJPEGPanelBufferedImage, 0, 0, this);
        }
    }

    //--------------------------------------------------------------------------
    public void mouseDragged(MouseEvent e) {
        control.sendControlledAction(e);
    }

    public void mouseMoved(MouseEvent e) {
        control.sendControlledAction(e);
    }

    //--------------------------------------------------------------------------
    public void mouseClicked(MouseEvent e) {
        requestFocus();//获得焦点
    }

    public void mousePressed(MouseEvent e) {
        control.sendControlledAction(e);
    }

    public void mouseReleased(MouseEvent e) {
        control.sendControlledAction(e);
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {
    }

    //--------------------------------------------------------------------------

    public void mouseWheelMoved (MouseWheelEvent e){
        control.sendControlledAction(e);
    }

    //--------------------------------------------------------------------------

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        control.sendControlledAction(e);
    }

    public void keyReleased(KeyEvent e) {
        control.sendControlledAction(e);
    }

}
