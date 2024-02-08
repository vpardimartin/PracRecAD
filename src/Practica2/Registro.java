package Practica2;

public class Registro {
    private String[] campos;

    public Registro(String[] campos) {
        this.campos = campos;
    }

    // Métodos para acceder y modificar los campos del registro
    public String getCampo(int indice) {
        if (indice >= 0 && indice < campos.length) {
            return campos[indice];
        } else {
            throw new IllegalArgumentException("Índice de campo fuera de rango.");
        }
    }

    public void setCampo(int indice, String valor) {
        if (indice >= 0 && indice < campos.length) {
            campos[indice] = valor;
        } else {
            throw new IllegalArgumentException("Índice de campo fuera de rango.");
        }
    }

    // Método para obtener el número de campos del registro
    public int getNumeroCampos() {
        return campos.length;
    }

    // Método para mostrar el registro como una cadena de texto
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < campos.length; i++) {
            sb.append(campos[i]);
            if (i < campos.length - 1) {
                sb.append("#"); // Separador entre campos
            }
        }
        return sb.toString();
    }
}
