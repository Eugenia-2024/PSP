package PadreHijo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Hijo {

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in)
        );

        try {
            // Leer línea enviada por el padre
            String linea = br.readLine();

            // Añadir texto
            String modificada = linea + " devuelta por el hijo";

            // Mostrar por salida estándar
            System.out.println(modificada);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
