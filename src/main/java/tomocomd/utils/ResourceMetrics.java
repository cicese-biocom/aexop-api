package tomocomd.utils;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceMetrics {

  private static final String LOG_NAME = "logs/resource-metrics.log";
  private static final long MB = 1024 * 1024;

  private static final Logger logger = LogManager.getLogger(ResourceMetrics.class);

  public ResourceMetrics() {
    //    // Remueve el console handler para evitar salida estándar
    //    Logger rootLogger = Logger.getLogger("");
    //    for (var handler : rootLogger.getHandlers()) {
    //      if (handler instanceof ConsoleHandler) {
    //        rootLogger.removeHandler(handler);
    //      }
    //    }
    //
    //    // Configurar el file handler
    //    FileHandler fileHandler = new FileHandler("logs/resource-metrics.log", true);
    //    fileHandler.setFormatter(new OneLineFormatter());
    //    logger.addHandler(fileHandler);
    //    logger.setLevel(Level.ALL); // Ajusta el nivel según sea necesario
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

  //  private static class OneLineFormatter extends Formatter {
  //    @Override
  //    public String format(LogRecord logRecord) {
  //      return logRecord.getMessage() + System.lineSeparator();
  //    }
  //  }
}
