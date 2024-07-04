package cn.travellerr.BlueArchive;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;

public class jrrp {
    public static void info(Contact subject, User user) {
        long qqNumber = user.getId();
        boolean isDifferentDate = GetSentenceApi.isDateDifferent(qqNumber);

        MessageChainBuilder message = new MessageChainBuilder();
        message.append(new At(qqNumber)).append("\n");
        int jrrp;
        if (!isDifferentDate) {
            message.append("今日已查询,");
        } else {
            GetSentenceApi.generateFortuneID(qqNumber, false);
        }
        jrrp = GetSentenceApi.getFortuneID(qqNumber) % 101;
        message.append("您的今日人品为:").append(String.valueOf(jrrp));
        subject.sendMessage(message.build());
    }
}
