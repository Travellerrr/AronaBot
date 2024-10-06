package cn.travellerr.BlueArchive;

import cn.chahuyun.hibernateplus.HibernateFactory;
import cn.hutool.core.date.DateUtil;
import cn.travellerr.entity.UserInfo;
import cn.travellerr.tools.Log;

import java.util.Date;
import java.util.Random;

public class SqlUtil {

    private static final int AllFortuneID = 402;

    public static UserInfo fortuneManager(long qqNumber, long botId) {

        if (isNewGen(qqNumber)) {
            int fortuneID = generateFortuneID(qqNumber, botId);
            if (!insertData(qqNumber, fortuneID)) {
                Log.debug("insert data failed");
                return null;
            }
        }
        return HibernateFactory.selectOne(UserInfo.class, qqNumber);
    }


    /**
     * 判断QQ号是否为新号
     *
     * @param qqNumber 用户QQ号
     * @return 布尔值，<code>true</code>为是
     */
    public static boolean isNewGen(long qqNumber) {
        UserInfo userInfo = HibernateFactory.selectOne(UserInfo.class, qqNumber);
        return (userInfo == null) || (!DateUtil.isSameDay(
                userInfo.getGenerateDate(),
                new Date()));
    }

    /**
     * 更新用户的运势ID
     *
     * @param qqNumber 用户QQ号
     * @param ID       运势ID
     */
    private static boolean insertData(long qqNumber, int ID) {
        UserInfo userInfo = HibernateFactory.selectOne(UserInfo.class, qqNumber);
        if (userInfo == null) {
            userInfo = UserInfo.builder()
                    .qq(qqNumber)
                    .build();
        }
        userInfo.setFortuneID(ID);
        userInfo.setGenerateDate(new Date());
        HibernateFactory.merge(userInfo);
        return true;
    }


    private static int generateFortuneID(long userId, long botId) {
        Random random = new Random(System.currentTimeMillis() + userId + botId);
        return random.nextInt(AllFortuneID) + 1;
    }
}
