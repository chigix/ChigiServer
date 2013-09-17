package System;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 全局工具函数包
 * @author Richard Lea
 */
public class ChigiFnc {
    public static final int LEVEL_LOG = 0;
    public static final int LEVEL_OUT_HEADER = 1;
    /**
     * 获取当前日期
     * @return 
     */
    public static String getDate(){
        return ChigiFnc.getDate(ChigiFnc.LEVEL_LOG);
    }
    
    /**
     * 指定格式级别 获取当前日期
     * @param level
     * @return 
     */
    public static String getDate(int level){
        return ChigiFnc.getDate(new Date(), level);
    }
    
    /**
     * 获取指定时间戳 的日期字符串
     * @param date
     * @param level
     * @return 
     */
    public static String getDate(Long date,int level){
        return ChigiFnc.getDate(new Date(date), level);
    }
    /**
     * 获取指定 Date 对象的日期字符串
     * @param date
     * @param level 
     * @return
     */
    public static String getDate(Date date,int level){
        SimpleDateFormat format = null;
        switch (level){
            case 0:
                // LEVEL_LOG
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                break;
            case 1:
                // LEVEL_OUT_HEADER
                format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
                break;
            default:
                format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z", Locale.US);
                break;
        }
        return format.format(date);
    }
}
