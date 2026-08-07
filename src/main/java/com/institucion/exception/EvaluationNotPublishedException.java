package com.institucion.exception;

//cuarta excepcion creada, para verificar el estado de la evaluación.
public class EvaluationNotPublishedException extends RuntimeException {
    public EvaluationNotPublishedException(String message) {
        super(message);
    }
}
