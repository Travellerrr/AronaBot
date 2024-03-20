package cn.travellerr.tools;

import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChainBuilder;

import java.io.File;
import java.text.DecimalFormat;


@Deprecated(since = "已废弃")
public class Security {

    static double cpu;
    static long TotalMem;
    static long UsedMem;
    static long TotalDisk;
    static long FreeSpaceDisk;
    static long UsedDisk;

    public static void info(MessageEvent event) {
        Contact subject = event.getSubject();
        getOsInfo();
        getMemoryInfo();
        getDiskUsed();
        MessageChainBuilder messages = new MessageChainBuilder();
        messages.append("CPU使用率: ").append(String.valueOf(cpu)).append("%\n总内存: ").append(String.valueOf(TotalMem)).append("GB\n使用内存: ").append(String.valueOf(UsedMem)).append("GB\n总磁盘: ").append(String.valueOf(TotalDisk)).append("GB\n剩余空间: ").append(String.valueOf(FreeSpaceDisk)).append("GB\n使用空间: ").append(String.valueOf(UsedDisk)).append("GB");
        subject.sendMessage(messages.build());
    }

    public static void getOsInfo() {
        CpuInfo cpuInfo = OshiUtil.getCpuInfo();
        double free = cpuInfo.getFree();
        DecimalFormat format = new DecimalFormat("#.00");
        cpu = Double.parseDouble(format.format(100.0D - free));
    }

    /**
     * 获取内存数据
     */
    public static void getMemoryInfo() {
        TotalMem = OshiUtil.getMemory().getTotal() / 1024 / 1024 / 1024;
        UsedMem = OshiUtil.getMemory().getAvailable() / 1024 / 1024 / 1024;
    }

    /**
     * 获取硬盘使用量
     */
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
}
