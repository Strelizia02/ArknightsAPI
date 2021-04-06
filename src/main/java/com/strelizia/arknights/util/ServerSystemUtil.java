package com.strelizia.arknights.util;

import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.*;
import oshi.util.FormatUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author wangzy
 * @Date 2020/12/31 9:31
 **/
public class ServerSystemUtil {
    /**
     * 获取系统硬件/操作系统信息
     * @return
     */
    public String getSystemInfo(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SystemInfo si = new SystemInfo();

        HardwareAbstractionLayer hal = si.getHardware();
        OperatingSystem os = si.getOperatingSystem();

        GlobalMemory memory = hal.getMemory();
        Sensors sensors = hal.getSensors();
        StringBuilder s = new StringBuilder("系统时间：" + sdf.format(new Date()) +
                "\n服务器操作系统：" + os +
                "\nCPU参数： 制造商：" + hal.getComputerSystem().getManufacturer() +
                " CPU核心数：" + hal.getProcessor().getPhysicalProcessorCount() +
                " CPU温度：" + sensors.getCpuTemperature() +
                " 风扇转速：" + Arrays.toString(sensors.getFanSpeeds()) +
                "\n内存使用量：" + FormatUtil.formatBytes(memory.getAvailable()) + "/" + FormatUtil.formatBytes(memory.getTotal()) +
                "\n系统进程总数：" + os.getProcessCount() + "个，线程总数：" + os.getThreadCount() +
                "\n磁盘使用量：");
        OSFileStore[] fsArray = os.getFileSystem().getFileStores();
        for (OSFileStore fs : fsArray) {
            long usable = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            s.append("\n\t").append(fs.getName()).append(" 磁盘格式：").append(fs.getType()).append(" 磁盘使用：").append(FormatUtil.formatBytes(usable)).append("/").append(FormatUtil.formatBytes(total));
        }
        NetworkParams networkParams = os.getNetworkParams();
        s.append("\n网络参数： 主机名：").append(networkParams.getHostName()).append(" IPv4：").append(networkParams.getIpv4DefaultGateway());

        return s.toString();
    }
}
