package com.institucion.evaluaciones.domain.exception;

/**
 * Excepción de Dominio arrojada al intentar instanciar un Value Object EvaluationScore fuera del rango permitido.
 */
public class InvalidEvaluationScoreException extends RuntimeException {
    public InvalidEvaluationScoreException(String message) {
        super(message);
    }
}
