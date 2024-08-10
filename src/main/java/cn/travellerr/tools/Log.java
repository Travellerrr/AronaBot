package cn.travellerr.tools;

import cn.travellerr.AronaBot;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.IOException;

public class Log {

    private static final MiraiLogger log = AronaBot.INSTANCE.getLogger();
    private static final String name = "阿洛娜杂项-";

    public static void info(Object msg) {
        log.info(name + msg);
    }

    public static void warning(Object msg) {
        log.warning(name + msg);
    }

    public static void error(Object msg, IOException e) {
        log.error(name + msg);
    }

    public static void error(Object msg) {
        log.error(name + msg);
    }

    public static void error(Object msg, Throwable e) {
        log.error(name + msg);
        log.error(name + e.getMessage(), e);
    }

    public static void error(Throwable e) {
        log.error(name + e.getMessage(), e);
    }

    public static void debug(Object msg) {
        log.debug(name + msg);
    }
}
