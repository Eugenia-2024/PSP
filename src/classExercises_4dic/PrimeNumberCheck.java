package classExercises_4dic;

import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PrimeNumberCheck {
    // Callable: does the calculation
    // Returns -1 if number is prime.
    // Otherwise returns the largest divisor (different from the number).
    static class PrimeTask implements Callable<Integer> {
        private final int number;

        public PrimeTask(int number) {
            this.number = number;
        }

        @Override
        public Integer call() {
            // Largest divisor search (given by the teacher as a tip)
            for (int i = number - 1; i > 1; i--) {
                if (number % i == 0) {
                    return i; // largest divisor found
                }
            }
            return -1; // no divisor found -> prime
        }
    }

    // Runnable: prints the result
    static class ResultPrinter implements Runnable {
        private final int number;
        private final Future<Integer> futureResult;

        public ResultPrinter(int number, Future<Integer> futureResult) {
            this.number = number;
            this.futureResult = futureResult;
        }

        @Override
        public void run() {
            try {
                int result = futureResult.get(); // waits until Callable finishes

                if (result == -1) {
                    System.out.println(number + " is a PRIME number.");
                } else {
                    System.out.println(number + " is NOT prime.");
                    System.out.println("Largest divisor (not the number itself) = " + result);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Result printing was interrupted.");
            } catch (ExecutionException e) {
                System.out.println("Error during computation: " + e.getCause());
            }
        }
    }

    // main: reads input and starts the threads
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an integer number: ");
        int number = scanner.nextInt();

        // ExecutorService with 2 threads (one for Callable, one for Runnable)
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // 1) Submit the Callable task (PrimeTask)
        PrimeTask primeTask = new PrimeTask(number);
        Future<Integer> future = executor.submit(primeTask);

        // 2) Execute the Runnable task (ResultPrinter)
        ResultPrinter printer = new ResultPrinter(number, future);
        executor.execute(printer);

        // 3) Shut down the executor (it will finish current tasks first)
        executor.shutdown();
    }
}
