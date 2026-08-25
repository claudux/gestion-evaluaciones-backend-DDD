package com.institucion.evaluaciones.infrastructure.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Payload para la creación de un nuevo instrumento de evaluación académica")
public record CreateEvaluationRequestDto(
        @Schema(description = "Nombre de la asignatura académica", example = "Estructuras de Datos y Algoritmos", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "La asignatura es obligatoria")
        String subject,

        @Schema(description = "Número de copias solicitadas (rango permitido: 1 a 49)", example = "35", minimum = "1", maximum = "49", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 1, message = "El mínimo de copias es 1")
        @Max(value = 49, message = "El máximo de copias es 49")
        int copies,

        @Schema(description = "Fecha programada del examen (debe ser futura para permitir impresión)", example = "2026-09-15", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "La fecha del examen es obligatoria")
        LocalDate examDate
) {
}
