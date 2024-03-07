package cn.travellerr.tools;

import cn.travellerr.AronaBot;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.IOException;

public class Log {

    private static final MiraiLogger log = AronaBot.INSTANCE.getLogger();
    private static final String name = "原神帮助-";

    public static void info(String msg) {
        log.info(name + msg);
    }

    public static void warning(String msg) {
        log.warning(name + msg);
    }

    public static void error(String msg, IOException e) {
        log.error(name + msg);
    }
}
