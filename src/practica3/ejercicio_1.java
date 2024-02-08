package practica3;

import java.sql.*;
import java.util.Scanner;

public class ejercicio_1 {

    //Constantes para la conexión a la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";

    public static void execute() {
        // Conexión con la base de datos.
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            create_database(conn); // Crea la base de datos.
            predefined_data(conn); // Inserta datos predefinidos.

            // Mostrar datos de la tabla "clientes".
            try (Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                 ResultSet rs = stmt.executeQuery("SELECT * FROM clientes")) {

                int currentRow = 0;
                Scanner scanner = new Scanner(System.in);
                String comando;

                // Navegación entre los registros.
// Navegación entre los registros.
                while (true) {
                    if (currentRow >= 0 && rs.absolute(currentRow + 1)) {
                        show_row(rs, currentRow + 1); // Mostrar fila actual.
                    } else if (currentRow < 0) {
                        System.out.println("\nEstás en la primera fila.\n");
                        currentRow = 0;
                        continue;
                    } else {
                        System.out.println("\nEstás en la última fila.\n");
                        currentRow = currentRow-1;
                        continue;
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
        } catch (SQLException e) {
            show_sql_error(e);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    // Crea la tabla "clientes".
    private static void create_database(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS clientes");
            stmt.execute("CREATE TABLE clientes ("
                    + "`DNI` char(9) NOT NULL,"
                    + "`APELLIDOS` varchar(32) NOT NULL,"
                    + "`CP` char(5) DEFAULT NULL,"
                    + "PRIMARY KEY (`DNI`))");
        }
    }

    // Inserta datos predefinidos en la tabla "clientes".
    private static void predefined_data(Connection conn) throws SQLException {
        String sql = "INSERT INTO clientes (DNI, APELLIDOS, CP) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            // Insertar registros.
            preparedStatement.setString(1, "987654321");
            preparedStatement.setString(2, "James Hetflield");
            preparedStatement.setString(3, "28001");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "876543210");
            preparedStatement.setString(2, "Kiko Loureiro");
            preparedStatement.setString(3, "28002");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "456789123");
            preparedStatement.setString(2, "Dickie Allen");
            preparedStatement.setString(3, "28003");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "789123456");
            preparedStatement.setString(2, "Dimebag Darrell");
            preparedStatement.setString(3, "28004");
            preparedStatement.executeUpdate();
        }
    }

    //Comprueba que es un número entero.
    private static boolean is_int(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Muestra los datos de una fila.
    private static void show_row(ResultSet rs, int rowNum) throws SQLException {
        System.out.println("Fila " + rowNum + ":");
        System.out.println("DNI: " + rs.getString("DNI"));
        System.out.println("APELLIDOS: " + rs.getString("APELLIDOS"));
        System.out.println("CP: " + rs.getString("CP"));
    }

    // Muestra los detalles de errores SQL.
    public static void show_sql_error(SQLException e) {
        System.err.println("SQL ERROR mensaje: " + e.getMessage());
        System.err.println("SQL Estado: " + e.getSQLState());
        System.err.println("SQL código específico: " + e.getErrorCode());
    }
}
