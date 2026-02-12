package Examples;

import java.util.concurrent.*;

public class ExecutorScheduled {
    public static void main(String[] args) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable tarea = () -> System.out.println("Ejecución periódica");

        // Ejecutar después de 3 segundos
        scheduler.schedule(tarea, 3, TimeUnit.SECONDS);

        // Ejecutar cada 2 segundos
        scheduler.scheduleAtFixedRate(tarea, 0, 2, TimeUnit.SECONDS);
    }
}

