package udpthreadpool;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {

    public static void main(String[] args) {
        String mensaje = "Hola servidor UDP";
        int puertoServidor = 9876;

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress direccion = InetAddress.getByName("localhost");

            byte[] buffer = mensaje.getBytes();

            DatagramPacket paquete = new DatagramPacket(
                    buffer,
                    buffer.length,
                    direccion,
                    puertoServidor
            );

            socket.send(paquete);

            // Recibir respuesta
            byte[] bufferRespuesta = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(
                    bufferRespuesta,
                    bufferRespuesta.length
            );

            socket.receive(respuesta);

            String mensajeRespuesta = new String(
                    respuesta.getData(),
                    0,
                    respuesta.getLength()
            );

            System.out.println("Respuesta del servidor: " + mensajeRespuesta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}