package Practica2;

import java.io.*;

public class Metodos {
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

    public static void mostrarFicherosGuardados() {
        System.out.println("-- Lista de ficheros guardados --");
        File directory = new File("ficheros");
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            int index = 1;
            for (File file : files) {
                if(!file.getName().equals("secuencial.txt")){
                    if (index<10){
                        System.out.println("0" + index + ". " + file.getName());
                    }else{
                        System.out.println(index + ". " + file.getName());
                    }
                    index++;
                }
            }
        } else {
            System.out.println("No se encontraron ficheros guardados.");
        }
        System.out.println();
    }

    public static void mostrarRegistrosPersonalizados(String nombreFichero) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            if (nombreFichero.equals("soloMuestra")) {
                // Pedir al usuario el nombre del fichero del cual desea ver los datos
                System.out.println("Ingrese el nombre del fichero del cual desea ver los datos:");
                nombreFichero = reader.readLine() + ".txt";
            }
            // Verificar si el fichero existe
            if (!CargaDatos.ficheroExiste(nombreFichero)) {
                System.out.println("El fichero no existe.");
                return; // Salir del método si el fichero no existe
            }
            if(!nombreFichero.equals(FicheroSecuencial.FICHERO_REGISTROS)){
                // Mostrar los registros del fichero
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nombreFichero))) {
                    String camposLinea = bufferedReader.readLine(); // Leer la primera línea que contiene los nombres de los campos
                    String[] nombresCampos = camposLinea.split("#"); // Dividir la línea en los nombres de los campos
                    int numCampos = nombresCampos.length;

                    // Mostrar la cabecera con los nombres de los campos
                    System.out.printf("%-5s", "Nº");
                    for (String nombreCampo : nombresCampos) {
                        System.out.printf("%-20s", nombreCampo);
                    }
                    System.out.println();

                    String linea = bufferedReader.readLine(); // Leer la primera línea de datos
                    int i = 1;
                    while (linea != null) {
                        // Mostrar el número de registro y los datos de cada línea si no comienza con "*"
                        if (!linea.startsWith("*")) {
                            System.out.printf("%-5d", i);
                            String[] datos = linea.split("#");
                            for (String dato : datos) {
                                System.out.printf("%-20s", dato); // Alinear los datos con los nombres de los campos
                            }
                            System.out.println();
                            i++;
                        }
                        linea = bufferedReader.readLine(); // Leer la siguiente línea de datos
                    }
                    System.out.println();
                    // Mostrar el mensaje "Pulsa intro para continuar"
                    System.out.print("Pulsa intro para continuar.");
                    reader.readLine(); // Espera hasta que se presione Enter
                    System.out.println();
                }
            }

        } catch (IOException e) {
            System.out.println("Error de E/S: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean verificarRegistroDuplicado(String nombreFichero, String campo, String valor) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(nombreFichero));
        String linea;
        boolean encontrado = false;
        // Leer la primera línea (nombres de campos) y descartarla
        fileReader.readLine();
        while ((linea = fileReader.readLine()) != null) {
            String[] partes = linea.split("#");
            if (partes[0].trim().equals(valor.trim())) {
                encontrado = true;
                break;
            }
        }
        fileReader.close();
        return encontrado;
    }

}
