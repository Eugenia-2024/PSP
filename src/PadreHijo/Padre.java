package PadreHijo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;


	public class Padre {

	    public static void main(String[] args) {

	        // Ejecutar el proceso hijo
	    	ProcessBuilder pb = new ProcessBuilder("java", "Hijo");
	        pb.redirectErrorStream(true); // mezcla stdout y stderr

	        try {
	            Process hijo = pb.start();

	            // Enviar texto al hijo
	            PrintStream ps = new PrintStream(hijo.getOutputStream());
	            ps.println("Hola desde el padre");
	            ps.flush();
	            ps.close(); // importante: así el hijo sabe que ya no habrá más datos

	            // Leer la respuesta del hijo
	            BufferedReader br = new BufferedReader(
	                    new InputStreamReader(hijo.getInputStream())
	            );

	            String respuesta = br.readLine();
	            System.out.println("El padre recibió: " + respuesta);

	            hijo.waitFor();

	        } catch (IOException | InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}
