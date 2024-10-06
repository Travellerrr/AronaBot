package cn.travellerr.tools;


import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.travellerr.AronaBot;
import cn.travellerr.entity.SysInfo;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.AvatarSpec;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static cn.travellerr.AronaBot.config;

public class SecurityNew {

    private static java.awt.Font Font = GFont.font;

    public static void drawPieChart(Graphics2D g2d, String title, DefaultPieDataset<String> dataset, int x, int y) {
        JFreeChart chart = ChartFactory.createPieChart(
                title,
                dataset,
                false,
                false,
                false
        );

        chart.setBorderVisible(false);
        chart.setBackgroundPaint(null);
        chart.setBackgroundImageAlpha(0.0f);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setCircular(true);
        plot.setBackgroundAlpha(0.0f);
        plot.setOutlinePaint(null);
        plot.setLabelGenerator(null);
        plot.setShadowGenerator(null);
        plot.setShadowPaint(null);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(200, 130);
        Color transparentColor = new Color(0, 0, 0, 0); // 完全透明
        chartPanel.setBackground(transparentColor);
        g2d.translate(x, y);
        chartPanel.paint(g2d);
    }


    public static void info(ByteArrayOutputStream stream, Contact subject, long QQ, SysInfo sysInfo) {
        String javaVersion = System.getProperty("java.version");
        long runTime = System.currentTimeMillis();
        Bot bot = subject.getBot();
        Font = Font.deriveFont(40f);

        // 加载背景图
        try {

            BufferedImage backgroundImage = ImageIO.read(Objects.requireNonNull(SecurityNew.class.getResourceAsStream("/background.png")));
            // 创建一个新的 BufferedImage，宽高与背景图相同
            BufferedImage combinedImage = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            // 在合成图像上绘制背景图
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.drawImage(backgroundImage, 0, 0, null);


            /*
             * 第一框图
             */
            BufferedImage avatar = ImageIO.read(new URL(bot.getAvatarUrl(AvatarSpec.LARGE)));
            //圆角处理
            BufferedImage avatarRounder = makeRoundedCorner(avatar);

            g2d.drawImage(avatarRounder, 75, 180, null);
            g2d.setFont(Font);
            g2d.setColor(Color.BLACK);
            g2d.drawString(bot.getNick(), 250, 195);

            Font = Font.deriveFont(25f);
            g2d.setFont(Font);
            g2d.drawString(String.valueOf(bot.getId()), 330, 240);
            g2d.drawString(javaVersion, 375, 280);
            g2d.drawString(convertTime(runTime - AronaBot.startTime), 330, 320);



            /*
             * 第二框图
             */
            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.setValue("Used Cpu", sysInfo.getUsedCpu());
            dataset.setValue("Free Cpu", 100 - sysInfo.getUsedCpu());

            drawPieChart(g2d, "", dataset, 100, 475);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(69, 34, 60, 60);
            g2d.setColor(Color.BLACK);
            Font SFont = Font.deriveFont(20f);
            g2d.setFont(SFont);
            String CpuInfo = sysInfo.getUsedCpu() + "%";
            FontMetrics fontMetrics = g2d.getFontMetrics(SFont);
            int textWidth = fontMetrics.stringWidth(CpuInfo);
            int textHeight = fontMetrics.getHeight();
            int x = (200 - textWidth) / 2;
            int y = (420 - textHeight) / 2 + fontMetrics.getAscent();
            g2d.drawString(CpuInfo, x, y);

            //绘制表盘
            dataset = new DefaultPieDataset<>();
            dataset.setValue("Used Memory", sysInfo.getTotalMem() - sysInfo.getFreeMem());
            dataset.setValue("Free Memory", sysInfo.getFreeMem());
            drawPieChart(g2d, "", dataset, 230, 0);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(69, 34, 60, 60);

            g2d.setColor(Color.BLACK);
            g2d.setFont(SFont);
            DecimalFormat df = new DecimalFormat("#.##");
            String MemInfo = df.format((double) (sysInfo.getTotalMem() - sysInfo.getFreeMem()) / sysInfo.getTotalMem() * 100) + "%";
            textWidth = fontMetrics.stringWidth(MemInfo);
            textHeight = fontMetrics.getHeight();
            x = (200 - textWidth) / 2;
            y = (420 - textHeight) / 2 + fontMetrics.getAscent();
            g2d.drawString(MemInfo, x, y);

            double usedDisk = sysInfo.getTotalDisk() - sysInfo.getFreeSpaceDisk();
            dataset = new DefaultPieDataset<>();
            dataset.setValue("Used Disk", usedDisk);
            dataset.setValue("Free Disk", sysInfo.getFreeSpaceDisk());
            drawPieChart(g2d, "", dataset, 230, 0);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(69, 34, 60, 60);
            // 保存合成后的图像到文件
            g2d.setColor(Color.BLACK);
            g2d.setFont(SFont);
            String DiskInfo = df.format((double) usedDisk / sysInfo.getTotalDisk() * 100) + "%";
            textWidth = fontMetrics.stringWidth(DiskInfo);
            textHeight = fontMetrics.getHeight();
            x = (200 - textWidth) / 2;
            y = (420 - textHeight) / 2 + fontMetrics.getAscent();
            g2d.drawString(DiskInfo, x, y);


            g2d.translate(-560, -475);


            /*
             * 第三框图
             */



            Font = Font.deriveFont(java.awt.Font.PLAIN);

            Font = Font.deriveFont(20f);
            g2d.setFont(Font);
            g2d.setColor(new Color(0, 160, 0));
            g2d.drawString("发送: " + sysInfo.getSent(), 520, 945);
            g2d.drawString(sysInfo.getSendGroupMsgNum() + "条", 250, 910);
            g2d.drawString(sysInfo.getSendFriendMsgNum() + "条", 250, 980);
            g2d.setColor(new Color(180, 0, 0));
            g2d.drawString("接收: " + sysInfo.getReceive(), 520, 1000);


            /*
             * 第四框图
             */
            g2d.setColor(Color.BLACK);
            Font = Font.deriveFont(27f);
            g2d.setFont(Font);

            g2d.drawString(sysInfo.getPlugins() + "个", 200, 1255);

            Font = Font.deriveFont(35f).deriveFont(java.awt.Font.BOLD);
            g2d.setFont(Font);
            String system = System.getProperty("os.name");
            g2d.drawString(system, 80, 1205);


            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy年M月d日 EEEE HH:mm:ss", Locale.CHINA);

            Font = Font.deriveFont(30f);
            g2d.setFont(Font);
            g2d.drawString(outputFormat.format(new Date()), 80, 1375);
            ImageIO.write(combinedImage, "png", stream);

        } catch (IOException e) {
            MessageChainBuilder messages = new MessageChainBuilder();
            messages.append(new At(QQ)).append("唔……好像出了些问题呢……图片无法发送，")
                    .append(subject.getBot().getNick()).append("就用文字代替吧！\nCPU使用率: ")
                    .append(String.valueOf(sysInfo.getUsedCpu()))
                    .append("%\n总内存: ").append(String.valueOf(sysInfo.getTotalMem())).append("GB\n使用内存: ")
                    .append(String.valueOf(sysInfo.getTotalMem() - sysInfo.getFreeMem()))
                    .append("GB\n总磁盘: ").append(String.valueOf(sysInfo.getTotalDisk()))
                    .append("GB\n剩余空间: ").append(String.valueOf(sysInfo.getFreeSpaceDisk()))
                    .append("GB\n使用空间: ").append(String.valueOf(sysInfo.getTotalDisk() - sysInfo.getFreeSpaceDisk())).append("GB");
            subject.sendMessage(messages.build());
            e.fillInStackTrace();
        }

    }





    public static void Security(Contact subject, User user) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        subject.sendMessage(new At(user.getId()).plus(config.getSuffix() + "\n状态获取中，请稍等"));
        SysInfo sysInfo = new SysInfo();
        info(stream, subject, user.getId(), sysInfo);
        try {
            ExternalResource resource = ExternalResource.create(new ByteArrayInputStream(stream.toByteArray()));
            net.mamoe.mirai.message.data.Image sendImage = subject.uploadImage(resource);
            subject.sendMessage(sendImage.plus(new At(user.getId())));
            resource.close();
            stream.close();

        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    /**
     * @author chahuyun
     * @see  <a href="https://github.com/Moyuyanli/HuYanEconomy/tree/master">HuyanEconomy</a>
     */
    private static BufferedImage makeRoundedCorner(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        output = g2.getDeviceConfiguration().createCompatibleImage(w, h, Transparency.TRANSLUCENT);
        g2.dispose();
        g2 = output.createGraphics();
        /*
        这里绘画圆角矩形
        原图切圆边角
         */
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, w, h, 60, 60);
        g2.setComposite(AlphaComposite.SrcIn);
        /*结束*/

        g2.drawImage(image, 0, 0, w, h, null);
        g2.dispose();

        return output;
    }


    public static String convertTime(long timestamp) {

        return DateUtil.formatBetween(timestamp, BetweenFormatter.Level.SECOND);
    }
}

