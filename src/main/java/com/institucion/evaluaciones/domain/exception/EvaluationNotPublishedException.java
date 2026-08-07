package com.institucion.evaluaciones.domain.exception;

/**
 * Excepción de Dominio (Clean Architecture): Arrojada cuando se intenta realizar una operación no permitida sobre una evaluación no publicada.
 */
public class EvaluationNotPublishedException extends com.institucion.exception.EvaluationNotPublishedException {
    public EvaluationNotPublishedException(String message) {
        super(message);
    }
}
