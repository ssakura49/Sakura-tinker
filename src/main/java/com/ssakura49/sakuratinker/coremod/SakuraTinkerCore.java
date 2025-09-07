package com.ssakura49.sakuratinker.coremod;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public class SakuraTinkerCore {
    private static boolean initialized = false;
    public static PrintStream stream;
    private static final File logFile = createLogFile();

    /** 创建日志文件（支持开发 / 发布环境） */
    private static File createLogFile() {
        File file;
        try {
            if (isDevEnvironment()) {
                file = new File("logs/sakura_dev_log.txt");
            } else {
                file = new File("logs/sakura_tinker_log.txt");
            }

            file.getParentFile().mkdirs();
            if (file.exists()) file.delete();
            file.createNewFile();
            return file;
        } catch (IOException e) {
            System.err.println("[SakuraTinker] 无法创建日志文件，降级使用 System.err");
            e.printStackTrace();
            return null;
        }
    }

    /** 自动判断是否是开发环境（你可以根据实际情况替换） */
    private static boolean isDevEnvironment() {
        return System.getProperty("forge.development", "false").equals("true");
    }

    /** 可手动执行初始化（非必须，静态引用时会自动触发） */
    public static synchronized void execute() {
        if (!initialized) {
            try {
                stream = (logFile != null) ? new PrintStream(logFile) : System.err;
                stream.println("[SakuraTinker] 初始化日志流: " + new Date());
                stream.flush();
            } catch (Throwable throwable) {
                System.err.println("[SakuraTinker] 初始化日志流失败: fallback to System.err");
                stream = System.err;
                throwable.printStackTrace(stream);
            }
            initialized = true;
        }
    }

    /** 安全捕获异常，不会崩溃游戏 */
    public static void catchException(Throwable e) {
        e.printStackTrace(); // 控制台输出

        if (stream != null) {
            e.printStackTrace(stream);
            stream.flush();
        } else {
            System.err.println("[SakuraTinker] 日志流为空，fallback 到 System.err");
            e.printStackTrace(System.err);
        }

        throw new RuntimeException("[SakuraTinker] Critical Error", e);
    }

    static {
        execute();
    }
}
