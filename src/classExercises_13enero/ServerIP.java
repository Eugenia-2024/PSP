package classExercises_13enero;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerIP {
    public static void main(String[] args){
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server waiting on port " +port+ "...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

            //Read 4 bytes (IPv4)
            int[] ipParts = new int[4];
            for (int i = 0; i<4; i++){
                ipParts[i] = dis.readInt();
            }

            //Build IP string
            String ip =
                    ipParts[0] +"."+ ipParts[1] +"."+ ipParts[2] +"."+ ipParts[3];

            System.out.println("Received IP address: " +ip);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}//class
