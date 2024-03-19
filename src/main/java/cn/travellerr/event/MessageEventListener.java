package cn.travellerr.event;

import cn.travellerr.BlueArchive.jrys;
import cn.travellerr.GehshinHelp.CharacterHelp;
import cn.travellerr.tools.Log;
import cn.travellerr.tools.Security;
import cn.travellerr.tools.SecurityNew;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.regex.Pattern;

public class MessageEventListener extends SimpleListenerHost {
    @EventHandler()
    public void onMessage(@NotNull MessageEvent event) {

        Contact subject = event.getSubject();
        String msg = event.getMessage().serializeToMiraiCode();

        switch (msg) {
            case "测试":
                return;
            case "帮助":
                subject.sendMessage("测试");
                return;
            case "#原神角色列表":
                Contact.sendImage(subject, new File("./data/cn.travellerr.GenshinHelper/GenshinHelp/角色列表/info.png"));
                return;
            case "#监控":
                Security.info(event);
                return;
            case "#新监控":
                SecurityNew.Security(event);
        }
        String info = "#原神攻略 (\\S+)";
        if (Pattern.matches(info, msg)) {
            Log.info("攻略指令");
            CharacterHelp.help(event);
        }


    }

    @EventHandler
    public void onGroupMessgae(@NotNull GroupMessageEvent event) {
        Contact subject = event.getSubject();
        String msg = event.getMessage().serializeToMiraiCode();
        switch (msg) {
            case "#今日运势":
            case "#jrys":
                jrys.info(event);
                Log.info("运势指令");
        }
    }
}