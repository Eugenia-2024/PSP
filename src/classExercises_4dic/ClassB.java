package classExercises_4dic;

import java.util.concurrent.Exchanger;

class ClassB implements Runnable {

    private final Exchanger<String> exchanger;
    private final String macAddress;

    public ClassB(Exchanger<String> exchanger, String macAddress) {
        this.exchanger = exchanger;
        this.macAddress = macAddress;
    }

    @Override
    public void run() {
        try {
            System.out.println("ClassB: sending MAC = " + macAddress);
            // Send MAC and receive IP
            String ipAddress = exchanger.exchange(macAddress);
            System.out.println("ClassB: received IP = " + ipAddress);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("ClassB interrupted.");
        }
    }
}
