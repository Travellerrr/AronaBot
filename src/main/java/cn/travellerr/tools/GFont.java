package cn.travellerr.tools;

import cn.travellerr.config.Config;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GFont {
    public static Font font;

    public static void init() {
        Config config = cn.travellerr.config.Config.INSTANCE;
        if (config.getUseLocalFont().isEmpty()) {
            try {
                InputStream fontStream = GFont.class.getResourceAsStream("/fonts/黑体.ttf");
                if (fontStream != null) {
                    font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                }
            } catch (FontFormatException | IOException e) {
                throw new RuntimeException(e);
            }
            font = font.deriveFont(Font.BOLD, 45);
            Log.info("字体 黑体.ttf 已加载");
        } else {
            try {
                File fontFile = new File(config.getUseLocalFont()).getAbsoluteFile();

                if (fontFile.isFile()) {
                    font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                } else {
                    Log.errorWithoutE("出现错误：" + config.getUseLocalFont() + "不是一个文件！");
                    return;
                }
            } catch (FontFormatException | IOException e) {
                throw new RuntimeException(e);
            }
            font = font.deriveFont(Font.BOLD, 45);
            Log.info("字体 自定义 已加载");
        }
    }
    }

