package Practica2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CargaDatos {

    public static final String NOM_FICHERO = "ficheros/registros.txt";

    public static void inicializarDatosRegistros() {
        File file = new File(NOM_FICHERO);

        // Verificar si el fichero ya existe
        if (file.exists()) {
            // Puedes decidir si quieres mantener el archivo existente o borrarlo
            System.out.println("Fichero " + NOM_FICHERO + " encontrado.");
        } else {
            // Crear el archivo con datos predefinidos
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                String linea = """
                        dni#nombre
                        123456789A#Paco
                        123456789B#Maria
                        987654321C#Ana
                        555555555D#Juan
                        111111111E#Luisa""";
                bufferedWriter.write(linea);
                bufferedWriter.newLine();
                System.out.println("Generando fichero " + NOM_FICHERO + " ...");
            } catch (IOException e) {
                throw new RuntimeException("Error al escribir en el archivo", e);
            }
        }
        System.out.println("Datos de " + NOM_FICHERO + " cargados correctamente.\n");
        // Independientemente de si el archivo existía o no, inicializar el array
        inicializarArray();
    }



    public static List<Registro> inicializarArray() {
        File file = new File(NOM_FICHERO);
        List<Registro> registrosList = new ArrayList<>();

        // Leer los datos y almacenarlos en la lista
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                String[] campos = readLine.split("#");
                Registro registro = new Registro(campos);
                registrosList.add(registro);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo", e);
        }
        return registrosList;
    }

    public static void crearFicheroPersonalizado() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            String nombreFichero;
            do {
                System.out.println("Ingrese el nombre del fichero:");
                nombreFichero = reader.readLine().trim();
                if (nombreFichero.isEmpty()) {
                    System.out.println("Error: El nombre del fichero no puede estar vacío.");
                }
            } while (nombreFichero.isEmpty());

            nombreFichero = "ficheros/" + nombreFichero + ".txt";
            creacionFicheroPersonalizado(nombreFichero);
            System.out.println("Fichero personalizado creado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al crear el fichero personalizado: " + e.getMessage());
        }
    }

    public static void creacionFicheroPersonalizado(String nombreFichero) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        File file = new File(nombreFichero);

        if (ficheroExiste(nombreFichero)) {
            System.out.println("El fichero " + nombreFichero + " ya existe.");
            System.out.println("Ingrese otro nombre para el fichero:");
            nombreFichero = reader.readLine() + ".txt";
            creacionFicheroPersonalizado(nombreFichero);
        } else {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                // Pedir los nombres de los campos
                System.out.println("Ingrese la cantidad de campos:");

                int cantidadCampos;
                do {
                    cantidadCampos = Metodos.obtenerOpcion();
                    if (cantidadCampos <= 0) {
                        System.out.println("Error: La cantidad de campos debe ser un número positivo mayor que cero.");
                        System.out.println("Ingrese la cantidad de campos nuevamente:");
                    }
                } while (cantidadCampos <= 0);

                List<String> campos = new ArrayList<>();
                for (int i = 0; i < cantidadCampos; i++) {
                    System.out.println("Ingrese el nombre del campo " + (i + 1) + ":");
                    campos.add(reader.readLine());
                }

                // Escribir los nombres de los campos en la primera línea del archivo
                for (int i = 0; i < campos.size(); i++) {
                    bufferedWriter.write(campos.get(i));
                    if (i < campos.size() - 1) {
                        bufferedWriter.write("#");
                    }
                }
                bufferedWriter.newLine();
            }
        }
    }
    public static boolean ficheroExiste(String nombreFichero) {
        File file = new File(nombreFichero);
        return file.exists();
    }
    public static void compactarFichero(String nombrefichero, String nombreFicheroACompactar) {
        // Nombre del fichero temporal

        String nombreFicheroTemporal = "ficheros/temporal_" + nombreFicheroACompactar;


        try {
            // Abrir el fichero original para lectura
            BufferedReader bufferedReader = new BufferedReader(new FileReader(nombrefichero));
            // Abrir el fichero temporal para escritura
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nombreFicheroTemporal));

            // Leer el fichero original línea por línea
            String linea;
            while ((linea = bufferedReader.readLine()) != null) {
                // Si la línea no está marcada como borrada, escribirla en el fichero temporal
                if (!linea.startsWith("*")) {
                    bufferedWriter.write(linea);
                    bufferedWriter.newLine();
                }
            }

            // Cerrar los flujos de lectura y escritura
            bufferedReader.close();
            bufferedWriter.close();

            // Eliminar el fichero original
            File ficheroOriginal = new File(NOM_FICHERO);
            ficheroOriginal.delete();

            // Renombrar el fichero temporal al nombre del fichero original
            File ficheroTemporal = new File(nombreFicheroTemporal);
            ficheroTemporal.renameTo(ficheroOriginal);

            System.out.println("Fichero compactado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al compactar el fichero: " + e.getMessage());
        }
    }
}
