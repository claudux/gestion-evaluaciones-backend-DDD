package com.institucion.evaluaciones.infrastructure.persistence.entity;

import com.institucion.evaluaciones.domain.model.Evaluation;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * ============================================================================
 * ENTIDAD JPA: MAPEO ORM DE EVALUACIONES
 * Mapeo relacional contra la tabla 'tbl_evaluations' en PostgreSQL
 * ============================================================================
 */
@Entity
@Table(name = "tbl_evaluations")
public class EvaluationJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String subject;

    @Column(name = "student_email", nullable = false, length = 150)
    private String studentEmail;

    @Column(nullable = false)
    private int copies;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDate evaluationDate;

    @Column(nullable = false, length = 50)
    private String status;

    public EvaluationJpaEntity() {
    }

    public EvaluationJpaEntity(Integer id, String subject, String studentEmail, int copies, LocalDate evaluationDate, String status) {
        this.id = id;
        this.subject = subject;
        this.studentEmail = studentEmail;
        this.copies = copies;
        this.evaluationDate = evaluationDate;
        this.status = status;
    }

    /**
     * Mapeo hacia el modelo de dominio puro
     */
    public Evaluation toDomain() {
        return new Evaluation(
                this.id != null ? this.id : 1,
                this.subject,
                this.studentEmail,
                this.copies,
                this.evaluationDate,
                this.status
        );
    }

    /**
     * Mapeo desde el modelo de dominio puro hacia la entidad JPA
     */
    public static EvaluationJpaEntity fromDomain(Evaluation domain) {
        Integer entityId = domain.getId() > 0 ? domain.getId() : null;
        return new EvaluationJpaEntity(
                entityId,
                domain.getSubject(),
                domain.getStudentEmail(),
                domain.getCopies(),
                domain.getEvaluationDate(),
                domain.getStatus()
        );
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.copies = copies;
    }

    public LocalDate getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(LocalDate evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
