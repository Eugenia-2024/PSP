package Examples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorWithRunnable {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable tarea = () ->
                System.out.println("Ejecutando tarea en: " + Thread.currentThread().getName());

        executor.execute(tarea);
        executor.execute(tarea);

        executor.shutdown(); // Cierre ordenado
    }
}