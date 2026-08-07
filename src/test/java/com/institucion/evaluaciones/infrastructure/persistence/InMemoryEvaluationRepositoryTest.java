package com.institucion.evaluaciones.infrastructure.persistence;

import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.domain.model.valueobjects.EvaluationScore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el repositorio en memoria InMemoryEvaluationRepository.
 */
class InMemoryEvaluationRepositoryTest {

    private InMemoryEvaluationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryEvaluationRepository();
    }

    @Test
    @DisplayName("Debe guardar y recuperar una evaluación por ID")
    void testSaveAndFindById() {
        Evaluation eval = new Evaluation(10, "estudiante@latam.cl", LocalDate.now().plusDays(2), new EvaluationScore(6.8));
        repository.save(eval);

        Optional<Evaluation> found = repository.findById(10);
        assertTrue(found.isPresent());
        assertEquals("estudiante@latam.cl", found.get().getStudentEmail());
        assertNotNull(found.get().getScore());
        assertEquals(6.8, found.get().getScore().value());
    }

    @Test
    @DisplayName("Debe listar todas las evaluaciones guardadas")
    void testFindAll() {
        repository.save(new Evaluation(1, "e1@test.com", LocalDate.now().plusDays(1)));
        repository.save(new Evaluation(2, "e2@test.com", LocalDate.now().plusDays(1)));

        assertEquals(2, repository.findAll().size());
    }
}
