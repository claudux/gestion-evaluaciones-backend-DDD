package com.institucion.evaluaciones.domain.model.valueobjects;

import com.institucion.evaluaciones.domain.exception.InvalidEvaluationScoreException;

/**
 * ============================================================================
 * PATRÓN TÁCTICO DDD: VALUE OBJECT (Objeto de Valor)
 * ============================================================================
 * Representa la calificación/nota de una evaluación.
 * 
 * Características clave:
 * 1. Inmutabilidad: Implementado como Java record (Java 17+).
 * 2. Auto-validación defensiva: Valida en el constructor que la nota esté
 *    dentro del rango académico chileno válido (1.0 a 7.0).
 * 3. Sin Identidad propia: Dos objetos EvaluationScore con el mismo valor son idénticos.
 */
public record EvaluationScore(double value) {

    public EvaluationScore {
        if (value < 1.0 || value > 7.0) {
            throw new InvalidEvaluationScoreException("La calificación debe estar entre 1.0 y 7.0. Valor recibido: " + value);
        }
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "%.1f", value);
    }
}
