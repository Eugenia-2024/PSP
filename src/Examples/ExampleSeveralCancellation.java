package Examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExampleSeveralCancellation {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService pool = Executors.newFixedThreadPool(3);

        List<Future<?>> futuros = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            int id = i;
            futuros.add(pool.submit(() -> {
                try {
                    System.out.println("Tarea " + id + " iniciada");
                    Thread.sleep(4000);
                    System.out.println("Tarea " + id + " terminada");
                } catch (InterruptedException e) {
                    System.out.println("Tarea " + id + " cancelada");
                }
            }));
        }

        Thread.sleep(1500);
        System.out.println("Cancelando todas las tareas...");

        for (Future<?> f : futuros) {
            f.cancel(true);
        }

        pool.shutdown();
    }
}