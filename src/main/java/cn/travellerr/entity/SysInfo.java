package cn.travellerr.entity;

import cn.travellerr.AronaBot;
import cn.travellerr.tools.Log;
import lombok.Getter;
import lombok.Setter;
import net.mamoe.mirai.console.MiraiConsole;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.util.Util;

import java.io.File;
import java.text.DecimalFormat;

@Setter
@Getter
public class SysInfo {

    private double usedCpu;
    private long TotalMem;
    private long FreeMem;
    private long TotalDisk;
    private long FreeSpaceDisk;
    private String sent;
    private String receive;
    private long sendGroupMsgNum = AronaBot.sendGroupMsgNum;
    private long sendFriendMsgNum = AronaBot.sendFriendMsgNum;
    private int plugins = MiraiConsole.INSTANCE.getPluginManager().getPlugins().size() - 1;


    public SysInfo() {
        this.usedCpu = getCpuUsage();
        getMemoryInfo();
        getDiskInfo();
        getNetflow();
    }


    private double getCpuUsage() {
        CentralProcessor processor = new SystemInfo().getHardware().getProcessor();
        // Wait a second...
        Util.sleep(100);
        double[] loads = processor.getProcessorCpuLoadBetweenTicks();
        double totalLoad = 0;
        for (double load : loads) {
            totalLoad += load;
        }
        Log.debug("CPU使用率: " + usedCpu);

        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format((totalLoad / loads.length) * 100));
    }

    private void getMemoryInfo() {
        SystemInfo systemInfo = new SystemInfo();
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        Log.debug("服务器总内存: " + TotalMem);
        Log.debug("服务器空闲内存: " + FreeMem);
        this.TotalMem = memory.getTotal() / 1024 / 1024 / 1024;
        this.FreeMem = memory.getAvailable() / 1024 / 1024 / 1024;
    }

    private void getDiskInfo() {
        File win = new File("/");
        if (win.exists()) {
            long total = win.getTotalSpace();
            long freeSpace = win.getFreeSpace();
            Log.debug("服务器总硬盘空间: " + TotalDisk);
            Log.debug("服务器剩余硬盘空间: " + FreeSpaceDisk);
            this.TotalDisk = total / 1024 / 1024 / 1024;
            this.FreeSpaceDisk = freeSpace / 1024 / 1024 / 1024;
        }
    }

    private void getNetflow() {
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

        Log.debug("服务器上行量: " + sent);
        Log.debug("服务器下行量: " + receive);
        this.receive = df.format((double) getBytesRecv / (1024 * 1024)) + "MB";
        this.sent = df.format((double) getBytesSent / (1024 * 1024)) + "MB";


    }
}
