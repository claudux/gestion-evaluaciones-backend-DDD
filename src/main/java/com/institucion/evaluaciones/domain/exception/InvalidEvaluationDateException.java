package com.institucion.evaluaciones.domain.exception;

/**
 * Excepción de Dominio (Clean Architecture): Arrojada cuando la fecha de solicitud no cumple la regla de negocio.
 */
public class InvalidEvaluationDateException extends com.institucion.exception.InvalidEvaluationDateException {
    public InvalidEvaluationDateException(String message) {
        super(message);
    }
}
