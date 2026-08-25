package com.institucion.evaluaciones.infrastructure.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * Estructura estándar RFC 7807 para respuestas de error de la API
 */
@Schema(description = "Respuesta de error unificada y estandarizada")
public record ErrorResponse(
        @Schema(description = "Marca temporal del suceso", example = "2026-08-25T10:45:00")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        @Schema(description = "Código de estado HTTP", example = "400")
        int status,

        @Schema(description = "Descripción breve del error HTTP", example = "Bad Request - Invalid Date")
        String error,

        @Schema(description = "Detalle pedagógico y de negocio del error", example = "La impresión solo puede realizarse exactamente el día anterior a la evaluación.")
        String message,

        @Schema(description = "Ruta URI de la solicitud fallida", example = "/api/v1/evaluations/1/print")
        String path
) {
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path);
    }
}
