package cn.travellerr.BlueArchive;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class GetSentenceApi {
    static String fortuneSummary;
    static String signText;
    static String unSignText;
    static String luckyStar;

    @Deprecated(since = "已废弃")
    public static String get(){
        try {
            URL url = new URL("https://api.gumengya.com/Api/WaSentence");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);
            JsonObject data = jsonResponse.getAsJsonObject("data");
            return data.get("text").getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getApi(long qqNumber) {
        try {
            long EncryptedQQ = qqNumber + 1029384758;
            URL url = new URL("https://api.fanlisky.cn/api/qr-fortune/get/" + EncryptedQQ);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            JsonObject data = jsonObject.getAsJsonObject("data");
            fortuneSummary = data.get("fortuneSummary").getAsString();
            signText = data.get("signText").getAsString();
            unSignText = data.get("unSignText").getAsString();
            luckyStar = data.get("luckyStar").getAsString();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static String getText(String place) {
        if (Objects.equals(place, "fortuneSummary")) return fortuneSummary;
        if (Objects.equals(place, "signText")) return signText;
        if (Objects.equals(place, "unSignText")) return unSignText;
        return luckyStar;
    }
}
