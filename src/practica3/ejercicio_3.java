package practica3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class ejercicio_3 {
    //Esto es el ejercicio 3.
    //Constantes para la conexión a la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";
    private Connection conn;

    //Conecta a la base de datos y crea las tablas si no existen.
    public ejercicio_3() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        createTablesIfNotExists();
    }

    //Crea las tablas si no existen.
    public void createTablesIfNotExists() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS EMPLEADOS(DNI_NIF CHAR(9) NOT NULL, NOMBRE VARCHAR(32) NOT NULL, PRIMARY KEY(DNI_NIF));");
            stmt.execute("CREATE TABLE IF NOT EXISTS PROYECTOS(NUM_PROY INTEGER AUTO_INCREMENT NOT NULL, NOMBRE VARCHAR(32) NOT NULL, DNI_NIF_JEFE_PROY CHAR(9) NOT NULL, F_INICIO DATE NOT NULL, F_FIN DATE, PRIMARY KEY(NUM_PROY), FOREIGN KEY FK_PROY_JEFE(DNI_NIF_JEFE_PROY) REFERENCES EMPLEADOS(DNI_NIF));");
            stmt.execute("CREATE TABLE IF NOT EXISTS ASIG_PROYECTOS(DNI_NIF_EMP CHAR(9), NUM_PROY INTEGER NOT NULL, F_INICIO DATE NOT NULL, F_FIN DATE, PRIMARY KEY(DNI_NIF_EMP,NUM_PROY, F_INICIO), FOREIGN KEY F_ASIG_EMP(DNI_NIF_EMP) REFERENCES EMPLEADOS(DNI_NIF), FOREIGN KEY F_ASIG_PROY(NUM_PROY) REFERENCES PROYECTOS(NUM_PROY));");
        }
    }

    //Agrega un nuevo empleado a la tabla "Empleados".
    public boolean nuevoEmpleado(String dni, String nombre) throws SQLException {
        String sql = "INSERT INTO EMPLEADOS (DNI_NIF, NOMBRE) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, dni);
            preparedStatement.setString(2, nombre);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    //Agrega un nuevo proyecto a la tabla "Proyectos".
    public int nuevoProyecto(String nombre, String dniJefe, Date fInicio, Date fFin) throws SQLException {
        String sql = "INSERT INTO PROYECTOS (NOMBRE, DNI_NIF_JEFE_PROY, F_INICIO, F_FIN) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, dniJefe);
            preparedStatement.setDate(3, fInicio == null ? new Date(System.currentTimeMillis()) : fInicio);
            preparedStatement.setDate(4, fFin);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        }
    }

    //Asigna un proyecto a un empleado en la tabla "AsignaEmpAProyecto".
    public boolean asignaEmpAProyecto(String dni, int numProy, Date fInicio, Date fFin) throws SQLException {
        String sql = "INSERT INTO ASIG_PROYECTOS (DNI_NIF_EMP, NUM_PROY, F_INICIO, F_FIN) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, dni);
            preparedStatement.setInt(2, numProy);
            preparedStatement.setDate(3, fInicio == null ? new Date(System.currentTimeMillis()) : fInicio);
            preparedStatement.setDate(4, fFin);
            return preparedStatement.executeUpdate() > 0;
        }
    }

    //Elimina las tablas.
    public void dropTables() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS ASIG_PROYECTOS");
            stmt.execute("DROP TABLE IF EXISTS PROYECTOS");
            stmt.execute("DROP TABLE IF EXISTS EMPLEADOS");
        }
    }

    public static void execute() {
        try {
            ejercicio_3 gestor = new ejercicio_3();
            Scanner scanner = new Scanner(System.in);
            List<String> empleados = new ArrayList<>();

            // Crea empleados
            System.out.println("Introduce el número de empleados que deseas agregar:");
            int numEmpleados = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < numEmpleados; i++) {
                System.out.println("Introduce el DNI del empleado " + (i + 1) + ":");
                String dni = scanner.nextLine();
                empleados.add(dni);

                System.out.println("Introduce el nombre del empleado " + (i + 1) + ":");
                String nombre = scanner.nextLine();

                if (gestor.nuevoEmpleado(dni, nombre)) {
                    empleados.add(dni);
                    System.out.println("Empleado " + nombre + " creado con éxito.");
                }
            }

            // Crea proyectos predeterminados
            int[] proyectos = new int[4];
            proyectos[0] = gestor.nuevoProyecto("Diseño Web", empleados.get(0), null, null);
            proyectos[1] = gestor.nuevoProyecto("Desarrollo de la aplicación", empleados.get(0), Date.valueOf("2023-04-07"), Date.valueOf("2023-12-31"));
            proyectos[2] = gestor.nuevoProyecto("Marketing Digital", empleados.get(0), null, null);
            proyectos[3] = gestor.nuevoProyecto("Optimización SEO", empleados.get(0), Date.valueOf("2023-01-01"), Date.valueOf("2023-12-31"));

            // Asigna proyectos
            for (int i = 0; i < proyectos.length; i++) {
                System.out.println("¿A qué empleado deseas asignar el proyecto " + (i + 1) + "?");
                for (int j = 0; j < empleados.size(); j++) {
                    System.out.println((j + 1) + ". " + empleados.get(j));
                }
                int empleadoSeleccionado = scanner.nextInt() - 1;
                if (gestor.asignaEmpAProyecto(empleados.get(empleadoSeleccionado), proyectos[i], null, null)) {
                    System.out.println(empleados.get(empleadoSeleccionado) + " asignado al proyecto " + (i + 1) + ".");
                }
            }

            // Borra las tablas al finalizar
            System.out.println("Borrando tablas...");
            gestor.dropTables();
            System.out.println("Tablas borradas. Regresando al menú principal.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
