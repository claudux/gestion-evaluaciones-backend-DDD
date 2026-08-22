package com.institucion.evaluaciones.infrastructure.rest.advice;

import com.institucion.evaluaciones.domain.exception.*;
import com.institucion.evaluaciones.infrastructure.rest.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ============================================================================
 * INTERCEPTOR GLOBAL DE EXCEPCIONES Y ERRORES DE NEGOCIO (@RestControllerAdvice)
 * Escudo perimetral que transforma fallos del backend en respuestas HTTP semánticas
 * ============================================================================
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Regla 1: Intentar imprimir una evaluación que no está 'Publicada'
     * -> HTTP 422 UNPROCESSABLE ENTITY
     */
    @ExceptionHandler(EvaluationNotPublishedException.class)
    public ResponseEntity<ErrorResponse> handleNotPublished(EvaluationNotPublishedException ex, HttpServletRequest request) {
        log.warn("⚠️ Regla de negocio violada (No Publicada): {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Unprocessable Entity - Evaluation Not Published",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    /**
     * Regla 2: Cantidad de copias inválida (<= 0 o >= 50)
     * -> HTTP 400 BAD REQUEST
     */
    @ExceptionHandler(InvalidCopyQuantityException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCopies(InvalidCopyQuantityException ex, HttpServletRequest request) {
        log.warn("⚠️ Cantidad de copias inválida: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request - Invalid Copy Quantity",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Regla 3: Fecha de evaluación inválida o no previa al examen
     * -> HTTP 400 BAD REQUEST
     */
    @ExceptionHandler(InvalidEvaluationDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDate(InvalidEvaluationDateException ex, HttpServletRequest request) {
        log.warn("⚠️ Fecha inválida: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request - Invalid Date",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Evaluación no encontrada por ID
     * -> HTTP 404 NOT FOUND
     */
    @ExceptionHandler(EvaluationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EvaluationNotFoundException ex, HttpServletRequest request) {
        log.warn("🔍 Evaluación no encontrada: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Argumentos ilegales generales
     * -> HTTP 400 BAD REQUEST
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("⚠️ Argumento ilegal: {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Captura de errores inesperados del servidor
     * -> HTTP 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("💥 Error interno no controlado:", ex);
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocurrió un error inesperado en el servidor.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
