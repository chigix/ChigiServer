package System;

/**
 * 服务器进程启动入口
 * @author Richard Lea
 */
public class Main {
    public static void main(String[] args) {
        // 初始化服务器配置项
        Settings.init();
        // 启动服务器
        Server server = new Server();
        Thread keyboard = new Thread(new KeyboardHandler());
        System.out.println("================================");
        System.out.println("==     欢迎使用千木WEB服务器     ==");
        System.out.println("================================");
        KeyboardHandler.showHelp();
        keyboard.start();
        if (Settings.AUTO_START) {
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}
