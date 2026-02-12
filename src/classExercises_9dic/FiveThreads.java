package classExercises_9dic;

import java.util.Random;

public class FiveThreads {
    //Worker thread class
    static class WorkerThread implements Runnable{
        private final String name;
        private final Random random = new Random();

        public WorkerThread(String name){
            this.name = name;
        }

        @Override
        public void run() {
            while (true){
                System.out.println(name + ": I am working");

                //Random delay between 1 and 10 seconds
                int delay = 1 + random.nextInt(10);

                try {
                    Thread.sleep(delay * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(name + " was interrupted");
                    break;
                }
            }
        }
    }

    public static void main(String[] args){
        //Create and start 5 threads
        for (int i = 1; i <= 5; i++){
            Thread t = new Thread(new WorkerThread("Worker thread-" + i));
            t.start();
        }
    }

}//class
