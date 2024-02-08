package practica3;

import java.sql.*;

public class Empleado {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";

    //Atributos de la clase Empleado.
    private String DNI_NIF;
    private String nombre;

    //Constructor vacío (No hace nada).
    public Empleado(String number, Connection connection) {}

    // Carga los datos del empleado desde la base de datos usando DNI_NIF.
    public Empleado(String DNI_NIF) {
        this.DNI_NIF = DNI_NIF;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EMPLEADOS WHERE DNI_NIF = ?")) {
            stmt.setString(1, DNI_NIF);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.nombre = rs.getString("NOMBRE");
            }
        } catch (SQLException e) {
        }
    }


    //Getter para el DNI o NIF del empleado.
    public String getDNINIF() {
        return DNI_NIF;
    }

    //Setter para el DNI o NIF del empleado.
    public void setDNINIF(String DNI_NIF) {
        this.DNI_NIF = DNI_NIF;
    }

    //Getter para el nombre del empleado.
    public String getNombre() {
        return nombre;
    }

    //Setter para el nombre del empleado.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Guarda, inserta o actualiza (si ya existe) los datos del empleado en la base de datos.
    public void save() throws SQLException, ClassNotFoundException {
        System.out.println("DNI en save: " + this.DNI_NIF);  // Añade esta línea
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO EMPLEADOS (DNI_NIF, NOMBRE) VALUES (?, ?) ON DUPLICATE KEY UPDATE NOMBRE = ?")) {
            stmt.setString(1, this.DNI_NIF);
            stmt.setString(2, this.nombre);
            stmt.setString(3, this.nombre);
            stmt.executeUpdate();
        }
    }


}
