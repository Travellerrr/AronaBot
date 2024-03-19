package cn.travellerr.BlueArchive;

import cn.travellerr.tools.GFont;
import cn.travellerr.tools.Log;
import net.mamoe.mirai.contact.AvatarSpec;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class jrys {
    private static int index = 1;
    public static Map<String, String> getMatcher(@NotNull GroupMessageEvent event) {
        Map<String, String> map = new HashMap<>();
        String text = event.getMessage().get(0).toString();
        String regex = "mirai:source:ids=\\[(.*?)], internalIds=\\[(.*?)], from group (.*?) to (.*?) at (.*?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            map.put("源ID", matcher.group(1));
            map.put("内部ID", matcher.group(2));
            map.put("来源QQ号", matcher.group(3));
            map.put("目标组ID", matcher.group(4));
            map.put("时间戳", matcher.group(5));
        }
        return map;
    }

    public static void info(GroupMessageEvent event) {
        Contact subject = event.getSubject();
        User sender = event.getSender();
        Map<String, String> matcher = getMatcher(event);
        long fromQQ = Long.parseLong(matcher.get("来源QQ号"));
        subject.sendMessage(new At(fromQQ).plus("\nSensei请稍等！阿洛娜这就为您抽签！"));

        try {
            int schoolNum = 3;  //学校数量
            int clubNum = 4;  //默认社团数量
            int picNum = 4;  //默认学员数量
            int club = 0; //默认社团
            Random rand = new Random();
            int school = rand.nextInt(schoolNum);
            if (school == 1) {
                rand = new Random();
                club = rand.nextInt(clubNum);
            }
            if (school == 1) {
                if (club == 1) picNum = 5;
                if (club == 3) picNum = 3;
            } else if (school == 0) {
                picNum = 5;
            }
            Log.info("School: " + school);
            Log.info("club: " + club);
            Log.info("picNum: " + picNum);
            // 读取背景图片和覆盖图片
            ClassLoader classLoader = jrys.class.getClassLoader();
            BufferedImage background = ImageIO.read(classLoader.getResourceAsStream("jrys/" + school + "/" + club + "/bg.png"));
            BufferedImage cover = ImageIO.read(classLoader.getResourceAsStream("jrys/" + school + "/" + club + "/" + (index % picNum == 0 ? picNum : index % picNum) + ".png"));
            index++;

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
            Font font = GFont.font;
            g.setFont(font);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);

            GetSentenceApi.getApi(fromQQ);

            //获取运势信息
            String text = GetSentenceApi.getText("fortuneSummary");
            //检测运势长度，自适应字体大小
            if (text.length() >= 5) font = font.deriveFont(30f);
            int stringWidth = g.getFontMetrics().stringWidth(text);
            int x =(495 - stringWidth) /2;
            g.drawString(text, x, 170);

            //获取运势建议
            String message = GetSentenceApi.getText("unSignText");
            font = font.deriveFont(30f);
            g.setFont(font);
            g.setColor(Color.black);

            //绘制竖向字体
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int fontHeight = fontMetrics.getHeight() * 2 + 5;
            int msgY = 250;
            int msgX = 310;
            for(int msgLength = 0; msgLength < message.length()-1; msgLength +=1) {
                g.drawString(String.valueOf(message.charAt(msgLength)), msgX, msgY);
                msgY += fontHeight;
                if(msgY > 650) {
                    msgY = 250;
                    msgX -= 38;
                }
            }
            font = font.deriveFont(20f);
            g.setFont(font);
            g.setColor(Color.white);
            g.drawString("BOT阿洛娜&Travellerr", 550, 795);
            g.dispose();
            sendImage(sender, combined, subject, fromQQ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendImage(User sender, Image image, Contact subject, Long fromQQ) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write((RenderedImage) image, "png", stream);
        } catch (IOException e) {
            MessageChainBuilder sendMsg = new MessageChainBuilder();
            net.mamoe.mirai.message.data.Image avatar = Contact.uploadImage(subject, new URL(sender.getAvatarUrl(AvatarSpec.LARGE)).openConnection().getInputStream());
            sendMsg.append(new At(fromQQ));
            sendMsg.append("\n");
            sendMsg.append(avatar);
            sendMsg.append("\n").append(GetSentenceApi.getText("fortuneSummary")).append("\n").append(GetSentenceApi.getText("luckyStar")).append("\n").append(GetSentenceApi.getText("signText")).append("\n").append(GetSentenceApi.getText("unSignText")).append("\n\n抱歉Sensei，由于图片无法发送，这是阿洛娜手写出来的签！");
            Log.error("签到管理:签到图片发送错误!", e);
            subject.sendMessage(sendMsg.build());
            return;
        }
        net.mamoe.mirai.message.data.Image sendImage = subject.uploadImage(ExternalResource.create(new ByteArrayInputStream(stream.toByteArray())));
        subject.sendMessage(sendImage.plus(new At(fromQQ)));
    }

    // 生成随机数获取运势

    @Deprecated(since = "已废弃")
    public static String jrysRand() {
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