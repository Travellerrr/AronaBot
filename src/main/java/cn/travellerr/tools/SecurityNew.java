package cn.travellerr.tools;


import cn.travellerr.AronaBot;
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
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.util.Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static cn.travellerr.AronaBot.config;

public class SecurityNew {

    static double cpu;
    static long TotalMem;
    static long UnusedMem;
    static long TotalDisk;
    static long FreeSpaceDisk;
    static long UsedDisk;
    static String getSent;
    static String getRecv;

    static Font Font = GFont.font;

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


    public static void info(ByteArrayOutputStream stream, Contact subject, long QQ) {
        Font = Font.deriveFont(40f);

        // 加载背景图
        try {

            BufferedImage backgroundImage = ImageIO.read(Objects.requireNonNull(SecurityNew.class.getResourceAsStream("/background.png")));
            // 创建一个新的 BufferedImage，宽高与背景图相同
            BufferedImage combinedImage = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            // 在合成图像上绘制背景图
            Graphics2D g2d = combinedImage.createGraphics();
            g2d.drawImage(backgroundImage, 0, 0, null);
            Bot bot = subject.getBot();
            BufferedImage avatar = ImageIO.read(new URL(bot.getAvatarUrl(AvatarSpec.LARGE)));
            //圆角处理
            BufferedImage avatarRounder = makeRoundedCorner(avatar);


            g2d.drawImage(avatarRounder, 75, 180, null);
            g2d.setFont(Font);
            g2d.setColor(Color.BLACK);
            g2d.drawString(bot.getNick(), 250, 195);
            String javaVersion = System.getProperty("java.version");

            Font = Font.deriveFont(25f);
            g2d.setFont(Font);
            g2d.drawString("QQ号: " + bot.getId(), 250, 240);
            g2d.drawString("Java版本: " + javaVersion, 250, 280);
            long runTime = System.currentTimeMillis();
            g2d.drawString("已运行" + convertTime(runTime - AronaBot.startTime), 250, 320);

            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            dataset.setValue("Used Cpu", cpu);
            dataset.setValue("Total Cpu", 100 - cpu);

            drawPieChart(g2d, "", dataset, 100, 475);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(69, 34, 60, 60);
            g2d.setFont(Font);
            g2d.setColor(Color.BLACK);
            g2d.drawString("CPU", 70, 160);
            Font SFont = Font.deriveFont(20f);
            g2d.setFont(SFont);
            g2d.drawString("使用率", 70, 190);
            String CpuInfo = cpu + "%";
            FontMetrics fontMetrics = g2d.getFontMetrics(SFont);
            int textWidth = fontMetrics.stringWidth(CpuInfo);
            int textHeight = fontMetrics.getHeight();
            int x = (200 - textWidth) / 2;
            int y = (420 - textHeight) / 2 + fontMetrics.getAscent();
            g2d.drawString(CpuInfo, x, y);


            /*
             * 重复使用，希望有一天能包装成函数
             */
            dataset = new DefaultPieDataset<>();
            dataset.setValue("Used Memory", UnusedMem);
            dataset.setValue("Total Memory", TotalMem - UnusedMem);
            drawPieChart(g2d, "", dataset, 230, 0);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(69, 34, 60, 60);

            g2d.setFont(Font);
            g2d.setColor(Color.BLACK);
            g2d.drawString("RAM", 70, 160);
            g2d.setFont(SFont);
            g2d.drawString("使用率", 70, 190);
            DecimalFormat df = new DecimalFormat("#.##");
            String MemInfo = df.format((double) (TotalMem - UnusedMem) / TotalMem * 100) + "%";
            textWidth = fontMetrics.stringWidth(MemInfo);
            textHeight = fontMetrics.getHeight();
            x = (200 - textWidth) / 2;
            y = (420 - textHeight) / 2 + fontMetrics.getAscent();
            g2d.drawString(MemInfo, x, y);


            dataset = new DefaultPieDataset<>();
            dataset.setValue("Used Disk", UsedDisk);
            dataset.setValue("Total Disk", FreeSpaceDisk);
            drawPieChart(g2d, "", dataset, 230, 0);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(69, 34, 60, 60);
            // 保存合成后的图像到文件
            g2d.setFont(Font);
            g2d.setColor(Color.BLACK);
            g2d.drawString("磁盘", 70, 160);
            g2d.setFont(SFont);
            g2d.drawString("使用率", 70, 190);
            String DiskInfo = df.format((double) UsedDisk / TotalDisk * 100) + "%";
            textWidth = fontMetrics.stringWidth(DiskInfo);
            textHeight = fontMetrics.getHeight();
            x = (200 - textWidth) / 2;
            y = (420 - textHeight) / 2 + fontMetrics.getAscent();
            g2d.drawString(DiskInfo, x, y);

            g2d.translate(-560, -475);
            Font = Font.deriveFont(35f);
            Font = Font.deriveFont(java.awt.Font.BOLD);
            g2d.setFont(Font);
            String system = System.getProperty("os.name");
            g2d.drawString(system, 80, 855);

            Font = Font.deriveFont(30f);
            Font = Font.deriveFont(java.awt.Font.PLAIN);
            g2d.setFont(Font);
            g2d.drawString("网络流量", 80, 905);
            Font = Font.deriveFont(20f);
            g2d.setFont(Font);
            g2d.setColor(new Color(0, 160, 0));
            g2d.drawString("发送: " + getSent, 90, 935);
            g2d.setColor(new Color(180, 0, 0));
            g2d.drawString("接收: " + getRecv, 90, 965);
            g2d.setColor(Color.BLACK);
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy年M月d日 EEEE HH:mm:ss", Locale.CHINA);


            Font = Font.deriveFont(30f);
            g2d.setFont(Font);
            g2d.drawString(outputFormat.format(new Date()), 80, 1015);
            ImageIO.write(combinedImage, "png", stream);

        } catch (IOException e) {
            MessageChainBuilder messages = new MessageChainBuilder();
            messages.append(new At(QQ)).append("唔……好像出了些问题呢……图片无法发送，").append(subject.getBot().getNick()).append("就用文字代替吧！\nCPU使用率: ").append(String.valueOf(cpu)).append("%\n总内存: ").append(String.valueOf(TotalMem)).append("GB\n使用内存: ").append(String.valueOf(UnusedMem)).append("GB\n总磁盘: ").append(String.valueOf(TotalDisk)).append("GB\n剩余空间: ").append(String.valueOf(FreeSpaceDisk)).append("GB\n使用空间: ").append(String.valueOf(UsedDisk)).append("GB");
            subject.sendMessage(messages.build());
            e.fillInStackTrace();
        }

    }

    public static void getCpuUsage() {
        CentralProcessor processor = new SystemInfo().getHardware().getProcessor();
        // Wait a second...
        Util.sleep(100);
        double[] loads = processor.getProcessorCpuLoadBetweenTicks();
        double totalLoad = 0;
        for (double load : loads) {
            totalLoad += load;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        cpu = Double.parseDouble(df.format((totalLoad / loads.length) * 100));
        Log.debug("CPU使用率: " + cpu);
    }

    public static void getMemoryInfo() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        TotalMem = memory.getTotal() / 1024 / 1024 / 1024;
        UnusedMem = memory.getAvailable() / 1024 / 1024 / 1024;
        Log.debug("服务器总内存: " + TotalMem);
        Log.debug("服务器空闲内存: " + UnusedMem);
    }

    public static void getDiskUsed() {
        File win = new File("/");
        if (win.exists()) {
            long total = win.getTotalSpace();
            long freeSpace = win.getFreeSpace();
            TotalDisk = total / 1024 / 1024 / 1024;
            FreeSpaceDisk = freeSpace / 1024 / 1024 / 1024;
            UsedDisk = (total - freeSpace) / 1024 / 1024 / 1024;
        }
        Log.debug("服务器总硬盘空间: " + TotalDisk);
        Log.debug("服务器剩余硬盘空间: " + FreeSpaceDisk);
        Log.debug("服务器使用硬盘空间: " + UsedDisk);
    }

    public static void getBytes() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        DecimalFormat df = new DecimalFormat("#.##");
        NetworkIF[] networkIFs = hardware.getNetworkIFs();
        long getBytesRecv = 0;
        long getBytesSent = 0;
        for (NetworkIF net : networkIFs) {
            getBytesRecv += net.getBytesRecv();
            getBytesSent += net.getBytesSent();
        }
        getRecv = df.format((double) getBytesRecv / (1024 * 1024)) + "MB";
        getSent = df.format((double) getBytesSent / (1024 * 1024)) + "MB";

        Log.debug("服务器上行量: " + getSent);
        Log.debug("服务器下行量: " + getRecv);

    }



    public static void Security(Contact subject, User user) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        subject.sendMessage(new At(user.getId()).plus(config.getSuffix() + "\n状态获取中，请稍等"));
        getCpuUsage();
        getMemoryInfo();
        getDiskUsed();
        getBytes();
        info(stream, subject, user.getId());
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
        g2.fillRoundRect(0, 0, w, h, 50, 50);
        g2.setComposite(AlphaComposite.SrcIn);
        /*结束*/

        g2.drawImage(image, 0, 0, w, h, null);
        g2.dispose();

        return output;
    }


    public static String convertTime(long timestamp) {
        long seconds = timestamp / 1000;
        long days = seconds / (24 * 3600);
        seconds = seconds % (24 * 3600);
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        return String.format("%d天%d小时%d分%d秒", days, hours, minutes, seconds);
    }
}

