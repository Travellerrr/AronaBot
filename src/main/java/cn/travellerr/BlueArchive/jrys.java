package cn.travellerr.BlueArchive;

import cn.travellerr.tools.Log;
import cn.travellerr.tools.GFont;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class jrys {
    public static void info(MessageEvent event) {
        Contact subject = event.getSubject();
        MessageChain messages = event.getMessage();


        MessageChainBuilder messagesChain = new MessageChainBuilder().append(new QuoteReply(messages));

        try {

            Random rand = new Random();
            int randNum = rand.nextInt(3);
            // 读取背景图片和覆盖图片
            ClassLoader classLoader = jrys.class.getClassLoader();
            BufferedImage background = ImageIO.read(classLoader.getResourceAsStream("jrys/千禧年/bg/游戏开发部.png"));
            BufferedImage cover = ImageIO.read(classLoader.getResourceAsStream("jrys/千禧年/character/" + randNum + ".png"));
            int newWidth = (int)(cover.getWidth() /1.6); // 缩小为原来的一半
            int newHeight = (int)(cover.getHeight() / 1.6);

            BufferedImage resizedCover = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Image scaledCover = cover.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            Graphics2D g2d = resizedCover.createGraphics();
            g2d.drawImage(scaledCover, 0, 0, null);
            g2d.dispose();

            // 创建一个新的BufferedImage对象，大小为背景图片的大小
            BufferedImage combined = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);

            // 将背景图片绘制到新的BufferedImage对象中
            Graphics2D g = combined.createGraphics();
            g.drawImage(background, 0, 0, null);

            //绘制半透明遮盖
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g.setComposite(alphaComposite);
            g.setColor(Color.WHITE);
            g.fillRect(20,20 , combined.getWidth()-40, combined.getHeight() - 40);
            alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
            g.setComposite(alphaComposite);

            // 将覆盖图片叠加到背景图片上
            int coverX = 600 - resizedCover.getWidth() / 2; // 计算要绘制的图片在背景图片中的 X 坐标，使其中心点的 x 坐标为 300
            g.drawImage(resizedCover, coverX, 100, null);

            // 绘制签底
            g.setColor(Color.red);
            g.fillRect(100,100,300,600);
            g.setColor(Color.white);
            g.fillRect(105,105,290,590);
            g.setColor(Color.red);
            g.fillRect(110,110,280,580);
            g.setColor(Color.WHITE);
            g.fillRect(115,115,270,570);
            g.setColor(Color.red);
            g.fillRect(115,115,270,75);

            //设置字体并绘制
            Font font = GFont.font;
            g.setFont(font);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            String text = jrys();
            int stringWidth = g.getFontMetrics().stringWidth(text);
            int x =(495 - stringWidth) /2;
            g.drawString(text, x, 170);
            String message = GetSentenceApi.get();

            font = font.deriveFont(30f);
            g.setFont(font);
            g.setColor(Color.black);
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int fontHeight = fontMetrics.getHeight()*2;
            int msgY = 250;
            int msgX = 310;
            for(int msgLength = 0; msgLength < message.length()-1; msgLength +=1) {
                g.drawString(String.valueOf(message.charAt(msgLength)), msgX, msgY);
                msgY += fontHeight;
                if(msgY > 650) {
                    msgY = 250;
                    msgX -= 36;
                }
            }
            font = font.deriveFont(20f);
            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("BOT阿洛娜&Travellerr", 550, 795);
            // 保存合成后的图片
            //ImageIO.write(combined, "PNG", new File("combined.png"));
            g.dispose();
            sendImage(combined, subject, messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendImage(Image image, Contact subject, MessageChain messages) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) image, "png", stream);
        } catch (IOException e) {
            Log.error("签到管理:签到图片发送错误!", e);
            subject.sendMessage(messages);
            return;
        }
        Contact.sendImage(subject, new ByteArrayInputStream(stream.toByteArray()));
    }

    // 生成随机数获取运势
    public static String jrys() {
        Random rand = new Random();
        int randnum = rand.nextInt(101);
        if(randnum < 18) return "凶";
        else if (randnum <53) return "末吉";
        else if (randnum <58) return "末小吉";
        else if (randnum< 62) return "小吉";
        else if (randnum <65) return  "半吉";
        else if (randnum < 71) return  "吉";
        else if (randnum < 85) return "大吉";
        else return "超大吉";
    }
}