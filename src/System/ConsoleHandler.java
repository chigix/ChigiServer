package System;

import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * 终端控制类
 * @author Richard Lea
 */
public class ConsoleHandler {
    private ArrayList msg = null;
    public static final PrintWriter TERMINAL;

    static{
        TERMINAL = new PrintWriter(System.out,true);
    }
    public ConsoleHandler() {
        this.msg = new ArrayList();
    }
    
    /**
     * 加入需要被调试的信息
     * @param msg
     * @return 
     */
    public ConsoleHandler log(String msg){
        if (msg != null && !msg.isEmpty()) {
            this.msg.add("[" + ChigiFnc.getDate() + "] " + "\"" + msg + "\"");
        }
        return this;
    }

    /**
     * 加入需要被调试的信息
     * @param prefix
     * @param msg
     * @return 
     */
    public ConsoleHandler log(String prefix,String msg){
        if (msg == null) {
            msg = "NULL";
        }
        if (!msg.isEmpty()) {
            this.msg.add(prefix + " - - [" + ChigiFnc.getDate() + "] " + "\"" + msg + "\"");
        }
        return this;
    }
    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder("");
        for (Object item : this.msg) {
            tmp.append("\n").append(item);
        }
        return tmp.toString();
    }
    
    /**
     * 线程安全地输出
     */
    public void print(){
        synchronized(ConsoleHandler.TERMINAL){
            ConsoleHandler.TERMINAL.println(this);
        }
    }
    
}
