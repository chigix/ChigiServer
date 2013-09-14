package System;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 服务器文件控制类
 *
 * @author Richard Lea
 *
 */
public class FileHandler {

    private String error = null;
    private ArrayList<Integer> data = null;

    /**
     * 带指定路径参数构造
     *
     * @param path 指定目标文件路径
     */
    public FileHandler(String path) {
        this.data = new ArrayList<Integer>();
        FileInputStream fis = null;
        // 尝试获取目标文件
        try {
            fis = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            Logger.getLogger(FileHandler.class.getName())
                    .log(Level.SEVERE, "文件" + path + "不存在", e);
            this.error = "File Not Found:" + path;
            return;
        }
        // 尝试读取文件内容
        BufferedInputStream bis = new BufferedInputStream(fis);
        int tmp_d = -1;
        try {
            while ((tmp_d = bis.read()) != -1) {
                this.data.add(tmp_d);
            }
        } catch (IOException e) {
            Logger.getLogger(FileHandler.class.getName())
                    .log(Level.SEVERE, "文件" + path + "无法读取，请检查权限", e);
            this.error = "File Read Error:" + path;
            return;
        }
        // 至此构造函数结束，且数据已存入data 属性中
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

    /**
     * 获取当前文件对象的输出数据的字节长度
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    public int lengthByte() {
        if (this.error != null) {
            return this.error.getBytes().length;
        } else {
            return this.data.size();
        }
    }
}
