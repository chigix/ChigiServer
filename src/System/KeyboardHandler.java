package System;

import java.util.Scanner;

/**
 * 键盘输入线程类
 *
 * @author Richard Lea
 *
 */
public class KeyboardHandler implements Runnable {

    @Override
    public void run() {
        Scanner input = new Scanner(System.in);
        Thread thread = null;
        while (true) {
            String cmd = input.next();
            switch (cmd.toLowerCase()) {
                case "exit":
                    System.out.println("服务器运行结束\n退出");
                    System.exit(0);
                    break;
                case "start":
                    if (Settings.STATUS == 1) {
                        System.out.println("当前已有一个正在运行的千木服务器进程。");
                    }else{
                        thread = new Thread(Settings.SERVER);
                        thread.start();
                    }
                    break;
                case "stop":
                    if (Settings.STATUS == 0) {
                        System.out.println("当前服务器进程已停止。");
                    }else{
                        Settings.SERVER.stop();
                    }
                    break;
                case "restart":
                    Settings.SERVER.stop();
                    thread = new Thread(Settings.SERVER);
                    thread.start();
                    break;
                case "help":
                    KeyboardHandler.showHelp();
                    break;
            }
        }
    }

    public static void showHelp() {
        System.out.println("=======千木服务器命令操作帮助=======");
        System.out.println(" help：   帮助命令，调出命令操作帮助");
        System.out.println("          列表。");
        System.out.println(" exit：   退出命令，可直接结束当前服");
        System.out.println("          运行进程。");
        System.out.println(" start：  启动服务器。");
        System.out.println(" stop：   暂停服务器进程。");
        System.out.println(" restart：重启服务器。");
        System.out.println("================================");
    }
}
