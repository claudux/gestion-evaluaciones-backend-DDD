package com.institucion.evaluaciones.infrastructure.rest.dto;

import com.institucion.evaluaciones.domain.model.Evaluation;
import java.time.LocalDate;

public record EvaluationResponseDto(
        int id,
        String subject,
        String studentEmail,
        int copies,
        LocalDate examDate,
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
