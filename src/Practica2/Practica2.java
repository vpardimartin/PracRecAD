package Practica2;

import java.io.IOException;

public class Practica2 {
    public static void execute() throws IOException {

        CargaDatos.inicializarDatosRegistros();

        registrosPersonalizados();

    }
    public static void registrosPersonalizados() throws IOException {
        int opcion;
        do {
            Menu.showRegistrosPersonalizados();
            opcion = Metodos.obtenerOpcion();
            switch (opcion) {
                case 1: // Mostrar todos los registros
                    Metodos.mostrarFicherosGuardados();
                    Metodos.mostrarRegistrosPersonalizados("soloMuestra");
                    break;
                case 2: // Añadir registro
                    Gestion.anyadirDatos();
                    break;
                case 3: // Recuperar registro
                    Gestion.recuperarDatos();
                    break;
                case 4: // Modificar registro
                    Gestion.modificarRegistro();
                    break;
                case 5:
                    Gestion.borrarRegistro();
                    break;
                case 6:
                    CargaDatos.crearFicheroPersonalizado();
                    break;
                case 7:
                    FicheroSecuencial.execute();
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