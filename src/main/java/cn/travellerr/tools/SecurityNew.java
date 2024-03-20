package cn.travellerr.tools;


import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;
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
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityNew {

    static double cpu;
    static long TotalMem;
    static long UsedMem;
    static long TotalDisk;
    static long FreeSpaceDisk;
    static long UsedDisk;

    static Font Font = GFont.font;

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


    public static void info(ByteArrayOutputStream stream, Contact subject, long QQ) {
        Font = Font.deriveFont(25f);
        // 加载背景图
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(SecurityNew.class.getResourceAsStream("/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*
         * 待优化
         * TODO 封装函数
         */

        // 创建饼图数据集
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Used Cpu", cpu);
        dataset.setValue("Total Cpu", 100 - cpu);

        JFreeChart chart = ChartFactory.createPieChart(
                "",
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


        // 创建一个新的 BufferedImage，宽高与背景图相同
        BufferedImage combinedImage = new BufferedImage(backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        // 在合成图像上绘制背景图
        Graphics2D g2d = combinedImage.createGraphics();
        g2d.drawImage(backgroundImage, 0, 0, null);

        // 创建一个 ChartPanel 并绘制饼图
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(200, 130);
        Color transparentColor = new Color(0, 0, 0, 0); // 完全透明
        chartPanel.setBackground(transparentColor);
        g2d.translate(100, 475);
        chartPanel.paint(g2d);
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
        dataset.setValue("Used Memory", UsedMem);
        dataset.setValue("Total Memory", TotalMem - UsedMem);

        chart = ChartFactory.createPieChart(
                "",
                dataset,
                false,
                false,
                false
        );

        chart.setBorderVisible(false);
        chart.setBackgroundPaint(null);
        chart.setBackgroundImageAlpha(0.0f);
        plot = (PiePlot) chart.getPlot();
        plot.setCircular(true);
        plot.setBackgroundAlpha(0.0f);
        plot.setOutlinePaint(null);
        plot.setLabelGenerator(null);
        plot.setShadowGenerator(null);
        plot.setShadowPaint(null);

        chartPanel = new ChartPanel(chart);
        chartPanel.setSize(200, 130);
        chartPanel.setBackground(transparentColor);
        g2d.translate(230, 0);
        chartPanel.paint(g2d);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(69, 34, 60, 60);

        g2d.setFont(Font);
        g2d.setColor(Color.BLACK);
        g2d.drawString("RAM", 70, 160);
        g2d.setFont(SFont);
        g2d.drawString("使用率", 70, 190);
        DecimalFormat df = new DecimalFormat("#.##");
        String MemInfo = df.format((double) UsedMem / TotalMem * 100) + "%";
        textWidth = fontMetrics.stringWidth(MemInfo);
        textHeight = fontMetrics.getHeight();
        x = (200 - textWidth) / 2;
        y = (420 - textHeight) / 2 + fontMetrics.getAscent();
        g2d.drawString(MemInfo, x, y);


        dataset = new DefaultPieDataset<>();
        dataset.setValue("Used Disk", UsedDisk);
        dataset.setValue("Total Disk", FreeSpaceDisk);

        chart = ChartFactory.createPieChart(
                "",
                dataset,
                false,
                false,
                false
        );

        chart.setBorderVisible(false);
        chart.setBackgroundPaint(null);
        chart.setBackgroundImageAlpha(0.0f);
        plot = (PiePlot) chart.getPlot();
        plot.setCircular(true);
        plot.setBackgroundAlpha(0.0f);
        plot.setOutlinePaint(null);
        plot.setLabelGenerator(null);
        plot.setShadowGenerator(null);
        plot.setShadowPaint(null);

        chartPanel = new ChartPanel(chart);
        chartPanel.setSize(200, 130);
        chartPanel.setBackground(transparentColor);
        g2d.translate(230, 0);
        chartPanel.paint(g2d);
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

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy年M月d日 EEEE HH:mm:ss", Locale.CHINA);

        Font = Font.deriveFont(25f);
        Font = Font.deriveFont(java.awt.Font.PLAIN);
        g2d.setFont(Font);
        g2d.drawString(outputFormat.format(new Date()), 80, 905);


        try {
            ImageIO.write(combinedImage, "png", stream);
        } catch (IOException e) {
            MessageChainBuilder messages = new MessageChainBuilder();
            messages.append(new At(QQ)).append("唔……什亭之匣好像出了些问题呢……图片无法发送，阿洛娜就用文字代替吧！\nCPU使用率: ").append(String.valueOf(cpu)).append("%\n总内存: ").append(String.valueOf(TotalMem)).append("GB\n使用内存: ").append(String.valueOf(UsedMem)).append("GB\n总磁盘: ").append(String.valueOf(TotalDisk)).append("GB\n剩余空间: ").append(String.valueOf(FreeSpaceDisk)).append("GB\n使用空间: ").append(String.valueOf(UsedDisk)).append("GB");
            subject.sendMessage(messages.build());
            e.fillInStackTrace();
        }

    }

    public static void getOsInfo() {
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        double free = cpuInfo.getFree();
        DecimalFormat format = new DecimalFormat("#.00");
        cpu = Double.parseDouble(format.format(100.0D - free));
    }

    public static void getMemoryInfo() {
        TotalMem = OshiUtil.getMemory().getTotal() / 1024 / 1024 / 1024;
        UsedMem = OshiUtil.getMemory().getAvailable() / 1024 / 1024 / 1024;
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
    }


    public static void Security(MessageEvent event) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Contact subject = event.getSubject();
        Map<String, String> matcher = getMatcher((GroupMessageEvent) event);
        long fromQQ = Long.parseLong(matcher.get("来源QQ号"));
        subject.sendMessage(new At(fromQQ).plus("Sensei\n状态获取中，请稍等"));
        getOsInfo();
        getMemoryInfo();
        getDiskUsed();
        info(stream, subject, fromQQ);
        try {
            net.mamoe.mirai.message.data.Image sendImage = subject.uploadImage(ExternalResource.create(new ByteArrayInputStream(stream.toByteArray())));
            subject.sendMessage(sendImage.plus(new At(fromQQ)));
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
}

