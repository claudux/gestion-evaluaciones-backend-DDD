package com.institucion.evaluaciones.application;

import com.institucion.evaluaciones.domain.exception.EvaluationNotFoundException;
import com.institucion.evaluaciones.domain.exception.EvaluationNotPublishedException;
import com.institucion.evaluaciones.domain.exception.InvalidCopyQuantityException;
import com.institucion.evaluaciones.domain.exception.InvalidEvaluationDateException;
import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.domain.repository.EvaluationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * ============================================================================
 * CAPA DE APLICACIÓN: SERVICIO DE CASOS DE USO DE EVALUACIONES
 * ============================================================================
 * Orquesta la lógica del negocio, control de estados y persistencia transaccional.
 */
@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final NotificationService notificationService;

    public EvaluationService(EvaluationRepository evaluationRepository,
                             NotificationService notificationService) {
        this.evaluationRepository = evaluationRepository;
        this.notificationService = notificationService;
    }

    @Transactional(readOnly = true)
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Evaluation getEvaluationById(int id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new EvaluationNotFoundException(id));
    }

    @Transactional
    public Evaluation createEvaluation(String subject, int copies, LocalDate examDate) {
        if (examDate == null) {
            throw new InvalidEvaluationDateException("La fecha de evaluación es obligatoria.");
        }
        if (examDate.isBefore(LocalDate.now())) {
            throw new InvalidEvaluationDateException("No se puede registrar una evaluación con fecha en el pasado: " + examDate);
        }

        // ID 0 para que la base de datos relacional genere la secuencia autoincremental
        Evaluation evaluation = new Evaluation(0, subject, "docente@institucion.cl", copies, examDate, "Pendiente");
        Evaluation saved = evaluationRepository.save(evaluation);

        if (notificationService != null) {
            notificationService.notify("Se ha registrado la evaluación #" + saved.getId() + " (" + saved.getSubject() + ")");
        }
        return saved;
    }

    @Transactional
    public Evaluation updateEvaluation(int id, Evaluation updatedData) {
        Evaluation existing = getEvaluationById(id);

        if (updatedData.getSubject() != null && !updatedData.getSubject().isBlank()) {
            existing.setSubject(updatedData.getSubject());
        }
        if (updatedData.getStudentEmail() != null && !updatedData.getStudentEmail().isBlank()) {
            existing.setStudentEmail(updatedData.getStudentEmail());
        }
        if (updatedData.getCopies() > 0 && updatedData.getCopies() < 50) {
            existing.setCopies(updatedData.getCopies());
        }
        if (updatedData.getEvaluationDate() != null) {
            existing.setEvaluationDate(updatedData.getEvaluationDate());
        }
        if (updatedData.getStatus() != null && !updatedData.getStatus().isBlank()) {
            existing.setStatus(updatedData.getStatus());
        }

        return evaluationRepository.save(existing);
    }

    @Transactional
    public Evaluation publishEvaluation(int id) {
        Evaluation evaluation = getEvaluationById(id);
        evaluation.publish();
        Evaluation updated = evaluationRepository.save(evaluation);

        if (notificationService != null) {
            notificationService.notify("Evaluación #" + id + " publicada para impresión.");
        }
        return updated;
    }

    /**
     * Reglas de Negocio de Impresión:
     * 1. La evaluación debe estar en estado 'Publicada'.
     * 2. La cantidad de copias debe ser > 0 y < 50.
     * 3. La impresión solo puede realizarse antes o el día anterior a la evaluación (no en el pasado ni el mismo día).
     */
    @Transactional
    public Evaluation printEvaluation(int id) {
        Evaluation evaluation = getEvaluationById(id);

        // Regla 1: Estado Publicada
        if (!"Publicada".equalsIgnoreCase(evaluation.getStatus())) {
            throw new EvaluationNotPublishedException("Cannot print an evaluation that is not published.");
        }

        // Regla 2: Copias > 0 y < 50
        if (evaluation.getCopies() <= 0 || evaluation.getCopies() >= 50) {
            throw new InvalidCopyQuantityException("The number of copies must be greater than 0 and less than 50.");
        }

        // Regla 3: La impresión solo puede realizarse estrictamente el día anterior a la evaluación
        LocalDate authorizedPrintDate = evaluation.getEvaluationDate().minusDays(1);
        LocalDate today = LocalDate.now();

        if (!today.equals(authorizedPrintDate)) {
            if (today.isBefore(authorizedPrintDate)) {
                throw new InvalidEvaluationDateException(
                        "Aún no corresponde imprimir. La impresión solo está autorizada exactamente el día anterior a la evaluación (Fecha autorizada: " + authorizedPrintDate + ")."
                );
            } else {
                throw new InvalidEvaluationDateException(
                        "La fecha de impresión autorizada (" + authorizedPrintDate + ") ya expiró. La fecha del examen (" + evaluation.getEvaluationDate() + ") es hoy o ya pasó."
                );
            }
        }

        // Si cumple todas las reglas, pasa a estado Completa
        evaluation.markAsPrinted();
        Evaluation updated = evaluationRepository.save(evaluation);

        if (notificationService != null) {
            notificationService.notify("Evaluación #" + id + " impresa con éxito (" + evaluation.getCopies() + " copias).");
        }
        return updated;
    }

    @Transactional
    public void deleteEvaluation(int id) {
        if (!evaluationRepository.existsById(id)) {
            throw new EvaluationNotFoundException(id);
        }
        evaluationRepository.deleteById(id);
    }
}
