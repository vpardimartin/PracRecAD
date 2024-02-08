package practica3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    //Definición de la base de datos.
    private static final String URL = "jdbc:mysql://localhost:3306/practica_ad";

    // Definición del nombre de usuario de la base de datos.
    private static final String USER = "root";

    // Definición de la contraseña.
    private static final String PASSWORD = "admin";

    // Método que conecta con la base de datos.
    public static Connection obtenerConexion() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
