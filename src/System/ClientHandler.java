package System;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 客户端线程类
 * 
 * @author Richard Lea
 */
public class ClientHandler implements Runnable {

	/**
	 * 当前连接线程 SOCKET
	 */
	private Socket client;
	/**
	 * 当前线程的字符输出流
	 */
	private PrintWriter writer;
	/**
	 * 当前线程的字节输出流
	 */
	private BufferedOutputStream outputStream;
	/**
	 * 当前线程的字符输入流
	 */
	private BufferedReader reader;
	/**
	 * 当前线程的字节输入流
	 */
	private InputStreamReader input;
	/**
	 * 当前线程的字节输出流
	 */
	private OutputStreamWriter output;
	/**
	 * 当前线程的输出报头
	 */
	private ArrayList<String> outputHeader;
	/**
	 * 当前线程的输入报头
	 */
	private Header header;

	/**
	 * 当前线程的输出内容
	 */
	private StringBuilder contentData;

	public PrintWriter getWriter() {
		return writer;
	}

	public StringBuilder getContentData() {
		return contentData;
	}

	public BufferedReader getReader() {
		return reader;
	}

	public ArrayList<String> getOutputHeader() {
		return outputHeader;
	}

	public Header getHeader() {
		return header;
	}

	/**
	 * 传入客户端连接 SOCKET 构造
	 * 
	 * @param client
	 */
	public ClientHandler(Socket client) {
		this.client = client;
		try {
			InputStream in = this.client.getInputStream();
			OutputStream out = this.client.getOutputStream();
			this.input = new InputStreamReader(in);
			this.output = new OutputStreamWriter(out);
			this.reader = new BufferedReader(this.input);
			this.writer = new PrintWriter(this.output);
			this.header = new Header();
			this.outputHeader = new ArrayList<String>();
			this.outputStream = new BufferedOutputStream(out);
		} catch (IOException ex) {
			Logger.getLogger(ClientHandler.class.getName())
			.log(Level.SEVERE, "I/O 控制出错", ex);
		}
	}

	public Socket getClient() {
		return client;
	}

	@Override
	public void run() {
		System.out.println("开始接收HTTP报头请求");
		// 开始读取HTTP请求报头
		String str = null;
		try {
			while (true) {
				str = this.reader.readLine();
				System.out.println(str);
				// 结束循环
				if (str.isEmpty()) {
					break;
				}
				this.header.push(str);
			}
		} catch (IOException ex) {
			Logger.getLogger(ClientHandler.class.getName())
			.log(Level.SEVERE, "访问请求读取出错", ex);
		}
		// 开始获取目标文件内容
		FileHandler outputFile = new FileHandler(Settings.DEFAULT_PATH + this.header.getScriptPath());
		// 开始输出响应报头
		System.out.println("开始输出响应报头");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss", Locale.US);
		this.writer.println("HTTP/1.1 200 OK");
		this.writer.println("Date: " + dateFormat.format(new Date()) + " GMT");
		this.writer.println("Server: CHIGI-SERVER (JRE) PHP/5.4.17");
		this.writer.println("Content-Length: " + outputFile.lengthByte());
		this.writer.println("Connection: keep-alive");
		this.writer.println("Keep-Alive: timeout=5, max=99");
		if(outputFile.getError() == null){
			// 当前文件读取没有错误
			this.writer.println("Content-Type: "+"text/html"+";\n");
		}else{
			// 输出系统级错误
			this.writer.println("Content-Type: text/html;charset=UTF-8\n");
		}
		// 开始输出内容
		this.writer.flush();
		// this.writer.println(this.outputFile.toString());
		try {
			if(outputFile.getError() != null){
				this.writer.println(outputFile.getError());
				this.writer.flush();
			}else{
				for (int tmp_d : outputFile.getData()) {
					this.outputStream.write(tmp_d);
				}
				this.outputStream.flush();
			}
			// 结束本次客户端socket
			this.writer.close();
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
