package practica3;

import java.sql.*;
import java.util.Scanner;

public class ejercicio_5 {

    //Constantes para la conexión a la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";
    private static int opcion;

    public static void execute() {

        //Conexión con la base datos.
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Scanner scanner = new Scanner(System.in);

            // Crea tablas si no existen
            createTables(conn);

            // Inserta proyectos predeterminados
            insertDefaultProjects(conn);

            // Pide datos del empleado
            System.out.println("Introduce el DNI del empleado:");
            String dni = scanner.nextLine();

            System.out.println("Introduce el nombre del empleado:");
            String nombre = scanner.nextLine();

            insertEmpleado(conn, dni, nombre);

            // Muestra los proyectos activos
            System.out.println("Hay 4 proyectos activos:");
            System.out.println("1. Diseño Web");
            System.out.println("2. Desarrollo de la aplicación");
            System.out.println("3. Marketing Digital");
            System.out.println("4. Optimización SEO");


            // Asigna empleado a proyectos
            asignaEmpAProyecto(dni, conn);

            // Muestra las asignaciones
            mostrarAsignaciones(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //Crea las tablas si no existen.
    private static void createTables(Connection conn) throws SQLException {
        String createEmpleados = "CREATE TABLE IF NOT EXISTS EMPLEADOS (DNI_NIF VARCHAR(9) PRIMARY KEY, NOMBRE VARCHAR(30))";
        String createProyectos = "CREATE TABLE IF NOT EXISTS PROYECTOS (NUM_PROY INT PRIMARY KEY, NOMBRE VARCHAR(30), DNI_NIF_JEFE_PROY VARCHAR(9), F_INICIO DATE, F_FIN DATE, FOREIGN KEY (DNI_NIF_JEFE_PROY) REFERENCES EMPLEADOS(DNI_NIF))";
        String createAsigProyectos = "CREATE TABLE IF NOT EXISTS ASIG_PROYECTOS (DNI_NIF_EMP VARCHAR(9), NUM_PROY INT, F_INICIO DATE, F_FIN DATE, PRIMARY KEY (DNI_NIF_EMP, NUM_PROY), FOREIGN KEY (DNI_NIF_EMP) REFERENCES EMPLEADOS(DNI_NIF), FOREIGN KEY (NUM_PROY) REFERENCES PROYECTOS(NUM_PROY))";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createEmpleados);
            stmt.execute(createProyectos);
            stmt.execute(createAsigProyectos);
        }
    }

    //Crea jefes ficticios para cada uno de los proyectos predeterminados.
    private static void insertDefaultProjects(Connection conn) throws SQLException {
        // Insertar empleados ficticios
        String insertEmpleado = "INSERT INTO EMPLEADOS (DNI_NIF, NOMBRE) VALUES (?, ?) ON DUPLICATE KEY UPDATE NOMBRE = VALUES(NOMBRE)";
        try (PreparedStatement stmt = conn.prepareStatement(insertEmpleado)) {
            stmt.setString(1, "12345678A");
            stmt.setString(2, "Jefe Ficticio 1");
            stmt.executeUpdate();

            stmt.setString(1, "23456789B");
            stmt.setString(2, "Jefe Ficticio 2");
            stmt.executeUpdate();

            stmt.setString(1, "34567890C");
            stmt.setString(2, "Jefe Ficticio 3");
            stmt.executeUpdate();

            stmt.setString(1, "45678901D");
            stmt.setString(2, "Jefe Ficticio 4");
            stmt.executeUpdate();
        }

        // Inserta los proyectos predeterminados.
        String insertProyecto = "INSERT INTO PROYECTOS (NUM_PROY, NOMBRE, DNI_NIF_JEFE_PROY, F_INICIO) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE NOMBRE = VALUES(NOMBRE), DNI_NIF_JEFE_PROY = VALUES(DNI_NIF_JEFE_PROY), F_INICIO = VALUES(F_INICIO)";
        try (PreparedStatement stmt = conn.prepareStatement(insertProyecto)) {
            // Proyecto 1
            stmt.setInt(1, 1);
            stmt.setString(2, "Diseño Web");
            stmt.setString(3, "12345678A");
            stmt.setDate(4, Date.valueOf("2023-01-01"));
            stmt.executeUpdate();

            // Proyecto 2
            stmt.setInt(1, 2);
            stmt.setString(2, "Desarrollo de la aplicación");
            stmt.setString(3, "23456789B");
            stmt.setDate(4, Date.valueOf("2023-02-01"));
            stmt.executeUpdate();

            // Proyecto 3
            stmt.setInt(1, 3);
            stmt.setString(2, "Marketing Digital");
            stmt.setString(3, "34567890C");
            stmt.setDate(4, Date.valueOf("2023-03-01"));
            stmt.executeUpdate();

            // Proyecto 4
            stmt.setInt(1, 4);
            stmt.setString(2, "Optimización SEO");
            stmt.setString(3, "45678901D");
            stmt.setDate(4, Date.valueOf("2023-04-01"));
            stmt.executeUpdate();
        }
    }


    //Inserta empleados o los actualiza si ya existe.
    private static void insertEmpleado(Connection conn, String dni, String nombre) throws SQLException {
        String insertEmpleado = "INSERT INTO EMPLEADOS (DNI_NIF, NOMBRE) VALUES (?, ?) ON DUPLICATE KEY UPDATE NOMBRE = VALUES(NOMBRE)";
        try (PreparedStatement stmt = conn.prepareStatement(insertEmpleado)) {
            stmt.setString(1, dni);
            stmt.setString(2, nombre);
            stmt.executeUpdate();
        }
    }

    //Crea la asignación del empleado al proyecto elegido por el usuario.
    private static void asignaEmpAProyecto(String dni, Connection conn) throws SQLException {
        System.out.println("Introduce el número del proyecto al que quieres asignar el empleado:");
        Scanner scanner = new Scanner(System.in);
        int numProy = scanner.nextInt();
        scanner.nextLine();  

        String sql = "INSERT INTO ASIG_PROYECTOS (DNI_NIF_EMP, NUM_PROY, F_INICIO) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.setInt(2, numProy);
            stmt.setDate(3, Date.valueOf("2022-01-01"));
            stmt.executeUpdate();
        }
    }

    //Muestra una lista con las asignaciones empleado - proyecto.
    private static void mostrarAsignaciones(Connection conn) throws SQLException {
        String sql = "SELECT p.NOMBRE AS Proyecto, e.NOMBRE AS Empleado, e.DNI_NIF AS DNI FROM PROYECTOS p JOIN ASIG_PROYECTOS a ON p.NUM_PROY = a.NUM_PROY JOIN EMPLEADOS e ON a.DNI_NIF_EMP = e.DNI_NIF";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Proyecto: " + rs.getString("Proyecto") + " | Empleado: " + rs.getString("Empleado") + " | DNI: " + rs.getString("DNI"));
            }
        }
    }
}
