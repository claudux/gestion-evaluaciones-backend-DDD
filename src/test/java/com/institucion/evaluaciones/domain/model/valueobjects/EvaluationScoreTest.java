package com.institucion.evaluaciones.domain.model.valueobjects;

import com.institucion.evaluaciones.domain.exception.InvalidEvaluationScoreException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el Value Object EvaluationScore (Java record inmutable y auto-validante).
 */
class EvaluationScoreTest {

    @Test
    @DisplayName("Debe instanciar correctamente un EvaluationScore con una nota válida dentro del rango 1.0 a 7.0")
    void testValidEvaluationScoreCreation() {
        EvaluationScore score = new EvaluationScore(6.5);
        assertEquals(6.5, score.value());
    }

    @ParameterizedTest
    @DisplayName("Debe lanzar InvalidEvaluationScoreException al intentar instanciar una nota fuera del rango 1.0 a 7.0")
    @ValueSource(doubles = {0.9, 0.0, -1.0, 7.1, 10.0})
    void testInvalidEvaluationScoreThrowsException(double invalidScore) {
        assertThrows(InvalidEvaluationScoreException.class, () -> new EvaluationScore(invalidScore));
    }
}
