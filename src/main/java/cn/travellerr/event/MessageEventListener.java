package cn.travellerr.event;

import cn.travellerr.BlueArchive.jrys;
import cn.travellerr.config.config;
import cn.travellerr.tools.Log;
import cn.travellerr.tools.SecurityNew;
import cn.travellerr.tools.api;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;


public class MessageEventListener extends SimpleListenerHost {
    @EventHandler()
    public void onMessage(@NotNull MessageEvent event) {
        config config = cn.travellerr.config.config.INSTANCE;
        User sender = event.getSender();
        boolean owner = config.getOwner() == sender.getId();
        String prefix = config.getPrefix();
        Contact subject = event.getSubject();
        String msg = event.getMessage().serializeToMiraiCode();
        if (msg.startsWith(prefix)) {
            msg = msg.substring(1);
            switch (msg) {
                case "测试":
                    return;
                /*case "原神角色列表":
                    Contact.sendImage(subject, new File("./data/cn.travellerr.GenshinHelper/GenshinHelp/角色列表/info.png"));
                    return;*/
                case "监控":
                case "状态":
                    SecurityNew.Security(event);
                    return;
                case "卡片":
                    api.use(event);
                    return;
                case "随机柴郡":
                    api.chaiq(event);
                    return;
                case "今日运势":
                case "jrys":
                    jrys.info(event);
                    Log.info("运势指令");
                    return;
            }
            /*String info = "原神攻略 (\\S+)";
            if (Pattern.matches(info, msg)) {
                Log.info("攻略指令");
                CharacterHelp.help(event);
                return;
            }
            String draw = "画\\s+(\\S+\\s*)*";
            if (Pattern.matches(draw, msg)) {
                Log.info("攻略指令");
                api.draw(event);
                return;
            }*/

        }
    }
}