package Examples;

import java.util.concurrent.*;

public class ExampleCancellation {
    public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<String> tareaLarga = () -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Trabajando... paso " + i);
                    Thread.sleep(1000); // Simula trabajo
                }
                return "Completada";
            } catch (InterruptedException e) {
                System.out.println("La tarea fue interrumpida");
                return "Cancelada";
            }
        };

        Future<String> futuro = executor.submit(tareaLarga);

        Thread.sleep(2500); // Esperamos 2.5 segundos
        System.out.println("Intentando cancelar...");
        
        boolean cancelada = futuro.cancel(true); // true = interrumpir hilo
        System.out.println("Cancelada? " + cancelada);

        executor.shutdown();
    }
}