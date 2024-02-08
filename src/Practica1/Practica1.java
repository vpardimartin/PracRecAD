package Practica1;

import Practica2.Metodos;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Practica1 {
    public static void execute() {
        int opcion;

        do {
            System.out.println("-- Practica 1 --");
            System.out.println("1. Mostrar PDF");
            System.out.println("0. Volver");
            System.out.print("Ingrese su opción: ");
            opcion = Metodos.obtenerOpcion();

            switch (opcion) {
                case 1:
                    mostrarPDF();
                    break;
                case 0:
                    System.out.println("Volviendo al menú principal...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, ingrese 1 o 0.");
            }
        } while (opcion != 0);
    }

    public static void mostrarPDF() {
        System.out.println("Abriendo PDF...");
        try {
            File file = new File("src/Practica1/pdf/Tipo de Sistema de Persistencia.pdf");
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            System.out.println("Error al abrir el PDF: " + e.getMessage());
        }
    }
}
