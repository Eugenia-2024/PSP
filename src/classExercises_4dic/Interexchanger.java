package classExercises_4dic;

import java.util.concurrent.Exchanger;

public class Interexchanger {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Interexchanger <IP> <MAC>");
            System.exit(1);
        }

        String ipFromCmd  = args[0]; // IP from the command line
        String macFromCmd = args[1]; // MAC from the command line

        Exchanger<String> exchanger = new Exchanger<>();

        Thread threadA = new Thread(new ClassA(exchanger, ipFromCmd), "ClassA-Thread");
        Thread threadB = new Thread(new ClassB(exchanger, macFromCmd), "ClassB-Thread");

        threadA.start();
        threadB.start();

        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Interexchange finished.");
    }
}
