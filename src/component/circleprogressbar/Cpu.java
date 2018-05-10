package component.circleprogressbar;

import org.hyperic.sigar.*;

public class Cpu {
    public static void main(String[] args) {
        try {
            cpu();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private static void memory() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        // 内存总量
        System.out.println("内存总量:    " + mem.getTotal() / 1024L + "K av");
        // 当前内存使用量
        System.out.println("当前内存使用量:    " + mem.getUsed() / 1024L + "K used");
        // 当前内存剩余量
        System.out.println("当前内存剩余量:    " + mem.getFree() / 1024L + "K free");
        Swap swap = sigar.getSwap();
        // 交换区总量
        System.out.println("交换区总量:    " + swap.getTotal() / 1024L + "K av");
        // 当前交换区使用量
        System.out.println("当前交换区使用量:    " + swap.getUsed() / 1024L + "K used");
        // 当前交换区剩余量
        System.out.println("当前交换区剩余量:    " + swap.getFree() / 1024L + "K free");
    }

    private static void cpu() throws SigarException {
        Sigar sigar = new Sigar();
        org.hyperic.sigar.CpuInfo infos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = null;
        cpuList = sigar.getCpuPercList();
        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
            org.hyperic.sigar.CpuInfo info = infos[i];
            System.out.println("第" + (i + 1) + "块CPU信息");
            System.out.println("CPU的总量MHz:    " + info.getMhz());// CPU的总量MHz
            System.out.println("CPU生产商:    " + info.getVendor());// 获得CPU的卖主，如：Intel
            System.out.println("CPU类别:    " + info.getModel());// 获得CPU的类别，如：Celeron
            System.out.println("CPU缓存数量:    " + info.getCacheSize());// 缓冲存储器数量
            printCpuPerc(cpuList[i]);
        }
    }

    private static void printCpuPerc(CpuPerc cpu) {
        System.out.println("CPU用户使用率:    " + CpuPerc.format(cpu.getUser()));// 用户使用率
        System.out.println("CPU系统使用率:    " + CpuPerc.format(cpu.getSys()));// 系统使用率
        System.out.println("CPU当前等待率:    " + CpuPerc.format(cpu.getWait()));// 当前等待率
        System.out.println("CPU当前错误率:    " + CpuPerc.format(cpu.getNice()));//
        System.out.println("CPU当前空闲率:    " + CpuPerc.format(cpu.getIdle()));// 当前空闲率
        System.out.println("CPU总的使用率:    " + CpuPerc.format(cpu.getCombined()));// 总的使用率
        System.out.println(cpu.getCombined());
    }
    public static int cpuRate() throws SigarException {
        Sigar sigar = new Sigar();
        org.hyperic.sigar.CpuInfo infos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = null;
        cpuList = sigar.getCpuPercList();
        double tmp = 0;
        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
            tmp += cpuList[i].getCombined();
        }
        return (int)(tmp*100/infos.length);
    }
    public static String memoryTotal() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        return mem.getTotal() / 1024L + "K ";
    }
    public static String memoryFree() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        return mem.getFree() / 1024L + "K ";
    }
    public static String memoryUsed() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        return mem.getUsed() / 1024L + "K ";
    }

}