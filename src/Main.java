import Practica1.Practica1;
import Practica2.Practica2;
import practica3.Practica3;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        menuPrincipal();
    }
    public static void menuPrincipal() throws IOException {
        int opcion;
        do {
            MenuPrincipal.showMenuPrincipal();
            opcion = MetodosGeneral.obtenerOpcion();
            switch (opcion) {
                case 1:
                    Practica1.execute();
                    break;
                case 2:
                    Practica2.execute();
                    break;
                case 3:
                    Practica3.execute();
                    break;
                case 4:
                    break;
                case 0:
                    System.out.println("Saliendo del programa.");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elige una opción válida.");
                    break;
            }
        } while (opcion != 0);
    }
}