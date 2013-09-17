package System;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 服务器配置类
 *
 * @author Richard Lea
 */
public class Settings {

    /**
     * 服务器端口号
     */
    public static int PORT = 80;
    /**
     * 服务器默认 WEB 根目录
     */
    public static String DEFAULT_PATH = "./Www";
    /**
     * 服务器是否自动启动
     */
    public static boolean AUTO_START = true;
    /**
     * 当前服务器进程对象
     */
    public static Server SERVER = null;

    /**
     * 当前运行状态 0：未运行 1：正在运行
     */
    public static int STATUS = 0;
    
    /**
     * 存放 扩展名-MIME 映射表
     * 使用示例：Settings.EXT_MIME.get("js")
     */
    public static HashMap EXT_MIME = null;

    public static void init() {
        new Settings();
    }

    private Settings() {
        this.getMainConf();
        this.getMimeList();
    }

    /**
     * 推入指定配置项（k-v格式）
     *
     * @param key
     * @param value
     * @return
     */
    private int pushSetting(String key, String value) {
        switch (key.toLowerCase()) {
            case "port":
                Settings.PORT = Integer.parseInt(value);
                return 1;
            case "default_path":
                DEFAULT_PATH = value;
                return 1;
            case "auto_start":
                AUTO_START = "true".equals(value.toLowerCase())
                        || "1".equals(value.toLowerCase());
                break;
        }
        return 0;
    }

    /**
     * 加载主配置选项
     */
    private void getMainConf() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream("./Conf/Main.conf");
            reader = new BufferedReader(new InputStreamReader(fis));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Conf/Main.conf 配置文件不存在，请检查", ex);
        }
        String tmp_string = null;
        try {
            while ((tmp_string = reader.readLine()) != null) {
                if ("".equals(tmp_string.trim())) {
                    // 当前行为空
                    continue;
                } else if ("#".equals(tmp_string.trim().substring(0, 1))) {
                    // 当前行为注释，跳过之
                    continue;
                } else {
                    // 当前行为配置项，进行读取与解析
                    String[] tmp_item = tmp_string.split(":", 2);
                    this.pushSetting(tmp_item[0].trim(), tmp_item[1].trim());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Main 配置文件读取出错，请检查权限！", ex);
        }
        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Main 配置文件读取结束错误", ex);
        }
    }

    /**
     * 获取 RFC 4288 映射表
     */
    private void getMimeList() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("./Conf/mime.types");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Conf/mime.types 文件不存在，无法获取MIME映射表", ex);
            System.out.println(" MIME 映射表获取失败，服务器启动失败");
        }
        EXT_MIME = new HashMap();
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        String t = null;
        try {
            while ((t = reader.readLine()) != null) {
                if ("".equals(t.trim())) {
                    // 当前行为空
                    continue;
                } else if ("#".equals(t.trim().substring(0, 1))) {
                    // 当前行为注释，跳过之
                    continue;
                } else {
                    // 当前行为配置项，进行读取与解析
                    String[] tmp_item = t.split("[\\p{Blank}]", 2);
                    String[] tmp_ext = tmp_item[1].trim().split("\\p{Blank}");
                    for (String tmp_ext1 : tmp_ext) {
                        String tmp_sub_ext = tmp_ext1;
                        EXT_MIME.put(tmp_sub_ext, tmp_item[0]);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Conf/mime.types 文件无法读取，请检查权限。", ex);
        }
    }
}
