package Examples;

import java.util.concurrent.*;

public class ExecutorCallableFuture {

	
public static void main(String[] args) throws Exception {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Integer> tarea = () -> {
            System.out.println("Calculando...");
            Thread.sleep(1000);
            return 10 + 20;
        };

        Future<Integer> resultado = executor.submit(tarea);

        System.out.println("Haciendo otras cosas...");

        System.out.println("Resultado de la tarea: " + resultado.get()); // Espera si no ha terminado

        executor.shutdown();
    }
}