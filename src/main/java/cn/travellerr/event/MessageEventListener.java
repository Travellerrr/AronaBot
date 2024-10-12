package cn.travellerr.event;

import cn.travellerr.AronaBot;
import cn.travellerr.tools.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.contact.BotIsBeingMutedException;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.MessageTooLargeException;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.EventCancelledException;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.QuoteReply;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.util.List;
import java.util.Random;


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

        String message = event.getMessage().serializeToMiraiCode();
        if (AronaBot.config.isAt()) {
            if (!message.contains("[mirai:at:" + event.getBot().getId() + "]")) {
                return;
            }
            message = message.split("]")[1].trim();
        }
        Contact subject = event.getSubject();
        QuoteReply quoteReply = new QuoteReply(event.getMessage());


        try (FileReader reader = new FileReader(AronaBot.INSTANCE.getDataFolder() + "/replyData.json")) {
            // 读取JSON文件
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);

            JsonObject normalObject = jsonObject.getAsJsonObject("Normal");
            // 获取对应键的值
            if (normalObject != null) {
                if (!handleMessages(normalObject, message, subject, quoteReply) && AronaBot.config.isR18()) {
                    JsonObject r18Object = jsonObject.getAsJsonObject("R18");
                    handleMessages(r18Object, message, subject, quoteReply);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean handleMessages(JsonObject jsonObject, String message, Contact subject, QuoteReply quoteReply) {
        JsonArray messageObject = jsonObject.getAsJsonArray(message);
        if (messageObject != null) {
            List<String> messages = new Gson().fromJson(messageObject, new TypeToken<List<String>>() {
            }.getType());
            String randomMessage = messages.get(new Random().nextInt(messages.size()));
            subject.sendMessage(quoteReply.plus(randomMessage));
            return true;
        }
        return false;
    }
}

