package cn.travellerr.websocket;

import cn.travellerr.tools.Log;

import java.io.File;
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
            File file = new File("./temp");
            if (!file.exists()) {
                boolean created = file.mkdirs();
                if (!created) {
                    Log.errorWithoutE("创建temp文件夹失败");
                } else {
                    Log.info("创建temp文件夹成功");
                }
            }
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
