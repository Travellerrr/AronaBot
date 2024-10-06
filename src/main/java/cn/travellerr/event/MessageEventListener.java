package cn.travellerr.event;

import cn.travellerr.config.Config;
import cn.travellerr.tools.Log;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.BotIsBeingMutedException;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.MessageTooLargeException;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.EventCancelledException;
import net.mamoe.mirai.event.events.MessageEvent;
import org.jetbrains.annotations.NotNull;


@Deprecated(since = "加入MCL指令系统")
public class MessageEventListener extends SimpleListenerHost {

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        if (exception instanceof EventCancelledException) {
            Log.error("发送消息被取消:", exception);
        } else if (exception instanceof BotIsBeingMutedException) {
            Log.error("你的机器人被禁言:", exception);
        } else if (exception instanceof MessageTooLargeException) {
            Log.error("发送消息过长:", exception);
        } else if (exception instanceof IllegalArgumentException) {
            Log.error("发送消息为空:", exception);
        }

        // 处理事件处理时抛出的异常
        Log.error(exception);
    }
    @EventHandler()
    public void onMessage(@NotNull MessageEvent event) {
        Config config = cn.travellerr.config.Config.INSTANCE;
        User sender = event.getSender();
        Contact subject = event.getSubject();
        String msg = event.getMessage().serializeToMiraiCode();
        String url = config.getUrl();
        boolean useSilk = config.getUseSilk();
        /*if (msg.startsWith(prefix)) {
            msg = msg.substring(1);
            switch (msg) {
                case "测试":
                    return;
                case "原神角色列表":
                    Contact.sendImage(subject, new File("./data/cn.travellerr.GenshinHelper/GenshinHelp/角色列表/info.png"));
                    return;
                case "监控":
                case "状态":
                    if (owner || config.getUser().contains(sender.getId())) {
                        Log.info("监控指令");
                        SecurityNew.Security(event);
                    } else {
                        Log.warning("权限不足");
                    }
                    return;
                case "卡片":
                    Api.use(event);
                    return;
                case "随机柴郡":
                    Log.info("表情包指令");
                    Api.chaiq(event);
                    return;
                case "今日运势":
                case "Jrys":
                    Log.info("运势指令");
                    Jrys.info(event);
                    return;
            }
            String info = "原神攻略 (\\S+)";
            if (Pattern.matches(info, msg)) {
                Log.info("攻略指令");
                CharacterHelp.help(event);
                return;
            }
            String draw = "画\\s+(\\S+\\s*)*";
            if (Pattern.matches(draw, msg)) {
                Log.info("攻略指令");
                Api.draw(event);
                return;
            }*/
            /*if (config.getUseVoice()) {

                String makeWithLang = "^(\\S+)说 .* (.*日.*|.*中.*|.*英.*)$";
                if (Pattern.matches(makeWithLang, msg)) {
                    Log.info("其他语音生成");
                    VoiceGet.make(event, true, url, useSilk);
                    return;
                }

                String make = "^(\\S+)说 .*$";
                if (Pattern.matches(make, msg)) {
                    Log.info("中文语音生成");
                    VoiceGet.make(event, false, url, useSilk);
                    return;
                }*/


                /*String BALogo = "balogo (\\S+) (\\S+)";
                if (Pattern.matches(BALogo, msg)) {
                    Log.info("BA标题生成");
                    VoiceGet.make(event, true, url, useSilk);
                    return;
                }*/
            }
        }

