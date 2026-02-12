package udpthreadpool;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ManejadorUDP implements Runnable {

    private DatagramSocket socket;
    private DatagramPacket paquete;

    public ManejadorUDP(DatagramSocket socket, DatagramPacket paquete) {
        this.socket = socket;
        this.paquete = paquete;
    }

    @Override
    public void run() {
        try {
            String mensaje = new String(
                    paquete.getData(),
                    0,
                    paquete.getLength()
            );

            System.out.println(
                "Hilo " + Thread.currentThread().getName() +
                " procesando: " + mensaje
            );

            // Simular procesamiento
            Thread.sleep(1000);

            String respuesta = "OK -> " + mensaje;
            byte[] datosRespuesta = respuesta.getBytes();

            DatagramPacket paqueteRespuesta = new DatagramPacket(
                    datosRespuesta,
                    datosRespuesta.length,
                    paquete.getAddress(),
                    paquete.getPort()
            );

            socket.send(paqueteRespuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}