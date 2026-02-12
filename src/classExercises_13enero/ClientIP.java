package classExercises_13enero;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientIP {
    public static void main(String[] args) {

        String host = "www.paraninfo.es";
        String serverAddress = "localhost";
        int port = 5000;

        try {
            // 1. Obtain IP address using InetAddress
            InetAddress inet = InetAddress.getByName(host);

            // 2. Get raw IP bytes
            byte[] ipBytes = inet.getAddress();

            // 3. Open socket to server
            Socket socket = new Socket(serverAddress, port);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            // 4. Send IP byte by byte
            for (byte b : ipBytes) {
                // Convert signed byte (-128..127) to unsigned int (0..255)
                int value = b & 0xFF;
                dos.writeInt(value);
            }

            System.out.println("IP address sent successfully.");

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}//class

