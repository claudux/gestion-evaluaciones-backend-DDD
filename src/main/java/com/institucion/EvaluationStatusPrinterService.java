package com.institucion;

//clase que engloba todo lo aprendido, revisa estado de evaluacion y se asegura que numero de copias sea menor que 50
import com.institucion.evaluaciones.application.NotificationService;

/**
 * Adaptador de compatibilidad para com.institucion.EvaluationStatusPrinterService.
 */
public class EvaluationStatusPrinterService extends com.institucion.evaluaciones.application.EvaluationStatusPrinterService {

    public EvaluationStatusPrinterService(NotificationService notificationService) {
        super(notificationService);
    }
}
