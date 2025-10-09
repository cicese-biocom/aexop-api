package tomocomd.utils;

import com.sun.management.OperatingSystemMXBean;
import java.io.IOException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Clase para registrar métricas de uso de recursos (memoria, CPU, hilos, etc.) en un archivo
 * separado (logs/resource-metrics.log).
 */
public class ResourceMetrics implements Serializable {

  private static final long MB = 1048576;
  private static final String LOG_FILE = "logs/resource-metrics.log";

  // Logger propio, separado del logger principal de la aplicación
  private static final Logger metricsLogger = Logger.getLogger("ResourceMetricsLogger");

  static {
    try {
      // Evita que duplique salida en consola
      metricsLogger.setUseParentHandlers(false);

      // Crea el directorio de logs si no existe
      java.nio.file.Files.createDirectories(java.nio.file.Paths.get("logs"));

      // Configura el FileHandler (modo append = true)
      FileHandler fileHandler = new FileHandler(LOG_FILE, true);
      fileHandler.setFormatter(new OneLineFormatter());
      fileHandler.setLevel(Level.INFO);

      // Asocia el FileHandler solo a este logger
      metricsLogger.addHandler(fileHandler);
      metricsLogger.setLevel(Level.INFO);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void logMetrics(String functionName, String className) {
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    OperatingSystemMXBean osBean =
        (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    // Heap de la JVM
    MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
    long usedHeapMemory = heapMemoryUsage.getUsed() / MB;
    long maxHeapMemory = heapMemoryUsage.getMax() / MB;
    long freeHeapMemory = maxHeapMemory - usedHeapMemory;

    // CPU
    double cpuLoad = osBean.getSystemCpuLoad() * 100;
    String formattedCpuLoad = String.format("%.2f", cpuLoad);

    // RAM física
    long freeRAM = osBean.getFreePhysicalMemorySize() / MB;
    long totalRAM = osBean.getTotalPhysicalMemorySize() / MB;

    // Hilos
    int activeThreadCount = threadMXBean.getThreadCount();

    // Fecha
    String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    // Línea formateada
    String message =
        String.format(
            "[%s] | %s.%s | Heap: %d/%d MB (Free: %d MB) | CPU: %s%% | RAM: %d/%d MB | Threads: %d",
            date,
            className,
            functionName,
            usedHeapMemory,
            maxHeapMemory,
            freeHeapMemory,
            formattedCpuLoad,
            freeRAM,
            totalRAM,
            activeThreadCount);

    // Registro en archivo separado
    metricsLogger.info(message);
  }

  // Formateador simple (una línea por log)
  private static class OneLineFormatter extends Formatter {
    @Override
    public String format(LogRecord logRecord) {
      return logRecord.getMessage() + System.lineSeparator();
    }
  }
}
