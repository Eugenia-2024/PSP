package classExercises_25enero;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocketServer {
    private ServerSocket serverSocket;
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    private InputStreamReader isr;
    private BufferedReader br;
    private PrintWriter pw;

    public TcpSocketServer(int puerto) throws IOException {
        serverSocket = new ServerSocket(puerto);
    }

    public void start() throws IOException {
        System.out.println("(Servidor) Esperando conexiones...");
        socket = serverSocket.accept();
        os = socket.getOutputStream();
        is = socket.getInputStream();
        System.out.println("(Servidor) Conexión establecida");
    }

    public void stop() throws IOException {
        System.out.println("(Servidor) Cerrando conexiones...");
        if (br != null) br.close();
        if (isr != null) isr.close();
        if (pw != null) pw.close();
        if (is != null) is.close();
        if (os != null) os.close();
        if (socket != null) socket.close();
        if (serverSocket != null) serverSocket.close();
        System.out.println("(Servidor) Conexiones cerradas.");
    }

    public void abrirCanalesdeTexto() {
        System.out.println("(Servidor) Abriendo canales de texto...");
        // Read channel
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        // Write channel (autoFlush=true so each println is sent immediately)
        pw = new PrintWriter(os, true);
        System.out.println("(Servidor) Canales de texto abiertos.");
    }

    public String leerMensajeTexto() throws IOException {
        return br.readLine(); // returns null if client disconnects
    }

    public void enviarMensajeTexto(String mensaje) {
        pw.println(mensaje);
    }

    public static void main(String[] args) {
        int puerto = 49171;

        try {
            TcpSocketServer servidor = new TcpSocketServer(puerto);
            servidor.start();
            servidor.abrirCanalesdeTexto();

            // Thread to receive messages from client
            Thread receiver = new Thread(() -> {
                try {
                    while (true) {
                        String msg = servidor.leerMensajeTexto();
                        if (msg == null) {
                            System.out.println("(Servidor) El cliente se ha desconectado.");
                            break;
                        }
                        System.out.println("Cliente: " + msg);

                        if (msg.equalsIgnoreCase("exit")) {
                            System.out.println("(Servidor) Chat terminado por el cliente.");
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("(Servidor) Error leyendo del cliente: " + e.getMessage());
                }
            });

            receiver.start();

            // Main thread sends messages from keyboard to client
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.print("Servidor> ");
                String toSend = console.readLine();
                if (toSend == null) break;

                servidor.enviarMensajeTexto(toSend);

                if (toSend.equalsIgnoreCase("exit")) {
                    System.out.println("(Servidor) Chat terminado.");
                    break;
                }
            }

            servidor.stop();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
