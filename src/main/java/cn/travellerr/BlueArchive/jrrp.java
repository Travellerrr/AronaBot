package cn.travellerr.BlueArchive;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;

public class jrrp {
    public static void info(Contact subject, User user) {
        long qqNumber = user.getId();
        boolean isUserNew = GetSentenceApi.isQQNumberNew(qqNumber);
        boolean isDifferentDate = !isUserNew && GetSentenceApi.isDateDifferent(qqNumber);

        MessageChainBuilder message = new MessageChainBuilder();
        message.append(new At(qqNumber)).append("\n");

        if (!isDifferentDate && !isUserNew) {
            message.append("今日已查询,");
        } else {
            GetSentenceApi.generateFortuneID(qqNumber, false);
        }

        int jrrp = GetSentenceApi.getFortuneID(qqNumber) % 101;
        message.append("您的今日人品为:").append(String.valueOf(jrrp));
        subject.sendMessage(message.build());
    }
}
