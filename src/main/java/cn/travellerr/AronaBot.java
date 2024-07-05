package cn.travellerr;

import cn.travellerr.command.RegCommand;
import cn.travellerr.config.Config;
import cn.travellerr.config.VoiceBlackList;
import cn.travellerr.event.Menu;
import cn.travellerr.tools.GFont;
import cn.travellerr.tools.Log;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendAddEvent;
import net.mamoe.mirai.event.events.MemberJoinEvent;

public final class AronaBot extends JavaPlugin {
    public static final AronaBot INSTANCE = new AronaBot();
    /*插件版本*/
    public static final String version = "1.1.1";

    public static Config config;
    public static VoiceBlackList blackList;
    public static String ffmpeg = null;
    public static long startTime = System.currentTimeMillis();
    private AronaBot() {
        super(new JvmPluginDescriptionBuilder("cn.travellerr.AronaBot", version)
                .name("AronaBot")
                .info("蔚蓝档案额外功能插件")
                .author("Travellerr")

                .build());
    }

    @Override
    public void onEnable() {
        //EventChannel<Event> eventEventChannel = GlobalEventChannel.INSTANCE.parentScope(AronaBot.INSTANCE);

        reloadPluginConfig(cn.travellerr.config.Config.INSTANCE);
        reloadPluginConfig(VoiceBlackList.INSTANCE);
        config = cn.travellerr.config.Config.INSTANCE;
        blackList = VoiceBlackList.INSTANCE;

        RegCommand regCommand = RegCommand.INSTANCE;
        regCommand.register();

        GFont.init();

        ffmpeg = config.getFfmpegPath();
        if (!config.getUseSilk() && ffmpeg == null) {
            Log.errorWithoutE("你似乎没有安装SilkConverter插件，并且没有安装ffmpeg。语音合成功能已关闭");
            config.setUseVoice(false);
            reloadPluginConfig(cn.travellerr.config.Config.INSTANCE);
        }
        //eventEventChannel.registerListenerHost(new MessageEventListener());





        // 私用模块，请修改后使用
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendAddEvent.class, Menu::sendMenuToFriend);
        GlobalEventChannel.INSTANCE.subscribeAlways(MemberJoinEvent.class, memberJoinEvent -> {
            if (memberJoinEvent.getMember().getId() == memberJoinEvent.getBot().getId()) {
                Menu.sendMenuToGroup(memberJoinEvent);
            }
        });




        Log.info("插件已加载!");


    }

    @Override
    public void onDisable() {
        Log.info("插件已卸载!");
    }
}