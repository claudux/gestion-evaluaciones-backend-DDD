package com.institucion.evaluaciones.infrastructure.rest.dto;

import com.institucion.evaluaciones.domain.model.Evaluation;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Payload para la actualización parcial de una evaluación existente")
public record UpdateEvaluationRequestDto(
        @Schema(description = "Nombre actualizado de la asignatura", example = "Estructuras de Datos Avanzadas")
        String subject,

        @Schema(description = "Correo electrónico institucional del docente", example = "docente.titular@institucion.cl")
        String studentEmail,

        @Schema(description = "Número actualizado de copias (1 a 49)", example = "40", minimum = "1", maximum = "49")
        Integer copies,

        @Schema(description = "Nueva fecha programada para el examen", example = "2026-09-20")
        LocalDate examDate,

        @Schema(description = "Estado del instrumento (Pendiente, Publicada, Completa)", example = "Publicada", allowableValues = {"Pendiente", "Publicada", "Completa"})
        String status
) {
    public void applyTo(Evaluation evaluation) {
        if (subject != null && !subject.isBlank()) {
            evaluation.setSubject(subject);
        }
        if (studentEmail != null && !studentEmail.isBlank()) {
            evaluation.setStudentEmail(studentEmail);
        }
        if (copies != null && copies > 0 && copies < 50) {
            evaluation.setCopies(copies);
        }
        if (examDate != null) {
            evaluation.setEvaluationDate(examDate);
        }
        if (status != null && !status.isBlank()) {
            evaluation.setStatus(status);
        }
    }
}
