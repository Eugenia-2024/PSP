package udpthreadpool;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServerThreadPool {

    private static final int PUERTO = 9876;
    private static final int TAM_POOL = 5;

    public static void main(String[] args) {

        ExecutorService threadPool = Executors.newFixedThreadPool(TAM_POOL);

        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            System.out.println("Servidor UDP con ThreadPool en puerto " + PUERTO);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);

                // Espera mensaje
                socket.receive(paquete);

                // IMPORTANTE: copiar los datos del paquete
                threadPool.execute(new ManejadorUDP(socket, copiarPaquete(paquete)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    private static DatagramPacket copiarPaquete(DatagramPacket paquete) {
        byte[] datos = new byte[paquete.getLength()];
        System.arraycopy(
                paquete.getData(),
                paquete.getOffset(),
                datos,
                0,
                paquete.getLength()
        );

        return new DatagramPacket(
                datos,
                datos.length,
                paquete.getAddress(),
                paquete.getPort()
        );
    }
}