package Examples;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorSingleThread {
    public static void main(String[] args) {

        // Creamos un Executor simple con un solo hilo
        Executor executor = Executors.newSingleThreadExecutor();

        Runnable tarea = () -> {
            System.out.println("Tarea ejecutada por: " + Thread.currentThread().getName());
        };

        System.out.println("Enviando tarea al Executor...");
        executor.execute(tarea);

        System.out.println("Tarea enviada.");
    }
}