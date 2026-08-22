package com.institucion.evaluaciones.infrastructure.rest.dto;

import java.time.LocalDate;

public record CreateEvaluationRequestDto(
        String subject,
        int copies,
        LocalDate examDate
) {
}
