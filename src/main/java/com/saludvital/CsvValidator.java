package com.saludvital;

import java.util.List;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvValidator implements Processor {

    private static final Logger LOG = LoggerFactory.getLogger(CsvValidator.class);

    private static final String EXPECTED_HEADER = "patient_id,full_name,appointment_date,insurance_code";
    private static final Set<String> VALID_INSURANCE_CODES = Set.of("IESS", "PRIVADO", "NINGUNO");
    private static final String DATE_REGEX = "\\d{4}-\\d{2}-\\d{2}";

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody(String.class);
        String fileName = exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);

        if (body == null || body.isBlank()) {
            exchange.getIn().setHeader("valid", false);
            exchange.getIn().setHeader("validationError", "Archivo vacío");
            LOG.warn("[{}] Archivo vacío", fileName);
            return;
        }

        String[] lines = body.split("\\r?\\n");
        List<String> errors = new java.util.ArrayList<>();

        // Validar header
        if (lines.length == 0 || !lines[0].trim().equals(EXPECTED_HEADER)) {
            String actual = lines.length > 0 ? lines[0].trim() : "(vacío)";
            String error = "Header inválido. Esperado: [" + EXPECTED_HEADER + "], encontrado: [" + actual + "]";
            errors.add("Línea 1: " + error);
            LOG.warn("[{}] Línea 1: {}", fileName, error);
        }

        // Validar filas de datos (desde línea 2)
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue; // Ignorar líneas vacías
            }

            int lineNum = i + 1;
            String[] columns = line.split(",", -1);

            // Exactamente 4 columnas
            if (columns.length != 4) {
                String error = "Se esperaban 4 columnas, se encontraron " + columns.length;
                errors.add("Línea " + lineNum + ": " + error);
                LOG.warn("[{}] Línea {}: {}", fileName, lineNum, error);
                continue;
            }

            // Campos no vacíos
            for (int j = 0; j < columns.length; j++) {
                if (columns[j].trim().isEmpty()) {
                    String error = "Campo vacío en columna " + (j + 1);
                    errors.add("Línea " + lineNum + ": " + error);
                    LOG.warn("[{}] Línea {}: {}", fileName, lineNum, error);
                }
            }

            // appointment_date formato YYYY-MM-DD
            String date = columns[2].trim();
            if (!date.isEmpty() && !date.matches(DATE_REGEX)) {
                String error = "Fecha inválida: [" + date + "]. Formato esperado: YYYY-MM-DD";
                errors.add("Línea " + lineNum + ": " + error);
                LOG.warn("[{}] Línea {}: {}", fileName, lineNum, error);
            }

            // insurance_code válido
            String insurance = columns[3].trim();
            if (!insurance.isEmpty() && !VALID_INSURANCE_CODES.contains(insurance)) {
                String error = "Código de seguro inválido: [" + insurance + "]. Válidos: IESS, PRIVADO, NINGUNO";
                errors.add("Línea " + lineNum + ": " + error);
                LOG.warn("[{}] Línea {}: {}", fileName, lineNum, error);
            }
        }

        boolean valid = errors.isEmpty();
        exchange.getIn().setHeader("valid", valid);

        if (!valid) {
            String errorSummary = String.join(" | ", errors);
            exchange.getIn().setHeader("validationError", errorSummary);
            LOG.info("[{}] Validación FALLIDA - {} errores encontrados", fileName, errors.size());
        } else {
            LOG.info("[{}] Validación EXITOSA", fileName);
        }
    }
}
