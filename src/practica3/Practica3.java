package practica3;

import java.util.Scanner;

public class Practica3 {
    public static void execute() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("----- Menú Principal -----");
            System.out.println("1. Navegación interactiva por la tabla CLIENTES");
            System.out.println("2. Mejora del programa anterior");
            System.out.println("3. Gestión de proyectos y empleados");
            System.out.println("4. Creación de clases Empleado, Proyecto y AsignaciónEmpAProyecto");
            System.out.println("5. Obtener lista de empleados asignados a un proyecto");
            System.out.println("6. Insertar datos de clientes desde un fichero CSV");
            System.out.println("7. Lista de condiciones de prueba");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    ejercicio_1.execute();
                    break;
                case 2:
                    ejercicio_2.execute();
                    break;
                case 3:
                    ejercicio_3.execute();
                    break;
                case 4:
                    ejercicio_4.execute();
                    break;
                case 5:
                    ejercicio_5.execute();
                    break;
                case 6:
                    ejercicio_6.execute();
                    break;
                case 7:
                    ejercicio_7.execute();
                    break;


                case 0:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }
}
