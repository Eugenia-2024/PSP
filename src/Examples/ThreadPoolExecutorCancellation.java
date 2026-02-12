package Examples;

import java.util.concurrent.*;

public class ThreadPoolExecutorCancellation {

    public static void main(String[] args) throws InterruptedException {

        // Crear un ThreadPoolExecutor personalizado
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,                 // núm. mínimo de hilos
                4,                 // núm. máximo de hilos
                10, TimeUnit.SECONDS, // tiempo de vida de hilos extra
                new LinkedBlockingQueue<>(10), // cola de tareas
                new ThreadPoolExecutor.CallerRunsPolicy() // política de rechazo
        );

        Callable<String> tareaLarga = () -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    System.out.println("Trabajando... paso " + i);
                    Thread.sleep(700);
                }
                return "Completada";
            } catch (InterruptedException e) {
                System.out.println("La tarea fue interrumpida dentro del pool");
                return "Cancelada";
            }
        };

        System.out.println("Enviando tarea al ThreadPoolExecutor...");
        Future<String> future = executor.submit(tareaLarga);

        Thread.sleep(2500); // Dejamos que trabaje un poco

        System.out.println("Cancelando tarea...");
        boolean cancelada = future.cancel(true); // true = interrumpir

        System.out.println("¿Se canceló correctamente? " + cancelada);

        executor.shutdown();
    }
}