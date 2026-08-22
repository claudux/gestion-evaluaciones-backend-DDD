package com.institucion.evaluaciones.infrastructure.rest.dto;

import com.institucion.evaluaciones.domain.model.Evaluation;
import java.time.LocalDate;

public record UpdateEvaluationRequestDto(
        String subject,
        String studentEmail,
        Integer copies,
        LocalDate examDate,
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
