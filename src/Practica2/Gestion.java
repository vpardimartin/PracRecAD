package Practica2;

import java.io.*;

public class Gestion {
    public static void anyadirDatos() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean agregarOtroRegistro;
        Metodos.mostrarFicherosGuardados();

        String nombreFichero;
        do {
            // Pedir al usuario el nombre del fichero al que desea añadir datos
            do {
                System.out.println("Ingrese el nombre del fichero al que desea añadir datos:");
                nombreFichero = reader.readLine().trim();
                if (nombreFichero.isEmpty()) {
                    System.out.println("Error: El nombre del fichero no puede estar vacío.");
                }
            } while (nombreFichero.isEmpty());

            nombreFichero = "ficheros/" + nombreFichero +".txt";

            // Verificar si el fichero existe
            if (!CargaDatos.ficheroExiste(nombreFichero)) {
                System.out.println("El fichero no existe. Creando fichero personalizado...");
                CargaDatos.creacionFicheroPersonalizado(nombreFichero);
            } else {
                Metodos.mostrarRegistrosPersonalizados(nombreFichero);
            }

            // Leer los campos del fichero
            BufferedReader fileReader = new BufferedReader(new FileReader(nombreFichero));
            String primeraLinea = fileReader.readLine(); // Leemos la primera línea que contiene los nombres de los campos
            String[] campos = primeraLinea.split("#");
            fileReader.close();

            // Solicitar y almacenar los datos para cada campo
            String[] datos = new String[campos.length];
            for (int i = 0; i < campos.length; i++) {
                boolean registroDuplicado;
                do {
                    System.out.println("Ingrese el valor para el campo '" + campos[i] + "':");
                    datos[i] = reader.readLine();
                    registroDuplicado = Metodos.verificarRegistroDuplicado(nombreFichero, campos[i], datos[i]);

                    if (registroDuplicado) {
                        System.out.println("Ya existe un registro con el mismo valor en el campo '" + campos[i] + "'. Introduzca otro valor.");
                    } else if (datos[i].equalsIgnoreCase(campos[i])) {
                        System.out.println("El valor ingresado coincide con el nombre del campo. Introduzca otro valor.");
                        registroDuplicado = true;
                    }
                } while (registroDuplicado);
            }

            // Escribir los datos en el fichero
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(nombreFichero, true)); // True para que añada al final del fichero
            for (int i = 0; i < datos.length; i++) {
                fileWriter.write(datos[i]);
                if (i < datos.length - 1) {
                    fileWriter.write("#");
                }
            }
            fileWriter.newLine();
            fileWriter.close();

            System.out.println("Datos añadidos correctamente al fichero " + nombreFichero);
            Metodos.mostrarRegistrosPersonalizados(nombreFichero);

            // Preguntar al usuario si quiere agregar otro registro
            System.out.println("¿Desea agregar otro registro? (S/N)");
            String respuesta = reader.readLine();
            agregarOtroRegistro = respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("s");
        } while (agregarOtroRegistro);
    }

    public static void recuperarDatos() throws IOException {
        Metodos.mostrarFicherosGuardados();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Pedir al usuario el nombre del fichero del cual desea recuperar los datos
        System.out.println("Ingrese el nombre del fichero del cual desea recuperar los datos:");
        String nombreFichero = reader.readLine() + ".txt";
        nombreFichero = "ficheros/" + nombreFichero;

        // Verificar si el fichero existe
        if (!CargaDatos.ficheroExiste(nombreFichero)) {
            System.out.println("El fichero no existe.");
            return; // Salir del método si el fichero no existe
        }else{
            Metodos.mostrarRegistrosPersonalizados(nombreFichero);
        }

        // Leer los campos del fichero
        BufferedReader fileReader = new BufferedReader(new FileReader(nombreFichero));
        String primeraLinea = fileReader.readLine(); // Leemos la primera línea que contiene los nombres de los campos
        String[] campos = primeraLinea.split("#");
        fileReader.close();

        // Solicitar y almacenar el valor del primer campo para identificar el registro
        System.out.println("Ingrese el valor de " + campos[0] + " :");
        String valorCampoIdentificador = reader.readLine();

        // Buscar el registro correspondiente al valor del campo identificador
        BufferedReader fileReader2 = new BufferedReader(new FileReader(nombreFichero));
        String linea;
        boolean encontrado = false;
        while ((linea = fileReader2.readLine()) != null) {
            String[] partes = linea.split("#");
            if (partes[0].trim().equals(valorCampoIdentificador)) {
                encontrado = true;
                for (int i = 0; i < campos.length; i++) {
                    System.out.println(campos[i] + ": " + partes[i].trim());
                }
                break;
            }
        }
        fileReader2.close();

        if (!encontrado) {
            System.out.println("No se encontró ningún registro con el valor especificado para el campo identificador.");
        }
    }

    public static void modificarRegistro() throws IOException {
        Metodos.mostrarFicherosGuardados();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // Pedir al usuario el nombre del fichero del cual desea modificar el registro
        System.out.println("Ingrese el nombre del fichero del cual desea modificar el registro:");
        String nombreFichero = reader.readLine() + ".txt";

        // Verificar si el fichero existe
        if (!CargaDatos.ficheroExiste(nombreFichero)) {
            System.out.println("El fichero no existe.");
            return; // Salir del método si el fichero no existe
        } else {
            Metodos.mostrarRegistrosPersonalizados(nombreFichero);
        }

        // Leer los campos del fichero
        BufferedReader fileReader = new BufferedReader(new FileReader(nombreFichero));
        // Leer la primera línea que contiene los nombres de los campos
        String primeraLinea = fileReader.readLine();
        // Avanzar al siguiente registro
        fileReader.readLine();
        String[] campos = primeraLinea.split("#");
        fileReader.close();


        // Solicitar y almacenar el valor del primer campo para identificar el registro a modificar
        System.out.println("Ingrese el valor de " + campos[0] + " del registro que desea modificar:");
        String valorCampoIdentificador = reader.readLine();

        // Abrir el fichero en modo de lectura y escritura
        RandomAccessFile raf = new RandomAccessFile(nombreFichero, "rw");
        String linea;
        boolean encontrado = false;
        while ((linea = raf.readLine()) != null) {
            // Obtener el valor del primer campo de la línea actual
            String[] partes = linea.split("#");
            String valorCampo = partes[0].trim();
            // Comparar con el valor del campo identificador
            if (valorCampo.equals(valorCampoIdentificador)) {
                encontrado = true;
                // Mostrar el registro encontrado
                if(valorCampoIdentificador.equals(campos[0])){
                    System.out.println("Registro no encontrado.");
                }else {
                    System.out.println("Registro encontrado:");
                    int indice = 1;
                    for (int i = 0; i < campos.length; i++) {
                        System.out.println(indice + ". " + campos[i] + ": " + partes[i].trim());
                        indice++;
                    }
                    // Pedir al usuario que seleccione el campo a modificar
                    int campoAModificar;
                    do {
                        System.out.println("Ingrese el número del campo que desea modificar:");
                        campoAModificar = Metodos.obtenerOpcion() - 1; // Restamos 1 porque los índices de los campos empiezan desde 0
                        if (campoAModificar < 0) {
                            System.out.println("El número del campo debe ser mayor o igual a 1. Intente de nuevo.");
                        }
                    } while (campoAModificar < 0);

                    // Pedir al usuario el nuevo valor para el campo seleccionado
                    System.out.println("Ingrese el nuevo valor para el campo '" + campos[campoAModificar] + "':");
                    String nuevoValor = reader.readLine();
                    // Actualizar el campo en el registro
                    partes[campoAModificar] = nuevoValor;
                    // Construir la nueva línea con los datos modificados
                    String nuevaLinea = String.join("#", partes) + "\n";
                    // Calcular la posición en el fichero donde está el registro actual
                    long posicionRegistro = raf.getFilePointer() - linea.length() - 1;
                    // Ir a la posición del registro en el fichero
                    raf.seek(posicionRegistro);
                    // Escribir la línea modificada en el fichero
                    raf.writeBytes(nuevaLinea);
                    System.out.println("Registro modificado correctamente.");
                    Metodos.mostrarRegistrosPersonalizados(nombreFichero);
                    break;

                }

            }
        }
        raf.close();

        if (!encontrado) {
            System.out.println("No se encontró ningún registro con el valor especificado para el campo identificador.");
        }
    }

    public static void borrarRegistro() throws IOException {
        Metodos.mostrarFicherosGuardados();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String nombreFichero = null;
        String nombreFicheroACompactar = null;
        do {
            // Pedir al usuario el nombre del fichero del cual desea borrar el registro
            if (nombreFichero == null) {
                System.out.println("Ingrese el nombre del fichero del cual desea borrar el registro:");
                nombreFichero = reader.readLine() + ".txt";
                nombreFicheroACompactar = nombreFichero;
                nombreFichero = "ficheros/" + nombreFichero;
            } else {
                System.out.println("Continuar borrando en el fichero " + nombreFichero + "? (S/N)");
                String respuesta = reader.readLine();
                if (!(respuesta.equalsIgnoreCase("S") || respuesta.equalsIgnoreCase("s"))) {
                    break; // Salir del bucle si la respuesta no es "S" o "s"
                }
            }

            // Verificar si el fichero existe
            if (!CargaDatos.ficheroExiste(nombreFichero)) {
                System.out.println("El fichero no existe.");
                return; // Salir del método si el fichero no existe
            } else {
                Metodos.mostrarRegistrosPersonalizados(nombreFichero);
            }

            // Leer los campos del fichero
            BufferedReader fileReader = new BufferedReader(new FileReader(nombreFichero));
            String primeraLinea = fileReader.readLine(); // Leemos la primera línea que contiene los nombres de los campos
            String[] campos = primeraLinea.split("#");
            fileReader.close();

            // Solicitar y almacenar el valor del primer campo para identificar el registro a borrar
            System.out.println("Ingrese el valor de " + campos[0] + " del registro que desea borrar:");
            String valorCampoIdentificador = reader.readLine();

            // Buscar el registro correspondiente al valor del campo identificador
            BufferedReader fileReader2 = new BufferedReader(new FileReader(nombreFichero));
            String linea;
            boolean encontrado = false;
            int numeroLinea = 0;
            while ((linea = fileReader2.readLine()) != null) {
                numeroLinea++;
                String[] partes = linea.split("#");
                if (partes[0].trim().equals(valorCampoIdentificador)) {
                    encontrado = true;
                    // Marcar el registro como borrado insertando un carácter especial al principio
                    String registroBorrado = "*" + linea.substring(0);
                    // Actualizar el registro en el fichero
                    fileReader2.close();
                    BufferedReader fileReader3 = new BufferedReader(new FileReader(nombreFichero));
                    StringBuilder contenidoNuevo = new StringBuilder();
                    String lineaModificar;
                    int contadorLineas = 0;
                    while ((lineaModificar = fileReader3.readLine()) != null) {
                        contadorLineas++;
                        if (contadorLineas == numeroLinea) {
                            contenidoNuevo.append(registroBorrado).append("\n");
                        } else {
                            contenidoNuevo.append(lineaModificar).append("\n");
                        }
                    }
                    fileReader3.close();
                    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(nombreFichero));
                    fileWriter.write(contenidoNuevo.toString());
                    fileWriter.close();
                    System.out.println("Registro marcado como borrado correctamente.");
                    Metodos.mostrarRegistrosPersonalizados(nombreFichero);
                    break;
                }
            }
            fileReader2.close();

            if (!encontrado) {
                System.out.println("No se encontró ningún registro con el valor especificado para el campo identificador.");
            }
        } while (true); // El bucle se repetirá hasta que el usuario decida no borrar más registros
        CargaDatos.compactarFichero(nombreFichero, nombreFicheroACompactar); // Llamar al método para compactar el fichero
    }

}
