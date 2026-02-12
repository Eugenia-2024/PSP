package classExercises_25enero;

import java.io.*;
import java.net.Socket;

public class TcpSocketCliente {
    private String serverIP;
    private int serverPort;
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private InputStreamReader isr;
    private BufferedReader br;
    private PrintWriter pw;

    public TcpSocketCliente(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        System.out.println("(Cliente) Estableciendo conexión...");
        socket = new Socket(serverIP, serverPort);
        os = socket.getOutputStream();
        is = socket.getInputStream();
        System.out.println("(Cliente) Conexión establecida.");
    }

    public void stop() throws IOException {
        System.out.println("(Cliente) Cerrando conexiones...");
        if (br != null) br.close();
        if (isr != null) isr.close();
        if (pw != null) pw.close();
        if (is != null) is.close();
        if (os != null) os.close();
        if (socket != null) socket.close();
        System.out.println("(Cliente) Conexiones cerradas.");
    }

    public void abrirCanalesdeTexto() {
        System.out.println("(Cliente) Abriendo canales de texto...");
        // Read channel
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        // Write channel
        pw = new PrintWriter(os, true);
        System.out.println("(Cliente) Canales de texto abiertos.");
    }

    public String leerMensajeTexto() throws IOException {
        return br.readLine(); // returns null if server disconnects
    }

    public void enviarMensajeTexto(String mensaje) {
        pw.println(mensaje);
    }

    public static void main(String[] args) {
        TcpSocketCliente cliente = new TcpSocketCliente("localhost", 49171);

        try {
            cliente.start();
            cliente.abrirCanalesdeTexto();

            // Thread to receive messages from server
            Thread receiver = new Thread(() -> {
                try {
                    while (true) {
                        String msg = cliente.leerMensajeTexto();
                        if (msg == null) {
                            System.out.println("(Cliente) El servidor se ha desconectado.");
                            break;
                        }
                        System.out.println("Servidor: " + msg);

                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("(Cliente) Chat terminado por el servidor.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("(Cliente) Error leyendo del servidor: " + e.getMessage());
                }
            });

            receiver.start();

            // Main thread sends messages from keyboard to server
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Cliente> ");
                String toSend = console.readLine();
                if (toSend == null) break;

                cliente.enviarMensajeTexto(toSend);

                if (toSend.equalsIgnoreCase("exit")) {
                    System.out.println("(Cliente) Chat terminado.");
                    break;
                }
            }

            cliente.stop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
