package com.institucion.evaluaciones.domain.model;

import com.institucion.evaluaciones.domain.exception.InvalidCopyQuantityException;
import com.institucion.evaluaciones.domain.exception.InvalidEvaluationDateException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * ============================================================================
 * PATRÓN TÁCTICO DDD: ENTIDAD (Entity) / RAÍZ DE AGREGADO (Aggregate Root)
 * ============================================================================
 * Representa una Evaluación Académica con identidad única (id).
 */
public class Evaluation {
    private final int id;
    private String subject;
    private String studentEmail;
    private int copies;
    private LocalDate evaluationDate;
    private String status; // "Pendiente", "Publicada", "Completa"

    public Evaluation(int id, String subject, String studentEmail, int copies, LocalDate evaluationDate, String status) {
        if (id < 0) {
            throw new IllegalArgumentException("El ID no puede ser negativo");
        }
        this.id = id;
        this.subject = Objects.requireNonNull(subject, "La asignatura no puede ser nula");
        this.studentEmail = studentEmail != null ? studentEmail : "docente@institucion.cl";
        
        if (copies <= 0 || copies >= 50) {
            throw new InvalidCopyQuantityException("The number of copies must be greater than 0 and less than 50.");
        }
        this.copies = copies;

        if (evaluationDate == null) {
            throw new InvalidEvaluationDateException("La fecha de evaluación no puede ser nula");
        }
        this.evaluationDate = evaluationDate;
        this.status = status != null ? status : "Pendiente";
    }

    public Evaluation(int id, String subject, int copies, LocalDate evaluationDate) {
        this(id, subject, "docente@institucion.cl", copies, evaluationDate, "Pendiente");
    }

    public Evaluation(int id, String studentEmail, LocalDate evaluationDate) {
        this(id, "Evaluación Académica", studentEmail, 30, evaluationDate, "Pendiente");
    }

    public void publish() {
        this.status = "Publicada";
    }

    public void markAsPrinted() {
        this.status = "Completa";
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        if (copies <= 0 || copies >= 50) {
            throw new InvalidCopyQuantityException("The number of copies must be greater than 0 and less than 50.");
        }
        this.copies = copies;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        if (evaluationDate == null) {
            throw new InvalidEvaluationDateException("La fecha de evaluación no puede ser nula.");
        }
        this.evaluationDate = evaluationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
