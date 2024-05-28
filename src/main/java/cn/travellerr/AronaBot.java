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
import net.mamoe.mirai.event.events.BotJoinGroupEvent;
import net.mamoe.mirai.event.events.FriendAddEvent;
import net.mamoe.mirai.event.events.GroupTalkativeChangeEvent;
import net.mamoe.mirai.event.events.MemberLeaveEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;

public final class AronaBot extends JavaPlugin {
    public static final AronaBot INSTANCE = new AronaBot();
    /*插件版本*/
    public static final String version = "1.0.5";

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


        /*
         *  Author: tsudzuki
         *  下方代码取于 开源项目 https://github.com/LaoLittle/AutoGroup
         *  若侵权请联系我删除
         *  可选择开启/不开启
         */
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupTalkativeChangeEvent.class, event -> {
            if (config.getUseGroupDragon()) {
                try {
                    if (event.getPrevious().getId() == event.getBot().getId()) {
                        event.getGroup().sendMessage("我的龙王被抢走了...");
                        Thread.sleep(2000);
                        event.getGroup().sendMessage(new PlainText("呜呜呜...").plus(new At(event.getNow().getId())).plus(new PlainText(" 你还我龙王！！！")));
                        Thread.sleep(3000);
                        event.getNow().sendMessage("还给我还给我还给我还给我还给我");
                    } else
                        event.getGroup().sendMessage(new At(event.getPrevious().getId()).plus(new PlainText(" 的龙王被").plus(new At(event.getNow().getId()).plus(new PlainText(" 抢走了，好可怜")))));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        /*
         *  Author: tsudzuki
         *  下方代码取于 开源项目 https://github.com/LaoLittle/AutoGroup
         *  若侵权请联系我删除
         *  可选择开启/不开启
         */
        GlobalEventChannel.INSTANCE.subscribeAlways(MemberLeaveEvent.Quit.class, event -> {
            if (config.getUseGroupLeave()) {
                event.getGroup().sendMessage(
                        new PlainText(event.getUser().getNick() + "(" + event.getUser().getId() + ")" + " 悄悄的退群了……")
                );
            }
        });

        GlobalEventChannel.INSTANCE.subscribeAlways(MemberLeaveEvent.Kick.class, event -> {
            if (event.getOperator() != null && config.getUseGroupLeave()) {
                event.getGroup().sendMessage(
                        new PlainText(event.getUser().getNick()) + "(" + event.getUser().getId() + ") 被管理员 " + event.getOperator().getNick() + " 踢了……"
                );
            }
        });


        // 私用模块，请修改后使用
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendAddEvent.class, Menu::sendMenuToFriend);
        GlobalEventChannel.INSTANCE.subscribeAlways(BotJoinGroupEvent.class, Menu::sendMenuToGroup);




        Log.info("插件已加载!");
    }

    @Override
    public void onDisable() {
        Log.info("插件已卸载!");
    }
}