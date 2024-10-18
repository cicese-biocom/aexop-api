package tomocomd.utils;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import tomocomd.exceptions.AExOpDCSException;

public class ResourceMetrics {

  private static final String LOG_NAME = "logs/resource-metrics.log";
  private static final long MB = 1024 * 1024;

  private static final Logger logger = Logger.getLogger(ResourceMetrics.class.getName());

  public ResourceMetrics() throws AExOpDCSException {
    try {
      FileHandler fileHandler = new FileHandler(LOG_NAME, true);
      fileHandler.setFormatter(new OneLineFormatter());
      logger.addHandler(fileHandler);
    } catch (Exception e) {
      throw AExOpDCSException.ExceptionType.AEXOPDCS_EXCEPTION.get(e);
    }
  }

  // recover metrics
  public void logMetrics(String functionName, String className) {

    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    OperatingSystemMXBean osBean =
        (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    // heap (JVM)
    MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
    long usedHeapMemory = heapMemoryUsage.getUsed() / MB;
    long maxHeapMemory = heapMemoryUsage.getMax() / MB;
    long freeHeapMemory = maxHeapMemory - usedHeapMemory;

    // CPU (%)
    double cpuLoad = osBean.getSystemCpuLoad() * 100;

    // RAM
    long freeRAM = osBean.getFreePhysicalMemorySize() / MB;
    long totalRAM = osBean.getTotalPhysicalMemorySize() / MB;

    // threads
    int activeThreadCount = threadMXBean.getThreadCount();

    logger.info(
        String.format(
            "[%s] | %s.%s | "
                + "Heap Memory: %d MB / %d MB | Free Heap: %d MB | "
                + "CPU Load: %.2f%% | Free RAM: %d MB / %d MB | Active Threads: %d",
            java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            functionName,
            className,
            usedHeapMemory,
            maxHeapMemory,
            freeHeapMemory,
            cpuLoad,
            freeRAM,
            totalRAM,
            activeThreadCount));
  }

  private static class OneLineFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
      return logRecord.getMessage() + System.lineSeparator();
    }
  }
}
