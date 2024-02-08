package practica3;

import java.sql.*;
import java.util.Scanner;

public class ejercicio_2 {

    //Constantes para la conexión a la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";

    public static void execute() {
        // Conexión con la base de datos.
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Introduce el nombre de la tabla (EMPLEADOS, PROYECTOS, ASIG_PROYECTOS): ");
            String tableName = scanner.nextLine();

            createTableIfNotExists(conn, tableName); // Crea la tabla si no existe.
            predefined_data(conn, tableName); // Inserta datos predefinidos.

            // Muestra los datos de la tabla seleccionada.
            try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                 ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                int currentRow = 0;
                String comando;

                // Navegación entre los registros.
                while (true) {
                    if (rs.absolute(currentRow + 1)) {
                        show_row(rs, currentRow + 1, columnCount, metaData); // Mostrar fila actual.
                    } else {
                        System.out.println("No se encontró una fila con ese número.");
                    }

                    // Comandos para moverte entre los registros.
                    System.out.print("k - siguiente, d - anterior, . salir:");
                    comando = scanner.nextLine();

                    if (comando.equals(".")) {
                        break;
                    } else if (comando.equals("k")) {
                        currentRow++;
                    } else if (comando.equals("d")) {
                        currentRow--;
                    } else if (is_int(comando)) {
                        currentRow = Integer.parseInt(comando) - 1;
                    } else {
                        System.out.println("Comando inválido.");
                    }
                }
            }

            dropTable(conn, tableName);

        } catch (SQLException e) {
            show_sql_error(e); //
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    //Crea las tablas si no existen.
    private static void createTableIfNotExists(Connection conn, String tableName) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            if ("EMPLEADOS".equalsIgnoreCase(tableName) || "PROYECTOS".equalsIgnoreCase(tableName) || "ASIG_PROYECTOS".equalsIgnoreCase(tableName)) {
                stmt.execute("CREATE TABLE IF NOT EXISTS EMPLEADOS(DNI_NIF CHAR(9) NOT NULL, NOMBRE VARCHAR(32) NOT NULL, PRIMARY KEY(DNI_NIF));");
            }
            if ("PROYECTOS".equalsIgnoreCase(tableName) || "ASIG_PROYECTOS".equalsIgnoreCase(tableName)) {
                stmt.execute("CREATE TABLE IF NOT EXISTS PROYECTOS(NUM_PROY INTEGER AUTO_INCREMENT NOT NULL, NOMBRE VARCHAR(32) NOT NULL, DNI_NIF_JEFE_PROY CHAR(9) NOT NULL, F_INICIO DATE NOT NULL, F_FIN DATE, PRIMARY KEY(NUM_PROY), FOREIGN KEY FK_PROY_JEFE(DNI_NIF_JEFE_PROY) REFERENCES EMPLEADOS(DNI_NIF));");
            }
            if ("ASIG_PROYECTOS".equalsIgnoreCase(tableName)) {
                stmt.execute("CREATE TABLE IF NOT EXISTS ASIG_PROYECTOS(DNI_NIF_EMP CHAR(9), NUM_PROY INTEGER NOT NULL, F_INICIO DATE NOT NULL, F_FIN DATE, PRIMARY KEY(DNI_NIF_EMP,NUM_PROY, F_INICIO), FOREIGN KEY F_ASIG_EMP(DNI_NIF_EMP) REFERENCES EMPLEADOS(DNI_NIF), FOREIGN KEY F_ASIG_PROY(NUM_PROY) REFERENCES PROYECTOS(NUM_PROY));");
            }
        }
    }

    //Introduce los datos predefinidos en las tablas
    private static void predefined_data(Connection conn, String tableName) throws SQLException {
        if ("EMPLEADOS".equalsIgnoreCase(tableName)) {
            String sql = "INSERT INTO EMPLEADOS (DNI_NIF, NOMBRE) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                // Registro 1
                preparedStatement.setString(1, "123456789");
                preparedStatement.setString(2, "Kirk Hammett");
                preparedStatement.executeUpdate();

                // Registro 2
                preparedStatement.setString(1, "987654321");
                preparedStatement.setString(2, "Phil Bozeman");
                preparedStatement.executeUpdate();

                // Registro 3
                preparedStatement.setString(1, "456789123");
                preparedStatement.setString(2, "Will Ramos");
                preparedStatement.executeUpdate();

                // Registro 4
                preparedStatement.setString(1, "789123456");
                preparedStatement.setString(2, "Herman Li");
                preparedStatement.executeUpdate();
            }
        } else if ("PROYECTOS".equalsIgnoreCase(tableName)) {
            String sql = "INSERT INTO PROYECTOS (NOMBRE, DNI_NIF_JEFE_PROY, F_INICIO, F_FIN) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                // Registro 1
                preparedStatement.setString(1, "Proyecto 1");
                preparedStatement.setString(2, "123456789");
                preparedStatement.setDate(3, Date.valueOf("2023-01-01"));
                preparedStatement.setDate(4, Date.valueOf("2023-12-31"));
                preparedStatement.executeUpdate();

                // Registro 2
                preparedStatement.setString(1, "Proyecto 2");
                preparedStatement.setString(2, "987654325");
                preparedStatement.setDate(3, Date.valueOf("2023-05-05"));
                preparedStatement.setDate(4, Date.valueOf("2023-12-30"));
                preparedStatement.executeUpdate();

                // Registro 3
                preparedStatement.setString(1, "Proyecto 3");
                preparedStatement.setString(2, "456789122");
                preparedStatement.setDate(3, Date.valueOf("2023-07-07"));
                preparedStatement.setDate(4, Date.valueOf("2023-10-31"));
                preparedStatement.executeUpdate();

                // Registro 4
                preparedStatement.setString(1, "Proyecto 4");
                preparedStatement.setString(2, "789123452");
                preparedStatement.setDate(3, Date.valueOf("2023-01-01"));
                preparedStatement.setDate(4, Date.valueOf("2023-03-30"));
                preparedStatement.executeUpdate();
            }
        } else if ("ASIG_PROYECTOS".equalsIgnoreCase(tableName)) {
            String sql = "INSERT INTO ASIG_PROYECTOS (DNI_NIF_EMP, NUM_PROY, F_INICIO, F_FIN) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                // Registro 1
                preparedStatement.setString(1, "123456789");
                preparedStatement.setInt(2, 1);
                preparedStatement.setDate(3, Date.valueOf("2023-01-01"));
                preparedStatement.setDate(4, Date.valueOf("2023-12-31"));
                preparedStatement.executeUpdate();

                // Registro 2
                preparedStatement.setString(1, "987654321");
                preparedStatement.setInt(2, 2);
                preparedStatement.setDate(3, Date.valueOf("2023-05-05"));
                preparedStatement.setDate(4, Date.valueOf("2023-12-30"));
                preparedStatement.executeUpdate();

                // Registro 3
                preparedStatement.setString(1, "456789123");
                preparedStatement.setInt(2, 3);
                preparedStatement.setDate(3, Date.valueOf("2023-07-07"));
                preparedStatement.setDate(4, Date.valueOf("2023-10-31"));
                preparedStatement.executeUpdate();

                // Registro 4
                preparedStatement.setString(1, "789123456");
                preparedStatement.setInt(2, 4);
                preparedStatement.setDate(3, Date.valueOf("2023-01-01"));
                preparedStatement.setDate(4, Date.valueOf("2023-03-30"));
                preparedStatement.executeUpdate();
            }
        }
    }

    private static void dropTable(Connection conn, String tableName) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            if ("ASIG_PROYECTOS".equalsIgnoreCase(tableName)) {
                stmt.execute("DROP TABLE ASIG_PROYECTOS");
                stmt.execute("DROP TABLE PROYECTOS");
                stmt.execute("DROP TABLE EMPLEADOS");
            } else if ("PROYECTOS".equalsIgnoreCase(tableName)) {
                stmt.execute("DROP TABLE PROYECTOS");
                stmt.execute("DROP TABLE EMPLEADOS");
            } else if ("EMPLEADOS".equalsIgnoreCase(tableName)) {
                stmt.execute("DROP TABLE EMPLEADOS");
            }
        }
    }

    // Mostrar datos de una fila específica.
    private static void show_row(ResultSet rs, int rowNum, int columnCount, ResultSetMetaData metaData) throws SQLException {
        System.out.println("Fila " + rowNum + ":");
        for (int i = 1; i <= columnCount; i++) {
            System.out.println(metaData.getColumnName(i) + ": " + rs.getString(i));
        }
    }

    // Verificar si una cadena es un número entero.
    private static boolean is_int(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Mostrar detalles de un error SQL.
    public static void show_sql_error(SQLException e) {
        System.err.println("SQL ERROR mensaje: " + e.getMessage());
        System.err.println("SQL Estado: " + e.getSQLState());
        System.err.println("SQL código específico: " + e.getErrorCode());
    }
}
