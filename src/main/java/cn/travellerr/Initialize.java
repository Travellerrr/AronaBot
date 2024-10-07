package cn.travellerr;

import cn.chahuyun.hibernateplus.HibernateFactory;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.travellerr.BlueArchive.GetSentenceApi;
import cn.travellerr.entity.FortuneInfo;
import cn.travellerr.tools.Log;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.mamoe.mirai.console.MiraiConsole;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Initialize {
    public static void init() {
        Thread thread = new Thread(() -> {
            if (HibernateFactory.selectList(FortuneInfo.class).isEmpty()) {
                Log.warning("运势数据库中没有数据，正在初始化...");
                long initSqlStart = System.currentTimeMillis();
                jrysToSql();
                Log.warning("初始化完成！用时 " +
                        DateUtil.formatBetween(System.currentTimeMillis() - initSqlStart, BetweenFormatter.Level.MILLISECOND));
            }
        });

        thread.start();
    }

    private static void jrysToSql() {
        try (InputStream inputStream = GetSentenceApi.class.getClassLoader().getResourceAsStream("jrys/jrys.json");
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            for (String key : jsonObject.keySet()) {
                JsonArray jsonArray = jsonObject.getAsJsonArray(key);
                for (JsonElement element : jsonArray) {
                    JsonObject item = element.getAsJsonObject();

                    int id = Integer.parseInt(key);
                    String fortuneSummary = item.get("fortuneSummary").getAsString();
                    String luckyStar = item.get("luckyStar").getAsString();
                    String signText = item.get("signText").getAsString();
                    String unSignText = item.get("unSignText").getAsString();

                    // 将提取的信息存储到数据库中，这里假设调用存储方法saveToDatabase()
                    saveToDatabase(id, fortuneSummary, luckyStar, signText, unSignText);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("无法找到或读取jrys.json文件", e);
        }
    }

    private static void saveToDatabase(int id, String fortuneSummary, String luckyStar, String signText, String unSignText) {
        FortuneInfo fortuneInfo = FortuneInfo.builder()
                .id(id)
                .fortuneSummary(fortuneSummary)
                .luckyStar(luckyStar)
                .signText(signText)
                .unSignText(unSignText)
                .build();
        HibernateFactory.merge(fortuneInfo);
        // 在这里编写将信息存储到数据库的逻辑
    }
}
