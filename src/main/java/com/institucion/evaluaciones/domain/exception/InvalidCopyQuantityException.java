package com.institucion.evaluaciones.domain.exception;

/**
 * Excepción de Dominio (Clean Architecture): Arrojada cuando la cantidad de copias solicitada rompe la regla de negocio (>0 y <50).
 */
public class InvalidCopyQuantityException extends com.institucion.exception.InvalidCopyQuantityException {
    public InvalidCopyQuantityException(String message) {
        super(message);
    }
}
