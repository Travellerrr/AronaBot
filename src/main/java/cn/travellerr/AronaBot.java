package cn.travellerr;

import cn.travellerr.command.RegCommand;
import cn.travellerr.config.Config;
import cn.travellerr.config.SqlConfig;
import cn.travellerr.config.VoiceBlackList;
import cn.travellerr.event.Menu;
import cn.travellerr.tools.GFont;
import cn.travellerr.tools.Log;
import cn.travellerr.utils.HibernateUtil;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotJoinGroupEvent;
import net.mamoe.mirai.event.events.FriendAddEvent;
import net.mamoe.mirai.event.events.FriendMessagePostSendEvent;
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent;

public final class AronaBot extends JavaPlugin {
    public static final AronaBot INSTANCE = new AronaBot();
    /*插件版本*/
    public static final String version = "2.0.0";

    public static Config config;
    public static VoiceBlackList blackList;
    public static SqlConfig sqlConfig;

    public static String ffmpeg = null;
    public static long startTime = System.currentTimeMillis();


    public static long sendGroupMsgNum = 0;
    public static long sendFriendMsgNum = 0;

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
        reloadPluginConfig(SqlConfig.INSTANCE);
        config = cn.travellerr.config.Config.INSTANCE;
        blackList = VoiceBlackList.INSTANCE;
        sqlConfig = SqlConfig.INSTANCE;

        RegCommand regCommand = RegCommand.INSTANCE;
        regCommand.register();

        GFont.init();
        HibernateUtil.init(this);
        Initialize.init();

        ffmpeg = config.getFfmpegPath();
        if (!config.getUseSilk() && ffmpeg == null) {
            Log.error("你似乎没有安装SilkConverter插件，并且没有安装ffmpeg。语音合成功能已关闭");
            config.setUseVoice(false);
            reloadPluginConfig(cn.travellerr.config.Config.INSTANCE);
        }

        // 私用模块，请修改后使用
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendAddEvent.class, Menu::sendMenuToFriend);
        GlobalEventChannel.INSTANCE.subscribeAlways(BotJoinGroupEvent.class, Menu::sendMenuToGroup);

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessagePostSendEvent.class, sendGroupMsgEvent -> sendGroupMsgNum++);
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessagePostSendEvent.class, sendFriendMsgEvent -> sendFriendMsgNum++);

        Log.info("插件已加载!");
    }

    @Override
    public void onDisable() {
        Log.info("插件已卸载!");
    }
}