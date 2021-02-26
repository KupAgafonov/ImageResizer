package SystemInfo;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;

import java.util.Arrays;


public class InfoCPU {

    private static final Runtime runtime = Runtime.getRuntime();

    private static final SystemInfo systemInfo = new SystemInfo();
    private static final OperatingSystem operatingSystem = systemInfo.getOperatingSystem();


    public static int getAvailableProcessorsByRuntime() {
        return runtime.availableProcessors();
    }

    public static int getLogicalProcessorsByOshi() {
        return systemInfo.getHardware().getProcessor().getLogicalProcessorCount();
    }

    public static int getPhysicalProcessorsByOshi() {
        return systemInfo.getHardware().getProcessor().getPhysicalProcessorCount();
    }

    public static String getDomainName() {
        return operatingSystem.getNetworkParams().getDomainName();
    }

    public static float getCurrentFreq(){
        Long currentFreq = Arrays.stream(systemInfo.getHardware().getProcessor().getCurrentFreq()).findFirst().getAsLong();
        float freqGHz = currentFreq.floatValue()/1000000000;
        return freqGHz;
    }

}
