package cn.travellerr;

import cn.travellerr.config.config;
import cn.travellerr.event.MessageEventListener;
import cn.travellerr.tools.GFont;
import cn.travellerr.tools.Log;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;

public final class AronaBot extends JavaPlugin {
    public static final AronaBot INSTANCE = new AronaBot();
    /*插件版本*/
    public static final String version = "1.0.1";

    public static config config;
    public static String ffmpeg = null;
    public static long startTime = System.currentTimeMillis();
    private AronaBot() {
        super(new JvmPluginDescriptionBuilder("cn.travellerr.AronaBot", version)
                .name("AronaBot")
                .info("阿洛娜机器人")
                .author("Travellerr")

                .build());
    }

    @Override
    public void onEnable() {
        EventChannel<Event> eventEventChannel = GlobalEventChannel.INSTANCE.parentScope(AronaBot.INSTANCE);

        reloadPluginConfig(cn.travellerr.config.config.INSTANCE);
        config = cn.travellerr.config.config.INSTANCE;
        GFont.init();

        ffmpeg = config.getFfmpegPath();
        if (!config.getUseSilk() && ffmpeg == null) {
            Log.errorWithoutE("你似乎没有安装SilkConverter插件，并且没有安装ffmpeg。语音合成功能已关闭");
            config.setUseVoice(false);
            reloadPluginConfig(cn.travellerr.config.config.INSTANCE);
        }
        eventEventChannel.registerListenerHost(new MessageEventListener());
        Log.info("插件已加载!");
    }

    @Override
    public void onDisable() {
        Log.info("插件已卸载!");
    }
}