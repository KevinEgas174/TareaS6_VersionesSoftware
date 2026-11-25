import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VersionSoftware[] versiones = new VersionSoftware[5];

        System.out.println("=== REGISTRO DE 5 VERSIONES DE SOFTWARE ===");
        for (int i = 0; i < versiones.length; i++) {
            System.out.println("\n--- Version " + (i + 1) + " ---");
            String numero = leerVersionUnica(sc, versiones, i, "Ingrese numero de version (ej. 1.0.0): ");
            String fecha = leerFechaValida(sc, "Ingrese fecha (AAAA-MM-DD, ej. 2026-11-25): ");
            boolean estable = leerBooleano(sc, "Es version estable? (si/no): ");
            int cambios = leerEnteroNoNegativo(sc, "Ingrese cantidad de cambios: ");
            versiones[i] = new VersionSoftware(numero, fecha, estable, cambios);
        }

        AnalizadorVersiones analizador = new AnalizadorVersiones(versiones);

        System.out.println("\n=== RESULTADOS ===");
        System.out.println("Versiones estables: " + analizador.contarEstables());
        System.out.println("Suma total de cambios: " + analizador.sumarCambios());
        System.out.printf("Promedio de cambios por version: %.2f\n", analizador.calcularPromedioCambios());

        VersionSoftware masCambios = analizador.obtenerVersionMasCambios();
        if (masCambios != null) {
            System.out.println("Version con mas cambios: " + masCambios);
        }

        System.out.println("\n--- Busqueda 1 ---");
        String vBuscar1 = leerFormatoVersion(sc, "Ingrese numero de version a buscar (ej. 1.0.0): ");
        VersionSoftware res1 = analizador.buscarVersion(vBuscar1);
        System.out.println(res1 != null ? "Encontrada: " + res1 : "Version no encontrada.");

        System.out.println("\n--- Busqueda 2 ---");
        String vBuscar2 = leerFormatoVersion(sc, "Ingrese otro numero de version a buscar (ej. 2.0.0): ");
        VersionSoftware res2 = analizador.buscarVersion(vBuscar2);
        System.out.println(res2 != null ? "Encontrada: " + res2 : "Version no encontrada.");

        sc.close();
    }

    private static String leerFormatoVersion(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String version = sc.nextLine().trim();
            if (version.matches("\\d+\\.\\d+\\.\\d+")) {
                return version;
            }
            System.out.println("Error: Formato invalido. Use el formato X.Y.Z con numeros (ej. 1.0.0).");
        }
    }

    private static String leerVersionUnica(Scanner sc, VersionSoftware[] existentes, int limite, String mensaje) {
        while (true) {
            String version = leerFormatoVersion(sc, mensaje);
            boolean repetida = false;
            for (int i = 0; i < limite; i++) {
                if (existentes[i] != null && existentes[i].getNumero().equals(version)) {
                    repetida = true;
                    break;
                }
            }
            if (!repetida) {
                return version;
            }
            System.out.println("Error: Esa version ya fue registrada. Ingrese una distinta.");
        }
    }

    private static String leerFechaValida(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String fecha = sc.nextLine().trim();
            try {
                LocalDate.parse(fecha);
                return fecha;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Fecha invalida o formato incorrecto. Use AAAA-MM-DD (ej. 2026-11-25).");
            }
        }
    }

    private static int leerEnteroNoNegativo(Scanner sc, String mensaje) {
        int numero = -1;
        while (numero < 0) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                numero = Integer.parseInt(linea);
                if (numero < 0) {
                    System.out.println("Error: Debe ingresar un numero entero mayor o igual a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Entrada invalida. Ingrese un numero entero.");
            }
        }
        return numero;
    }

    private static boolean leerBooleano(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String resp = sc.nextLine().trim().toLowerCase();
            if (resp.equals("si")) {
                return true;
            }
            if (resp.equals("no")) {
                return false;
            }
            System.out.println("Error: Responda unicamente 'si' o 'no'.");
        }
    }
}