import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MetodosGeneral {
    public static int obtenerOpcion() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String input = br.readLine();
            System.out.println();
            return Integer.parseInt(input);
        } catch (NumberFormatException | IOException e) {
            System.out.println("Error: Ingrese un número válido.");
            return -1;
        }
    }
}
