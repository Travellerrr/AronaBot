package cn.travellerr.utils;

import cn.chahuyun.hibernateplus.Configuration;
import cn.chahuyun.hibernateplus.DriveType;
import cn.chahuyun.hibernateplus.HibernatePlusService;
import cn.travellerr.AronaBot;

import java.nio.file.Path;

import static cn.travellerr.AronaBot.sqlConfig;

/**
 * 说明
 *
 * @author Moyuyanli
 * @Description :hibernate
 * @Date 2022/7/30 22:47
 */
public class HibernateUtil {


    private HibernateUtil() {

    }

    /**
     * Hibernate初始化
     *
     * @param plugin 插件
     * @author Moyuyanli
     * @date 2022/7/30 23:04
     */
    public static void init(AronaBot plugin) {

        Configuration configuration = HibernatePlusService.createConfiguration(plugin.getClass());
        configuration.setPackageName("cn.travellerr.entity");

        DriveType dataType = sqlConfig.getDataType();
        configuration.setDriveType(dataType);
        Path dataFolderPath = plugin.getDataFolderPath();
        switch (dataType) {
            case MYSQL:
                configuration.setAddress(sqlConfig.getMysqlUrl());
                configuration.setUser(sqlConfig.getMysqlUser());
                configuration.setPassword(sqlConfig.getMysqlPassword());
                break;
            case H2:
                configuration.setAddress(dataFolderPath.resolve("AronaBot.h2").toString());
                break;
            case SQLITE:
                configuration.setAddress(dataFolderPath.resolve("AronaBot.db").toString());
                break;
        }

        HibernatePlusService.loadingService(configuration);

    }


}
