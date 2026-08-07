package com.institucion.evaluaciones.domain.model;

import com.institucion.evaluaciones.domain.model.valueobjects.EvaluationScore;
import java.time.LocalDate;

//clase que permite crear objetos Evaluation, para encapsular datos y evitar hardcoreo.
/**
 * ============================================================================
 * PATRÓN TÁCTICO DDD: ENTIDAD (Entity) / RAÍZ DE AGREGADO (Aggregate Root)
 * ============================================================================
 * Representa una Evaluación con identidad única (id).
 * 
 * Reglas de Arquitectura Limpia & DDD:
 * - Identidad propia a través de 'id'.
 * - Inmutable en campos fundamentales salvo el cambio de estado (status).
 * - Integra Objetos de Valor (EvaluationScore) para garantizar validez de notas.
 */
public class Evaluation {
    private final int id;
    private final String studentEmail;
    private final LocalDate evaluationDate;
    private String status;
    private EvaluationScore score;

    public Evaluation(int id, String studentEmail, LocalDate evaluationDate) {
        this.id = id;
        this.studentEmail = studentEmail;
        this.evaluationDate = evaluationDate;
        this.status = "Pendiente";
        this.score = null;
    }

    public Evaluation(int id, String studentEmail, LocalDate evaluationDate, EvaluationScore score) {
        this(id, studentEmail, evaluationDate);
        this.score = score;
    }

    public void publish() {
        this.status = "Publicada";
    }

    public int getId() {
        return id;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public EvaluationScore getScore() {
        return score;
    }

    public void setScore(EvaluationScore score) {
        this.score = score;
    }
}
