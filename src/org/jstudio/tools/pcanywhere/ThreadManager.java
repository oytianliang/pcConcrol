package org.jstudio.tools.pcanywhere;

import java.util.*;
import java.net.InetAddress;

/**
 * <p>Title: 线程管理</p>
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
public class ThreadManager {
    private static HashMap threadMap = new HashMap();

    public static void start(Class cls,Class[] argTypes,Object[] args)throws Exception{
        ThreadServer t = (ThreadServer)threadMap.get(cls.getName());
        if(t==null){
            t = (ThreadServer)(cls.getConstructor(argTypes).newInstance(args));
            t.start();
            System.out.println(cls.getName() + "is start ");
            threadMap.put(cls.getName(),t);
        }
    }

    public static void quit(Class cls){
        ThreadServer t = (ThreadServer)threadMap.get(cls.getName());
        if(t!=null){
            t.quit();
            remove(cls);
            System.out.println(cls.getName() + "is quit ");
        }
    }

    public static void remove(Class cls){
        if(threadMap.containsKey(cls.getName())){
            threadMap.remove(cls.getName());
            System.out.println(cls.getName() + "is remove ");
        }
    }
}

abstract class ThreadServer extends Thread{
    public abstract void quit();
}
