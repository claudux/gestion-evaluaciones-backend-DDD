package com.institucion.evaluaciones.application;

//clase que engloba todo lo aprendido, revisa estado de evaluacion y se asegura que numero de copias sea menor que 50
import com.institucion.evaluaciones.domain.exception.EvaluationNotPublishedException;
import com.institucion.evaluaciones.domain.exception.InvalidCopyQuantityException;
import com.institucion.evaluaciones.domain.exception.InvalidEvaluationDateException;
import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.domain.repository.EvaluationRepository;

import java.time.LocalDate;

/**
 * ============================================================================
 * CLEAN ARCHITECTURE: CASO DE USO / SERVICIO DE APLICACIÓN
 * ============================================================================
 * Orquesta la lógica del negocio (reglas de impresión) e interactúa con el dominio
 * a través de interfaces (NotificationService, EvaluationRepository).
 */
public class EvaluationStatusPrinterService {
    private final NotificationService notificationService;
    private final EvaluationRepository evaluationRepository;

    public EvaluationStatusPrinterService(NotificationService notificationService) {
        this(notificationService, null);
    }

    public EvaluationStatusPrinterService(NotificationService notificationService, EvaluationRepository evaluationRepository) {
        this.notificationService = notificationService;
        this.evaluationRepository = evaluationRepository;
    }

    //metodo que engloba las reglas de negocio
    public void requestPrintJob(Evaluation evaluation, int copies) {
        //regla 1, evaluacion debe estar en Publicada
        if (!"Publicada".equals(evaluation.getStatus())) {
            throw new EvaluationNotPublishedException("Cannot print an evaluation that is not published.");
        }

        //regla 2, la cantidad de copias >0 y <=50
        if (copies <= 0 || copies >= 50) {
            throw new InvalidCopyQuantityException("The number of copies must be greater than 0 and less than 50.");
        }

        //regla 3, solo se puede imprimir la evaluacion el dia anterior a la fecha establecida
        if (!LocalDate.now().isBefore(evaluation.getEvaluationDate())) {
            throw new InvalidEvaluationDateException("La impresión solo puede realizarse el dia anterior a la evaluación");
        }

        // si pasa las reglas, camino correcto
        String message = "Your printing job for evaluation " + evaluation.getId() + " with " + copies + " copies has been approved.";
        notificationService.sendAlert(evaluation.getStudentEmail(), message);

        // Opcional: Persistir en repositorio si está inyectado
        if (evaluationRepository != null) {
            evaluationRepository.save(evaluation);
        }
    }
}
