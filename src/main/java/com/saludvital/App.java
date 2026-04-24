package com.saludvital;

import org.apache.camel.main.Main;

public class App {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new FileTransferRoute());

        // Detener después de 10 segundos
        main.configure().setDurationMaxSeconds(10);

        System.out.println("=== Salud Vital - Integración de Archivos ===");
        System.out.println("Monitoreando carpeta data/input/ por archivos CSV...");
        System.out.println("La aplicación se detendrá en 10 segundos.");

        main.run(args);

        System.out.println("=== Procesamiento finalizado ===");
    }
}
