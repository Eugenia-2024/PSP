package classExercises_4dic;

import java.util.concurrent.Exchanger;

class ClassA implements Runnable {

    private final Exchanger<String> exchanger;
    private final String ipAddress;

    public ClassA(Exchanger<String> exchanger, String ipAddress) {
        this.exchanger = exchanger;
        this.ipAddress = ipAddress;
    }

    @Override
    public void run() {
        try {
            System.out.println("ClassA: sending IP = " + ipAddress);
            // Send IP and receive MAC
            String macAddress = exchanger.exchange(ipAddress);
            System.out.println("ClassA: received MAC = " + macAddress);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("ClassA interrupted.");
        }
    }
}
