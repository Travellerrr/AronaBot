package cn.travellerr.tools;

import cn.hutool.core.io.FileUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static cn.travellerr.AronaBot.config;

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

            try (InputStream stream = url.openStream()) {
                if (config.getDebug()) { // 文件类型测试
                    File dictionary = new File("./aronaBotDebug");
                    if (!dictionary.exists()) {
                        if (dictionary.mkdir()) {
                            Log.debug("debug文件夹创建成功");
                        } else {
                            Log.error("debug文件夹创建失败");
                        }
                    }
                    long time = System.currentTimeMillis();
                    File tempFile = File.createTempFile("tempFile", null);

                    try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile))) {
                        outputStream.write(stream.readAllBytes());
                    }

                    URLConnection connection = tempFile.toURI().toURL().openConnection();
                    String mimeType = connection.getContentType();
                    Log.debug("文件类型: " + mimeType);
                    String[] suf = mimeType.split("/");
                    String suffix = suf[suf.length - 1];
                    File finalFile = new File("./aronaBotDebug/" + time + "file." + suffix);
                    FileUtil.copy(tempFile, finalFile, true);
                }

                try (ExternalResource resource = ExternalResource.create(stream)) {
                    Image image = subject.uploadImage(resource);
                    subject.sendMessage(new At(user.getId()).plus(image));
                }
            }
        } catch (Exception e) {
            subject.sendMessage(new At(user.getId()).plus("\n发送图片失败！请检查控制台输出！\n原因: ").plus(e.getMessage()));
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
