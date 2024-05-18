package cn.travellerr.event;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.BotJoinGroupEvent;
import net.mamoe.mirai.event.events.FriendAddEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;
import java.io.IOException;


/**
 * @author Travellerr
 * @implNote 实现加群自动发图片功能，私用模块，如要启用请更改下方信息
 * @since 2024/05/18
 */
public class Menu {
    public static void sendMenuToFriend(FriendAddEvent event) {

        // 启用QQ
        if (event.getBot().getId() == 896603204) {
            Contact subject = event.getUser();

            // 菜单图片路径（相对路径）
            File menu = new File("./data/image/menu/menu1.png");
            try (ExternalResource resource = ExternalResource.create(menu)) {
                Image image = subject.uploadImage(resource);

                // 发送消息
                subject.sendMessage(new PlainText(event.getUser().getNick()
                        + " Sensei您好！\n欢迎使用由 Travellerr 制作的 \"蔚蓝档案-阿洛娜\"机器人！\n请在使用之前仔细查看菜单！")
                        .plus(image));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void sendMenuToGroup(BotJoinGroupEvent event) {

        // 启用QQ
        if (event.getBot().getId() == 896603204) {
            Contact subject = event.getGroup();

            // 菜单图片路径（相对路径）
            File menu = new File("./data/image/menu/menu1.png");
            try (ExternalResource resource = ExternalResource.create(menu)) {
                Image image = subject.uploadImage(resource);

                // 发送消息
                subject.sendMessage(new PlainText("来自 "
                        + event.getGroup().getName()
                        + " 的Sensei们好！\n欢迎使用由 Travellerr 制作的 \"蔚蓝档案-阿洛娜\"机器人！\n请在使用之前仔细查看菜单！")
                        .plus(image));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
