package cn.travellerr.websocket;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class downloadVoice {
    public static long tempId;

    public static void downloadFromUrl(String urlS, String modelUrl) {
        String realUrl = "https://" + modelUrl + "/file=" + urlS;
        try {
            URL url = new URL(realUrl);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            tempId = System.currentTimeMillis();
            FileOutputStream outputStream = new FileOutputStream("./temp/" + tempId + ".wav");

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
