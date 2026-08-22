package com.institucion.evaluaciones.infrastructure.persistence;

import com.institucion.evaluaciones.domain.model.Evaluation;
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
        repository.clear();
    }

    @Test
    @DisplayName("Debe guardar y recuperar una evaluación por ID")
    void testSaveAndFindById() {
        Evaluation eval = new Evaluation(10, "Bases de Datos", "estudiante@latam.cl", 25, LocalDate.now().plusDays(2), "Publicada");
        repository.save(eval);

        Optional<Evaluation> found = repository.findById(10);
        assertTrue(found.isPresent());
        assertEquals("Bases de Datos", found.get().getSubject());
        assertEquals("estudiante@latam.cl", found.get().getStudentEmail());
        assertEquals(25, found.get().getCopies());
        assertEquals("Publicada", found.get().getStatus());
    }

    @Test
    @DisplayName("Debe listar todas las evaluaciones guardadas")
    void testFindAll() {
        repository.save(new Evaluation(1, "Estructuras", 20, LocalDate.now().plusDays(1)));
        repository.save(new Evaluation(2, "Algoritmos", 30, LocalDate.now().plusDays(1)));

        assertEquals(2, repository.findAll().size());
    }

    @Test
    @DisplayName("Debe eliminar una evaluación por ID")
    void testDeleteById() {
        Evaluation eval = new Evaluation(5, "Redes", 15, LocalDate.now().plusDays(1));
        repository.save(eval);
        assertTrue(repository.findById(5).isPresent());

        repository.deleteById(5);
        assertFalse(repository.findById(5).isPresent());
    }
}
