package practica3;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Proyecto {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";

    //Atributos de la clase Proyecto.
    private Integer NUM_PROY;
    private String nombre;
    private String DNI_NIF_JEFE_PROY;
    private Date F_INICIO;
    private Date F_FIN;

    //Constructor vacío (No hace nada).
    public Proyecto(int i, Connection connection) {}

    // Carga los datos del proyecto desde la base de datos usando NUM_PROY.
    public Proyecto(int numProy) throws SQLException {
        this.NUM_PROY = NUM_PROY;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM PROYECTOS WHERE NUM_PROY = ?")) {
            stmt.setInt(1, NUM_PROY);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.nombre = rs.getString("NOMBRE");
                this.DNI_NIF_JEFE_PROY = rs.getString("DNI_NIF_JEFE_PROY");
                this.F_INICIO = rs.getDate("F_INICIO");
                this.F_FIN = rs.getDate("F_FIN");
            } else {
                throw new SQLException("Proyecto con NUM_PROY " + NUM_PROY + " no encontrado.");
            }
        }
    }


    //Getters y Setters para los atributos de la clase.

    public Integer getNUM_PROY() {
        return NUM_PROY;
    }

    public void setNUM_PROY(Integer NUM_PROY) {
        this.NUM_PROY = NUM_PROY;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDNI_NIF_JEFE_PROY() {
        return DNI_NIF_JEFE_PROY;
    }

    public void setDNI_NIF_JEFE_PROY(String DNI_NIF_JEFE_PROY) {
        this.DNI_NIF_JEFE_PROY = DNI_NIF_JEFE_PROY;
    }

    public Date getF_INICIO() {
        return F_INICIO;
    }

    public void setF_INICIO(Date F_INICIO) {
        this.F_INICIO = F_INICIO;
    }

    public Date getF_FIN() {
        return F_FIN;
    }

    public void setF_FIN(Date F_FIN) {
        this.F_FIN = F_FIN;
    }

    // Guarda, inserta o actualiza los datos del proyecto en la base de datos.
    public void save() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if (NUM_PROY == null) {
                // INSERT
                String sql = "INSERT INTO PROYECTOS (NOMBRE, DNI_NIF_JEFE_PROY, F_INICIO, F_FIN) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, nombre);
                    stmt.setString(2, DNI_NIF_JEFE_PROY);
                    stmt.setDate(3, F_INICIO);
                    stmt.setDate(4, F_FIN);
                    stmt.executeUpdate();
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        NUM_PROY = generatedKeys.getInt(1);
                    }
                }
            } else {
                String sql = "UPDATE PROYECTOS SET NOMBRE = ?, DNI_NIF_JEFE_PROY = ?, F_INICIO = ?, F_FIN = ? WHERE NUM_PROY = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, nombre);
                    stmt.setString(2, DNI_NIF_JEFE_PROY);
                    stmt.setDate(3, F_INICIO);
                    stmt.setDate(4, F_FIN);
                    stmt.setInt(5, NUM_PROY);
                    stmt.executeUpdate();
                }
            }
        }
    }

    // Obtiene la lista de empleados asignados a los proyectos que están activos.
    public List<Empleado> getListAsigEmpleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT e.* FROM EMPLEADOS e JOIN ASIG_PROYECTOS a ON e.DNI_NIF = a.DNI_NIF WHERE a.NUM_PROY = ? AND (a.F_INICIO <= NOW() AND (a.F_FIN IS NULL OR a.F_FIN >= NOW()))";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.NUM_PROY);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Empleado empleado = new Empleado(rs.getString("DNI_NIF"));
                empleado.setNombre(rs.getString("NOMBRE"));
                empleados.add(empleado);
            }
        }
        return empleados;
    }
}
