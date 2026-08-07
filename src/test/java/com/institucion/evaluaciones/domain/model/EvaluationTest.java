package com.institucion.evaluaciones.domain.model;

import com.institucion.evaluaciones.domain.model.valueobjects.EvaluationScore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad Evaluation en la capa de dominio.
 */
class EvaluationTest {

    @Test
    @DisplayName("Debe instanciar y permitir manipular la nota de una evaluación")
    void testEvaluationScoreAssignment() {
        Evaluation eval = new Evaluation(1, "test@latam.cl", LocalDate.now().plusDays(2));
        assertNull(eval.getScore());

        EvaluationScore score = new EvaluationScore(6.8);
        eval.setScore(score);

        assertNotNull(eval.getScore());
        assertEquals(6.8, eval.getScore().value());
        assertEquals("6.8", eval.getScore().toString());
    }

    @Test
    @DisplayName("Debe instanciar Evaluation con el constructor enriquecido de Value Object")
    void testEvaluationConstructorWithScore() {
        EvaluationScore score = new EvaluationScore(5.5);
        Evaluation eval = new Evaluation(2, "profesor@latam.cl", LocalDate.now().plusDays(3), score);

        assertEquals(2, eval.getId());
        assertEquals("profesor@latam.cl", eval.getStudentEmail());
        assertEquals(5.5, eval.getScore().value());
    }
}
