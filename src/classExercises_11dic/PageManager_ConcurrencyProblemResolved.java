package classExercises_11dic;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PageManager_ConcurrencyProblemResolved implements Runnable {
    //Thread-safe queue with capacity 10
    private static final BlockingQueue<String> queue = new ArrayBlockingQueue<>(10);

    @Override
    public void run(){
        try{
            while (true){
                //If queue is full, remove one element.
                //take() is safe: if the queue is empty, it just waits.
                if (queue.remainingCapacity() == 0){
                    queue.take(); //removes head of queue safely
                } else {
                    //if not full, add a new element
                    queue.put("text"); //blocks only if full
                }

                for (String s : queue){
                    System.out.println(s);
                }

                //Small pause so threads don't burn 100% CPU
                Thread.sleep(10);
            }

        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " interrupted.");
        }
    }

    public static void main(String[] args){
        //Initial content: fill queue to capacity
        for (int i = 0; i < 10; i++){
            queue.add("text");
        }

        int numThreads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < numThreads; i++){
            executor.execute(new PageManager_ConcurrencyProblemResolved());
        }
    }
}//class
