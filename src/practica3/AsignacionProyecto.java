package practica3;

import java.sql.*;

public class AsignacionProyecto {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/practica_ad";
    private static final String USER = "root";
    private static final String PASS = "admin";

    //Atributos de la clase AsignacionProyecto.
    private String DNI_NIF_EMP;
    private int NUM_PROY;
    private Date F_INICIO;
    private Date F_FIN;

    //Constructor vacío (No hace nada).
    public AsignacionProyecto() {}

    //Carga los datos de la asignación desde la base de datos usando DNI_NIF_EMP, NUM_PROY y F_INICIO.
    public AsignacionProyecto(String DNI_NIF_EMP, int NUM_PROY, Date F_INICIO) throws SQLException {
        this.DNI_NIF_EMP = DNI_NIF_EMP;
        this.NUM_PROY = NUM_PROY;
        this.F_INICIO = F_INICIO;
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ASIG_PROYECTOS WHERE DNI_NIF_EMP = ? AND NUM_PROY = ? AND F_INICIO = ?")) {
            stmt.setString(1, DNI_NIF_EMP);
            stmt.setInt(2, NUM_PROY);
            stmt.setDate(3, F_INICIO);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.F_FIN = rs.getDate("F_FIN");
            } else {
                throw new SQLException("Asignación con DNI_NIF_EMP " + DNI_NIF_EMP + ", NUM_PROY " + NUM_PROY + " y F_INICIO " + F_INICIO + " no encontrada.");
            }
        }
    }

    //Getter para el DNI o NIF del empleado asociado a la asignación.
    public String getDNI_NIF_EMP() {
        return DNI_NIF_EMP;
    }

    //Setter para el DNI o NIF del empleado asociado a la asignación.
    public void setDNI_NIF_EMP(String DNI_NIF_EMP) {
        this.DNI_NIF_EMP = DNI_NIF_EMP;
    }

    //Getter para el número del proyecto asociado a la asignación.
    public int getNUM_PROY() {
        return NUM_PROY;
    }

    //Setter para el número del proyecto asociado a la asignación.
    public void setNUM_PROY(int NUM_PROY) {
        this.NUM_PROY = NUM_PROY;
    }

    //Getter para la fecha de inicio de la asignación.
    public Date getF_INICIO() {
        return F_INICIO;
    }

    //Setter para la fecha de inicio de la asignación.
    public void setF_INICIO(Date F_INICIO) {
        this.F_INICIO = F_INICIO;
    }

    //Getter para la fecha de fin de la asignación.
    public Date getF_FIN() {
        return F_FIN;
    }

    //Setter para la fecha de fin de la asignación.
    public void setF_FIN(Date F_FIN) {
        this.F_FIN = F_FIN;
    }

    // Guardar, inserta o actualiza (si ya existe) los datos de la asignación en la base de datos.
    public void save() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO ASIG_PROYECTOS (DNI_NIF_EMP, NUM_PROY, F_INICIO, F_FIN) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE F_FIN = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, DNI_NIF_EMP);
                stmt.setInt(2, NUM_PROY);
                stmt.setDate(3, F_INICIO);
                stmt.setDate(4, F_FIN);
                stmt.setDate(5, F_FIN);
                stmt.executeUpdate();
            }
        }
    }
}
