package practica3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ejercicio_4 {

    //Constantes para la conexión a la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";
    private static Connection conn;

    //Crea las tablas si no existen.
    public static void createTablesIfNotExists() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS EMPLEADOS(DNI_NIF CHAR(9) NOT NULL, NOMBRE VARCHAR(32) NOT NULL, PRIMARY KEY(DNI_NIF));");
            stmt.execute("CREATE TABLE IF NOT EXISTS PROYECTOS(NUM_PROY INTEGER AUTO_INCREMENT NOT NULL, NOMBRE VARCHAR(32) NOT NULL, DNI_NIF_JEFE_PROY CHAR(9) NOT NULL, F_INICIO DATE NOT NULL, F_FIN DATE, PRIMARY KEY(NUM_PROY), FOREIGN KEY FK_PROY_JEFE(DNI_NIF_JEFE_PROY) REFERENCES EMPLEADOS(DNI_NIF));");
            stmt.execute("CREATE TABLE IF NOT EXISTS ASIG_PROYECTOS(DNI_NIF_EMP CHAR(9), NUM_PROY INTEGER NOT NULL, F_INICIO DATE NOT NULL, F_FIN DATE, PRIMARY KEY(DNI_NIF_EMP,NUM_PROY, F_INICIO), FOREIGN KEY F_ASIG_EMP(DNI_NIF_EMP) REFERENCES EMPLEADOS(DNI_NIF), FOREIGN KEY F_ASIG_PROY(NUM_PROY) REFERENCES PROYECTOS(NUM_PROY));");
        }
    }

    //Borra las tablas.
    public static void dropTables() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS ASIG_PROYECTOS");
            stmt.execute("DROP TABLE IF EXISTS PROYECTOS");
            stmt.execute("DROP TABLE IF EXISTS EMPLEADOS");
        }
    }

    //Aañde o actualiza un empleado si ya existe
    public static boolean nuevoEmpleado(String dni, String nombre) throws SQLException {
        String sql = "INSERT INTO EMPLEADOS (DNI_NIF, NOMBRE) VALUES (?, ?) ON DUPLICATE KEY UPDATE NOMBRE = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, dni);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, nombre);  // Para actualizar el nombre en caso de que el DNI ya exista
            return preparedStatement.executeUpdate() > 0;
        }
    }


    public static void execute() {
        try {
            ejercicio_3 gestor = new ejercicio_3(); //Inicializa el gestor de proyectos
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Scanner scanner = new Scanner(System.in);
        List<Empleado> empleados = new ArrayList<>();


        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS); //Conexión con la base de datos.
            createTablesIfNotExists();  // Llama al método para crear las tablas si no existen.

            // Crea tantos empleados como quiera el usuario.
            System.out.println("Introduce el número de empleados que deseas agregar:");
            int numEmpleados = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            for (int i = 0; i < numEmpleados; i++) {
                System.out.println("Introduce el DNI del empleado:");
                String dni = scanner.nextLine();
                System.out.println("DNI ingresado: " + dni);
                Empleado empleado = new Empleado(dni);



                System.out.println("Introduce el nombre del empleado " + (i + 1) + ":");
                String nombre = scanner.nextLine();


                empleado.setNombre(nombre);
                empleado.save();

                if (nuevoEmpleado(dni, nombre)) {
                    System.out.println("Empleado " + nombre + " creado con éxito.");
                }
            }

            // Crea proyectos con datos predeterminados.
            List<Proyecto> proyectos = new ArrayList<>();

            // Proyecto 1
            proyectos.add(new Proyecto(0, null));
            proyectos.get(0).setNombre("Diseño Web");
            proyectos.get(0).setDNI_NIF_JEFE_PROY(empleados.get(0).getDNINIF());
            proyectos.get(0).save();

            // Proyecto 2
            proyectos.add(new Proyecto(0, null));
            proyectos.get(1).setNombre("Desarrollo de la aplicación");
            proyectos.get(1).setDNI_NIF_JEFE_PROY(empleados.get(0).getDNINIF());
            proyectos.get(1).setF_INICIO(Date.valueOf("2023-04-07"));
            proyectos.get(1).setF_FIN(Date.valueOf("2023-12-31"));
            proyectos.get(1).save();

            // Proyecto 3
            proyectos.add(new Proyecto(0, null));
            proyectos.get(2).setNombre("Marketing Digital");
            proyectos.get(2).setDNI_NIF_JEFE_PROY(empleados.get(0).getDNINIF());
            proyectos.get(2).save();

            // Proyecto 4
            proyectos.add(new Proyecto(0, null));
            proyectos.get(3).setNombre("Optimización SEO");
            proyectos.get(3).setDNI_NIF_JEFE_PROY(empleados.get(0).getDNINIF());
            proyectos.get(3).setF_INICIO(Date.valueOf("2023-01-01"));
            proyectos.get(3).setF_FIN(Date.valueOf("2023-12-31"));
            proyectos.get(3).save();

            // Asigna proyectos a empleados según a elección del usuario.
            for (int i = 0; i < proyectos.size(); i++) {
                System.out.println("¿A qué empleado deseas asignar el proyecto " + proyectos.get(i).getNombre() + "?");
                for (int j = 0; j < empleados.size(); j++) {
                    System.out.println((j + 1) + ". " + empleados.get(j).getNombre());
                }
                int empleadoSeleccionado = scanner.nextInt() - 1;

                AsignacionProyecto asignacion = new AsignacionProyecto(); // Crea una nueva asignación.
                asignacion.setDNI_NIF_EMP(empleados.get(empleadoSeleccionado).getDNINIF()); // Establece el DNI del empleado.
                asignacion.setNUM_PROY(proyectos.get(i).getNUM_PROY()); // Establece el número de proyecto.
                asignacion.save(); // Guarda la asignación en la base de datos.

                System.out.println(empleados.get(empleadoSeleccionado).getNombre() + " asignado al proyecto " + proyectos.get(i).getNombre() + ".");
            }

            dropTables();
            System.out.println("Tablas borradas. Regresando al menú principal.");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
