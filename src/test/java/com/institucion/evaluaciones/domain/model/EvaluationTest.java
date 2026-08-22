package com.institucion.evaluaciones.domain.model;

import com.institucion.evaluaciones.domain.exception.InvalidCopyQuantityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la entidad Evaluation en la capa de dominio.
 */
class EvaluationTest {

    @Test
    @DisplayName("Debe instanciar correctamente una Evaluación con sus campos válidos")
    void testEvaluationInstantiation() {
        Evaluation eval = new Evaluation(1, "Estructuras de Datos", "test@latam.cl", 30, LocalDate.now().plusDays(2), "Pendiente");
        assertEquals(1, eval.getId());
        assertEquals("Estructuras de Datos", eval.getSubject());
        assertEquals("test@latam.cl", eval.getStudentEmail());
        assertEquals(30, eval.getCopies());
        assertEquals("Pendiente", eval.getStatus());
    }

    @Test
    @DisplayName("Debe cambiar estado a Publicada y Completa")
    void testStateTransitions() {
        Evaluation eval = new Evaluation(2, "Bases de Datos", 25, LocalDate.now().plusDays(3));
        assertEquals("Pendiente", eval.getStatus());

        eval.publish();
        assertEquals("Publicada", eval.getStatus());

        eval.markAsPrinted();
        assertEquals("Completa", eval.getStatus());
    }

    @Test
    @DisplayName("Debe lanzar InvalidCopyQuantityException si las copias son <= 0 o >= 50")
    void testInvalidCopiesThrowsException() {
        assertThrows(InvalidCopyQuantityException.class, () ->
                new Evaluation(3, "Programación", 0, LocalDate.now().plusDays(1))
        );
        assertThrows(InvalidCopyQuantityException.class, () ->
                new Evaluation(4, "Programación", 50, LocalDate.now().plusDays(1))
        );
    }
}
