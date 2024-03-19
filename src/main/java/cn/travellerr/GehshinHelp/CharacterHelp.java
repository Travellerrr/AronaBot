package cn.travellerr.GehshinHelp;

import cn.travellerr.tools.Log;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import java.io.File;

public class CharacterHelp {
    public static void help(MessageEvent event) {
        Contact subject = event.getSubject();
        MessageChain message = event.getMessage();
        String code = message.serializeToMiraiCode();
        Log.info(code);
        String[] name = code.split(" ");
        Log.info(name[name.length - 1]);
        info(subject, name[name.length - 1]);
    }

    public static void info(Contact subject, String name) {
        // 发送图像
        String imgPath = "./data/cn.travellerr.GenshinHelper/GenshinHelp/角色/";
        File directory = new File(imgPath).getParentFile();
        if (!directory.exists()) {
            subject.sendMessage("错误：data文件夹为空");
        }
        File imageFile = new File(imgPath + name + "/info.png");
        if (imageFile.exists()) {
            Contact.sendImage(subject, imageFile);
        } else {
            subject.sendMessage("错误：没有该角色");
        }
    }

}