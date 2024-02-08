package Practica2;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FicheroSecuencial {
    public static final String FICHERO_REGISTROS = "ficheros/secuencial.txt";
    private static final int ID_LENGTH = 6;
    private static final int NOMBRE_LENGTH = 15;
    private static final int APELLIDO_LENGTH = 30;
    private static final int REGISTRO_LENGTH = ID_LENGTH + NOMBRE_LENGTH + APELLIDO_LENGTH;

    public static void execute() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int opcion;

        do {
            mostrarMenu();
            opcion = Metodos.obtenerOpcion();

            // Leer la línea adicional para eliminar cualquier retorno de carro adicional

            switch (opcion) {
                case 1:
                    // Insertar nuevo registro
                    insertarRegistro(reader);
                    break;
                case 2:
                    // Mostrar todos los registros
                    mostrarRegistros();
                    break;
                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, elige una opción válida.");
                    break;
            }
        } while (opcion != 0);
    }

    public static void mostrarMenu() {
        System.out.println("\n--- Menú Fichero Secuencial ---");
        System.out.println("1. Insertar nuevo registro");
        System.out.println("2. Mostrar todos los registros");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opción: ");
    }

    public static void insertarRegistro(BufferedReader reader) {
        System.out.println("Insertar nuevo registro:");
        try {
            int id;
            do {
                System.out.print("ID (máx. " + ID_LENGTH + " dígitos): ");
                id = obtenerEntero(reader);
            } while (id < 0 || id >= 1000000);

            System.out.print("Nombre (máx. " + NOMBRE_LENGTH + " caracteres): ");
            String nombre = obtenerTextoConLongitud(reader, NOMBRE_LENGTH);
            System.out.print("Apellidos (máx. " + APELLIDO_LENGTH + " caracteres): ");
            String apellidos = obtenerTextoConLongitud(reader, APELLIDO_LENGTH);

            insertarRegistroEnFichero(id, nombre, apellidos);
            System.out.println("Registro insertado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al insertar el registro: " + e.getMessage());
        }
    }




    private static int obtenerEntero(BufferedReader reader) throws IOException {
        int valor = -1; // Inicializamos con un valor que indica que no se ha ingresado un valor válido
        do {
            try {
                String input = reader.readLine().trim();
                valor = Integer.parseInt(input);
                if (valor < 0 || valor >= 1000000) {
                    System.out.print("Por favor, introduce un número de máximo " +  ID_LENGTH + " dígitos: ");
                    valor = -1; // Reiniciamos el valor para solicitar la entrada nuevamente
                }
            } catch (NumberFormatException e) {
                System.out.print("Por favor, introduce un número válido: ");
                valor = -1; // Reiniciamos el valor para solicitar la entrada nuevamente
            }
        } while (valor == -1);
        return valor;
    }



    private static String obtenerTextoConLongitud(BufferedReader reader, int longitud) throws IOException {
        String texto;
        do {
            texto = reader.readLine().trim();
            if (texto.length() > longitud) {
                System.out.println("Error: El texto no puede exceder la longitud de " + longitud + " caracteres.");
                if(longitud==NOMBRE_LENGTH){
                    System.out.print("Introduce un nombre valido: ");
                }else {
                    System.out.print("Introduce apellidos validos: ");
                }
            }
        } while (texto.length() > longitud);
        return texto;
    }



    public static void mostrarRegistros() {
        System.out.println("\n--- Registros en el fichero secuencial ---");
        List<String> registros = leerRegistros();
        registros.sort(Comparator.comparingInt(r -> Integer.parseInt(r.substring(0, ID_LENGTH).trim())));

        System.out.printf("%-1s(%-" + (ID_LENGTH) + "s) %-1s(%-" + (NOMBRE_LENGTH) + "s) %-1s(%-" + (APELLIDO_LENGTH) + "s)%n", "ID", ID_LENGTH, "Nombre", NOMBRE_LENGTH, "Apellidos", APELLIDO_LENGTH);

        for (String registro : registros) {
            String id = registro.substring(0, ID_LENGTH).trim();
            String nombre = registro.substring(ID_LENGTH, ID_LENGTH + NOMBRE_LENGTH).trim();
            String apellidos = registro.substring(ID_LENGTH + NOMBRE_LENGTH).trim();
            System.out.printf("%-9s%-15s%-20s%n", id, nombre, apellidos);
        }
    }







    private static void insertarRegistroEnFichero(int id, String nombre, String apellidos) throws IOException {
        String nuevoRegistro = String.format("%-" + ID_LENGTH + "s%-" + NOMBRE_LENGTH + "s%-" + APELLIDO_LENGTH + "s", id, nombre, apellidos);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FICHERO_REGISTROS, true))) {
            writer.write(nuevoRegistro);
            writer.newLine();
        }
    }

    private static List<String> leerRegistros() {
        List<String> registros = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FICHERO_REGISTROS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                if (linea.length() >= REGISTRO_LENGTH) {
                    registros.add(linea);
                } else {
                    System.out.println("Error: la línea '" + linea + "' no tiene la longitud esperada y será ignorada.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
        return registros;
    }

}
