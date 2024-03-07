package cn.travellerr.tools;

import java.io.IOException;
import java.io.InputStream;

import cn.travellerr.tools.Log;

import java.awt.*;

public class GFont {
    public static Font font;

    public static void init() {
        try {
            InputStream fontStream = GFont.class.getResourceAsStream("/fonts/黑体.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
                // 在这里可以处理异常，例如打印错误信息或执行其他操作
            }
            font = font.deriveFont(Font.BOLD, 45);
            Log.info("字体 黑体.ttf 已加载");
    }
}
