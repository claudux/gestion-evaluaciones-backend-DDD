package com.institucion.evaluaciones.domain.exception;

public class EvaluationNotFoundException extends RuntimeException {
    public EvaluationNotFoundException(int id) {
        super("No se encontró la evaluación con ID: " + id);
    }

    public EvaluationNotFoundException(String message) {
        super(message);
    }
}
