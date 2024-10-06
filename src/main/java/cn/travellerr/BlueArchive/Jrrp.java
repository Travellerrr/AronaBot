package cn.travellerr.BlueArchive;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.util.Objects;

public class Jrrp {
    public static void info(Contact subject, User user) {
        long qqNumber = user.getId();
        long botId = subject.getBot().getId();
        boolean isNewGen = SqlUtil.isNewGen(qqNumber);

        MessageChainBuilder message = new MessageChainBuilder();
        message.append(new At(qqNumber)).append("\n");

        if (!isNewGen) {
            message.append("今日已查询,");
        }

        int jrrp = Objects.requireNonNull(SqlUtil.fortuneManager(qqNumber, botId)).getFortuneID() % 101;
        message.append("您的今日人品为:").append(String.valueOf(jrrp));
        subject.sendMessage(message.build());
    }
}
