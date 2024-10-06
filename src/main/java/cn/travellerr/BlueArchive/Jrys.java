package cn.travellerr.BlueArchive;

import cn.travellerr.tools.GFont;
import cn.travellerr.tools.Log;
import net.mamoe.mirai.contact.AvatarSpec;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

import static cn.travellerr.AronaBot.config;

public class Jrys {
    private static int index = 1;

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
            sendMsg.append("\n").append(GetSentenceApi.fortuneSummary).append("\n").append(GetSentenceApi.luckyStar).append("\n").append(GetSentenceApi.signText).append("\n").append(GetSentenceApi.unSignText).append("\n\n抱歉").append(config.getSuffix()).append("，由于图片无法发送，这是阿洛娜手写出来的签！");
            Log.error("签到管理:签到图片发送错误!", e);
            subject.sendMessage(sendMsg.build());
            stream.close();
            return;
        }
        ExternalResource resource = ExternalResource.create(new ByteArrayInputStream(stream.toByteArray()));
        net.mamoe.mirai.message.data.Image sendImage = subject.uploadImage(resource);
        subject.sendMessage(sendImage.plus(new At(fromQQ)));
        stream.close();
        resource.close();
    }

    private static String stamp(String luckyStar) {
        if (Objects.equals(luckyStar, "☆☆☆☆☆☆☆") || Objects.equals(luckyStar, "★☆☆☆☆☆☆") || Objects.equals(luckyStar, "★★☆☆☆☆☆")) {
            Random random = new Random();
            int randImg = random.nextInt(4);
            return "jrys/心奈印章/0_" + randImg + ".png";
        }
        if (Objects.equals(luckyStar, "★★★☆☆☆☆") || Objects.equals(luckyStar, "★★★★☆☆☆")) {
            return "jrys/心奈印章/59.png";
        }
        if (Objects.equals(luckyStar, "★★★★★☆☆") || Objects.equals(luckyStar, "★★★★★★☆")) {
            return "jrys/心奈印章/60.png";
        } else {
            return "jrys/心奈印章/100.png";
        }
    }


    public static void info(Contact subject, User sender) {
        subject.sendMessage(new At(sender.getId()).plus("\n" + config.getSuffix() + "请稍等！" + subject.getBot().getNick() + "这就为您抽签！"));
        long botId = subject.getBot().getId();
        GetSentenceApi.generateFortuneID(sender.getId(), botId, true);

        if (!config.isText()) {

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

                // 读取背景图片和覆盖图片
                ClassLoader classLoader = Jrys.class.getClassLoader();
                BufferedImage background = ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream("jrys/" + school + "/" + club + "/bg.png")));
                BufferedImage cover = ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream("jrys/" + school + "/" + club + "/" + (index % picNum == 0 ? picNum : index % picNum) + ".png")));
                index++;

                int newWidth = (int) (cover.getWidth() / 1.6); // 缩小为原来的一半
                int newHeight = (int) (cover.getHeight() / 1.6);

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
                g.fillRect(20, 20, combined.getWidth() - 40, combined.getHeight() - 40);
                alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
                g.setComposite(alphaComposite);

                // 将覆盖图片叠加到背景图片上
                int coverX = 600 - resizedCover.getWidth() / 2; // 计算要绘制的图片在背景图片中的 X 坐标，使其中心点的 x 坐标为 300
                g.drawImage(resizedCover, coverX, 100, null);

                // 绘制签底
                g.setColor(Color.red);
                g.fillRect(100, 100, 300, 600);
                g.setColor(Color.white);
                g.fillRect(105, 105, 290, 590);
                g.setColor(Color.red);
                g.fillRect(110, 110, 280, 580);
                g.setColor(Color.WHITE);
                g.fillRect(115, 115, 270, 570);
                g.setColor(Color.red);
                g.fillRect(115, 115, 270, 75);
                Font font = GFont.font;
                g.setFont(font);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.WHITE);


                //获取运势信息
                String text = GetSentenceApi.fortuneSummary;
                //检测运势长度，自适应字体大小
                int textX = 495;
                if (text.length() > 6) {

                    font = font.deriveFont(30f);
                    textX = 610;
                }
                if (text.length() > 8) {
                    font = font.deriveFont(25f);
                    textX = 700;
                }
                int stringWidth = g.getFontMetrics().stringWidth(text);
                g.setFont(font);
                int x = (textX - stringWidth) / 2;
                g.drawString(text, x, 165);

                //获取运势建议
                String message = GetSentenceApi.unSignText;
                int adaption = 5;
                int moveX = 38;
                int msgX = 310;
                font = font.deriveFont(30f);
                if (message.length() >= 45) {
                    font = font.deriveFont(20f);
                    adaption = 0;
                    moveX = 32;
                    msgX = 350;
                }
                g.setFont(font);
                g.setColor(Color.black);

                //绘制竖向字体
                FontMetrics fontMetrics = g2d.getFontMetrics();
                int fontHeight = fontMetrics.getHeight() * 2 + adaption;
                int msgY = 220;
                for (int msgLength = 0; msgLength < message.length() - 1; msgLength += 1) {
                    g.drawString(String.valueOf(message.charAt(msgLength)), msgX, msgY);
                    msgY += fontHeight;
                    if (msgY > 618) {
                        msgY = 220;
                        msgX -= moveX;
                    }
                }
                font = font.deriveFont(20f);
                g.setFont(font);
                g.setColor(Color.white);
                g.drawString("AronaBot&Travellerr", 550, 795);


                BufferedImage stamp = ImageIO.read(Objects.requireNonNull(classLoader.getResourceAsStream(stamp(GetSentenceApi.luckyStar))));
                g.drawImage(stamp, 315, 618, null);

            /*font = font.deriveFont(30f);
            g.setFont(font);
            g.setColor(new Color(194, 9, 9));
            g.drawString("祝各位考生能考出属于自己的理想成绩！",40 ,750);*/

                g.dispose();
                sendImage(sender, combined, subject, sender.getId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                MessageChainBuilder sendMsg = new MessageChainBuilder();
                net.mamoe.mirai.message.data.Image avatar = Contact.uploadImage(subject, new URL(sender.getAvatarUrl(AvatarSpec.LARGE)).openConnection().getInputStream());
                sendMsg.append(new At(sender.getId()));
                sendMsg.append("\n");
                sendMsg.append(avatar);
                sendMsg.append("\n").append(GetSentenceApi.fortuneSummary).append("\n").append(GetSentenceApi.luckyStar).append("\n").append(GetSentenceApi.signText).append("\n").append(GetSentenceApi.unSignText);
                subject.sendMessage(sendMsg.build());
            } catch (Exception e) {
                Log.error("出错了~", e.fillInStackTrace());
            }
        }
    }
}