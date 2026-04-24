package com.saludvital;

import org.apache.camel.builder.RouteBuilder;

public class FileTransferRoute extends RouteBuilder {

    @Override
    public void configure() {
        from("file:data/input?delete=true&include=.*\\.csv")
            .routeId("file-transfer-route")
            .log("Procesando archivo: ${file:name}")
            .process(new CsvValidator())
            .choice()
                .when(header("valid").isEqualTo(true))
                    .log("Archivo válido: ${file:name}")
                    .to("file:data/output")
                    .to("file:data/archive?fileName=${date:now:yyyy-MM-dd_HHmmss}_${file:name}")
                .otherwise()
                    .log("Archivo inválido: ${file:name} - Razón: ${header.validationError}")
                    .to("file:data/error")
            .end()
            .log("Archivo ${file:name} procesado correctamente.");
    }
}
