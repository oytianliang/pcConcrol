package org.jstudio.tools.pcanywhere;

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
public class JPcAnyWhereControlled {
    private static JPcAnyWhereControlled client = null ;
    ControlledReceiver receiver ;

    private JPcAnyWhereControlled(){
        try {
            receiver = new ControlledReceiver(); //开启客户端服务
        }
        catch (Exception e) {
        }
    }

    public static void start(){
        if(client==null){
            client = new JPcAnyWhereControlled();
        }
    }

    public static JPcAnyWhereControlled getClient() {
        if (client == null) {
            start();
        }
        return client;
    }

    public ControlledReceiver getReceiver(){
        return receiver ;
    }

    public static void main(String[] args){
        JPcAnyWhereControlled.start();
    }
}
