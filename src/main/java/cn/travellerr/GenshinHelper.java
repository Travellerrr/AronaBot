package cn.travellerr;

import cn.travellerr.config.config;
import cn.travellerr.event.MessageEventListener;
import cn.travellerr.tools.Log;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;

public final class GenshinHelper extends JavaPlugin {
    public static final GenshinHelper INSTANCE = new GenshinHelper();
    /*插件版本*/
    public static final String version = "0.1.0";

    public static config config;

    private GenshinHelper() {
        super(new JvmPluginDescriptionBuilder("cn.travellerr.GenshinHelper", version)
                .name("GenshinHelper")
                .info("抽角色")
                .author("Travellerr")

                .build());
    }

    @Override
    public void onEnable() {
        EventChannel<Event> eventEventChannel = GlobalEventChannel.INSTANCE.parentScope(GenshinHelper.INSTANCE);

        reloadPluginConfig(cn.travellerr.config.config.INSTANCE);
        config = cn.travellerr.config.config.INSTANCE;
        eventEventChannel.registerListenerHost(new MessageEventListener());

        Log.info("插件已加载!");
    }

    @Override
    public void onDisable() {
        Log.info("插件已卸载!");
    }
}