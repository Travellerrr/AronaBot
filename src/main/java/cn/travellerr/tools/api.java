package cn.travellerr.tools;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.LightApp;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class api {
    public static void use(MessageEvent event) {
        try {
            Contact subject = event.getSubject();
            User user = event.getSender();
            Date date = new Date();
            //String msg = "现在是：" + date + " #markdown测试";
            String bt = "机器人指令";
            String subtitle = user.getNick();
            String yx = "菜单卡片";
            String encodedMsg = URLEncoder.encode(subtitle, StandardCharsets.UTF_8);
            String encodedBt = URLEncoder.encode(bt, StandardCharsets.UTF_8);
            String encodedYx = URLEncoder.encode(yx, StandardCharsets.UTF_8);
            //URL url = new URL("https://api.lolimi.cn/API/ark/w.php?msg="+encodedMsg+"&bt="+encodedBt+"&yx="+encodedYx);
            URL url = new URL("https://api.mrgnb.cn/API/qq_ark37.php?url=https://www.travellerr.cn/wp-content/uploads/2024/02/image-1708944958301.png&title=" + encodedBt + "&yx=" + encodedYx + "&subtitle=" + encodedMsg);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String apiResponse = response.toString();
            LightApp app = new LightApp(apiResponse);
            subject.sendMessage(app);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void chaiq(MessageEvent event) {
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
