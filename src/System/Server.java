package System;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 服务器总类
 *
 * @author Richard Lea
 */
public class Server implements Runnable{

    private ExecutorService threadPool;
    private ServerSocket server;

    /**
     * 创建服务器对象
     */
    public Server() {
        Settings.SERVER = this;
    }

    public ServerSocket getServer() {
        return server;
    }

    /**
     * 启动服务器（可指定线程数）
     */
    @Override
    public void run() {
        Settings.init();
        ConsoleHandler console = new ConsoleHandler();
        this.threadPool = Executors.newCachedThreadPool();
        try {
            this.server = new ServerSocket(Settings.PORT);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName())
                    .log(Level.SEVERE, String.valueOf(Settings.PORT) + "端口已被其他进程占用，请检查", ex);
            if (Settings.STATUS == 1) {
                this.stop();
            }
            return;
        }
        console.log("千木服务器进程启动");
        console.print();
        System.out.print("Chigi>");
        Settings.STATUS = 1;
        while (true) {
            if (Settings.STATUS == 0) {
                // 若当前服务器已停止，则跳出循环
                break;
            }
            Socket socket = null;
            try {
                socket = this.server.accept();
            } catch (IOException ex) {
                // 判断当前服务器进程是否仍在运行
                // 仅在运行期间才抛出读取异常
                if (Settings.STATUS == 1) {
                    Logger.getLogger(Server.class.getName())
                            .log(Level.SEVERE, "请求读取出错", ex);
                }
                return ;
            }
            this.threadPool.execute(new ClientHandler(socket));
        }
    }

    /**
     * 暂停服务器进程
     */
    public void stop() {
        if (Settings.STATUS == 0) {
            System.out.println("当前服务器进程已停止。");
            return;
        }
        Settings.STATUS = 0;
        this.threadPool.shutdown();
        System.out.println("所有线程将在60秒内全部结束");
        try {
            if (!this.threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                this.threadPool.shutdownNow();
                if (!this.threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.out.println("线程池关闭失败");
                }
            }
        } catch (InterruptedException ex) {
            this.threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        try {
            this.server.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "当前服务器已关闭", ex);
        }
        this.server = null;
        System.gc();
        System.out.println("【~~千木服务器进程停止~~】");
        Settings.STATUS = 0;
    }
}
