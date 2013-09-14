package System;

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
	private String userAgent = null;
	private String accept = null;
	private String acceptLanguage = null;
	private String acceptEncoding = null;
	private String acceptCharset = "UTF-8";
	private int keepAlive = 115;
	private String connection = "keep-alive";

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
			for (int i = 0; i < tmp_header.length; i++) {
				tmp_header[i].trim();
			}
			this.type = tmp_header[0];
			this.uri = tmp_header[1];
			this.protocol = tmp_header[2];
		} else {
			// k-v 报头信息解析
			String[] tmp_header = msg.split(":", 2);
			tmp_header[0].trim();
			tmp_header[1].trim();
		}
		return this;
	}

	/**
	 * 获取目标URI
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

	public String getScriptPath(){
		int index_dot = this.uri.indexOf(".");
		if(index_dot == -1){
			// 点不存在，即为声明MIME类型
			if(this.uri.endsWith("/") || this.uri.endsWith("\\")){
				// 检测URI字符串末尾是否写了斜杠
				return this.uri.trim() + "index.html";
			}else{
				return this.uri.trim() + "/index.html";
			}
		}else{
			// 检测到点号的位置，即有MIME类型声明
			int index_slash = this.uri.indexOf("/", index_dot);
			if(index_slash == -1){
				// 未检测到斜杠，即点号后所有字符为MIME类型声明
				return this.uri.trim();
			}else{
				// 已检测到斜杠，则截取斜杠前到点号部分所有内容为MIME类型
				return this.uri.substring(0, index_slash).trim();
			}
		}
	}
}
