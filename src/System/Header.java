package System;

import java.util.Locale;

/**
 * HTTP 报头类
 *
 * @author Richard Lea
 *
 */
public class Header {

    private String type = null;
    private String uri = null;
    private String protocol = null;
    private String host = "localhost";
    private String userAgent = null;
    private String[] accept = null;
    private String acceptLanguage = null;
    private Locale locale = null;
    private String acceptEncoding = null;
    private String acceptCharset = "UTF-8";
    private int keepAlive = 115;
    private String connection = "keep-alive";
    private String contentType = "text/html";

    public Header() {
    }

    /**
     * 推入一条具体的报头信息 Header类会自动解析并分配到对应位置 注：默认插入的第一条会作为请求类型判断， 故可用clear
     * 方法清空当前报头信息。
     *
     * @param msg
     * @return
     */
    public Header push(String msg) {
        if (this.type == null) {
            // 当前为首报头信息解析
            String[] tmp_header = msg.split(" ", 3);
            this.type = tmp_header[0].trim();
            this.uri = tmp_header[1].trim();
            this.protocol = tmp_header[2].trim();
        } else {
            // k-v 报头信息解析
            String[] tmp_header = msg.split(":", 2);
            this.kvParser(tmp_header[0], tmp_header[1]);
        }
        return this;
    }

    /**
     * 获取目标URI
     *
     * @return
     */
    public String getUri() {
        return uri;
    }

    /**
     * 手动清空报头操作
     *
     * @return
     */
    public Header clear() {
        this.type = null;
        this.uri = null;
        this.protocol = null;
        this.userAgent = null;
        this.accept = null;
        this.acceptLanguage = null;
        this.acceptEncoding = null;
        this.acceptCharset = "UTF-8";
        this.keepAlive = 115;
        this.connection = "keep-alive";
        return this;
    }

    /**
     * 返回目标脚本文件的所在路径
     *
     * @return
     */
    public String getScriptPath() {
        int index_dot = this.uri.indexOf(".");
        if (index_dot == -1) {
            // 点不存在，即为声明MIME类型
            if (this.uri.endsWith("/") || this.uri.endsWith("\\")) {
                // 检测URI字符串末尾是否写了斜杠
                return this.uri.trim() + "index.html";
            } else {
                return this.uri.trim() + "/index.html";
            }
        } else {
            // 检测到点号的位置，即有MIME类型声明
            int index_slash = this.uri.indexOf("/", index_dot);
            int index_mark = this.uri.indexOf("?", index_dot);
            int index_pathEnd = 0;
            if (index_slash == -1 && index_mark == -1) {
                // 未检测到斜杠或问号，即点号后所有字符为MIME类型声明
                return this.uri.trim();
            } else if (index_slash == -1) {
                index_pathEnd = index_mark;
            } else if (index_mark == -1) {
                index_pathEnd = index_slash;
            } else {
                index_pathEnd = Integer.compare(index_slash, index_mark) > 0 ? index_mark : index_slash;
            }
            // 已检测到斜杠或问号，则截取目标标记前到点号部分所有内容为MIME类型
            return this.uri.substring(0, index_pathEnd).trim();
        }
    }
    
    /**
     * HTTP 报头信息 k-v 解释器
     * @param key
     * @param value
     * @return 
     */
    public int kvParser(String key,String value){
        value = value.trim();
        switch (key.trim().toLowerCase()) {
            case "host":
                int i = -1;
                this.host = value.substring(0, (i = value.indexOf(":")) == -1?value.length()-1:i);
                return 1;
            case "connection":
                this.connection = value;
                return 1;
            case "accept":
                int index_comma = value.indexOf(";");
                String declare = value.substring(0, index_comma == -1? value.length()-1:index_comma);
                this.accept = declare.split(",");
                String tmp_type = null;
                if (this.getExt() != null) {
                    // 有明确格式名
                    tmp_type = (String) Settings.EXT_MIME.get(this.getExt());
                    this.contentType = (tmp_type==null)? null:tmp_type;
                }
                return 1;
            case "accept-language":
                return 1;
            default: return 0;
        }
    }
    
    public String getContentType() {
        return contentType;
    }
    
    /**
     * 获取后缀扩展名
     * 无则返回 null
     * @return 
     */
    public String getExt(){
        int index = this.uri.lastIndexOf(".");
        String ext = null;
        if (index != -1) {
            ext = this.uri.substring(index+1, this.uri.length());
        }
        return ext;
    }
}
