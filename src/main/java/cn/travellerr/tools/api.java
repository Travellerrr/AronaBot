package cn.travellerr.tools;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;

import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class api {

    /*public static void chaiq(MessageEvent event) {
        try {
            Contact subject = event.getSubject();
            User user = event.getSender();
            URL url = new URL("https://api.lolimi.cn/API/chaiq/c.php");
            ExternalResource resource = ExternalResource.create(url.openStream());
            Image image = subject.uploadImage(resource);
            subject.sendMessage(new At(user.getId()).plus(image));
            resource.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    public static void chaiq(Contact subject, User user) {
        try {
            URL url = new URL("https://api.lolimi.cn/API/chaiq/c.php");
            ExternalResource resource = ExternalResource.create(url.openStream());
            Image image = subject.uploadImage(resource);
            subject.sendMessage(new At(user.getId()).plus(image));
            resource.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void draw(MessageEvent event) {
        try {
            Contact subject = event.getSubject();
            User user = event.getSender();
            subject.sendMessage(new At(user.getId()).plus("绘制中，请稍等"));
            MessageChain message = event.getMessage();
            String code = message.serializeToMiraiCode();
            //Log.info(code);
            String[] name = code.split("画 ");
            Log.info(name[name.length - 1]);

            String text = URLEncoder.encode(name[name.length - 1], StandardCharsets.UTF_8);
            String headUrl = "https://ai.cloudroo.top/draw/?t=" + text;
            URL url = new URL(headUrl);
            ExternalResource resource = ExternalResource.create(url.openStream());
            Image image = subject.uploadImage(resource);
            subject.sendMessage(new At(user.getId()).plus(image));
            resource.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
