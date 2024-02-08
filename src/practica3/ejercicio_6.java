package practica3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class ejercicio_6 {

    //Constantes para la conexión a la base de datos.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";
    private static final String CSV_PATH = "practica_ad_u3\\clientes.csv"; //Ubicación del archivo CSV.

    public static void execute() {
        try {
            createTableIfNotExists(); // Crear tabla en la base de datos si no existe.
            createCSVIfNotExists(); // Crear archivo CSV si no existe.
            int registrosInsertados = insertDataFromCSV();  // Inserta los datos desde el CSV a la base de datos.
            System.out.println("Se han insertado " + registrosInsertados + " registros en la tabla CLIENTES.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Crea la tabla clientes si no existe.
    private static void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS clientes (DNI char(9) NOT NULL, APELLIDOS varchar(32) NOT NULL, CP char(5) DEFAULT NULL, PRIMARY KEY (DNI))";
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    // Crea un archivo CSV con datos predeterminados.
    private static void createCSVIfNotExists() throws IOException {
        File file = new File(CSV_PATH);
        if (!file.exists()) {
            List<String> lines = Arrays.asList(
                    "DNI1234567;García Pérez;28001",
                    "DNI7654321;Martínez López;28002",
                    "DNI9876543;Rodríguez Fernández;28003"
            );
            Files.write(Paths.get(CSV_PATH), lines);
        }
    }

    // // Lee el archivo CSV e inserta los datos en la tabla clientes..
    private static int insertDataFromCSV() throws SQLException, IOException {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO clientes (DNI, APELLIDOS, CP) VALUES (?, ?, ?)")) {
            List<String> lines = Files.readAllLines(Paths.get(CSV_PATH));
            for (String line : lines) {
                String[] parts = line.split(";");
                stmt.setString(1, parts[0]);
                stmt.setString(2, parts[1]);
                stmt.setString(3, parts[2]);
                count += stmt.executeUpdate();
            }
        }
        return count;
    }
}

