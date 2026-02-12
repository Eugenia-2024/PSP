package Threads_December;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SistemaRiego extends TimerTask {
    @Override
    public void run(){
        System.out.println("Regado");
    }

    public static void main(String[] args){
        Timer temporizador = new Timer();
        temporizador.schedule(new SistemaRiego(), 1000, 2000);

        ScheduledExecutorService stp = Executors.newSingleThreadScheduledExecutor();
       // stp.scheduleAtFixedRate(sr, 1, 2, TimeUnit.SECONDS);
    }

}//class
