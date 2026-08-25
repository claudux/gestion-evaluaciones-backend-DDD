package com.institucion.evaluaciones.infrastructure.rest.dto;

import com.institucion.evaluaciones.domain.model.Evaluation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Representación detallada de una evaluación académica")
public record EvaluationResponseDto(
        @Schema(description = "Identificador único de la evaluación en PostgreSQL", example = "1")
        int id,

        @Schema(description = "Nombre de la asignatura académica", example = "Estructuras de Datos y Algoritmos")
        String subject,

        @Schema(description = "Correo del docente responsable", example = "docente.mate@institucion.cl")
        String studentEmail,

        @Schema(description = "Cantidad de copias solicitadas", example = "35")
        int copies,

        @Schema(description = "Fecha fijada para el examen", example = "2026-09-15")
        LocalDate examDate,

        @Schema(description = "Estado actual en el ciclo de vida", example = "Publicada", allowableValues = {"Pendiente", "Publicada", "Completa"})
        String status
) {
    public static EvaluationResponseDto fromDomain(Evaluation evaluation) {
        return new EvaluationResponseDto(
                evaluation.getId(),
                evaluation.getSubject(),
                evaluation.getStudentEmail(),
                evaluation.getCopies(),
                evaluation.getEvaluationDate(),
                evaluation.getStatus()
        );
    }
}
