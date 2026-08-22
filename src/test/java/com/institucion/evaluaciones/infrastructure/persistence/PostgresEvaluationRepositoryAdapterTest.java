package com.institucion.evaluaciones.infrastructure.persistence;

import com.institucion.evaluaciones.domain.model.Evaluation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostgresEvaluationRepositoryAdapterTest {

    @Autowired
    private PostgresEvaluationRepositoryAdapter repositoryAdapter;

    @Test
    @DisplayName("Debe persistir y recuperar una evaluación académica en la base de datos")
    void shouldSaveAndRetrieveEvaluation() {
        Evaluation evaluation = new Evaluation(0, "Cálculo Numérico", "docente.calculo@institucion.cl", 30, LocalDate.now().plusDays(15), "Publicada");

        Evaluation saved = repositoryAdapter.save(evaluation);

        assertTrue(saved.getId() > 0, "El ID autoincremental debe ser mayor a cero");
        assertEquals("Cálculo Numérico", saved.getSubject());

        Optional<Evaluation> found = repositoryAdapter.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(30, found.get().getCopies());
        assertEquals("Publicada", found.get().getStatus());
    }

    @Test
    @DisplayName("Debe listar todas las evaluaciones persistidas")
    void shouldFindAllEvaluations() {
        Evaluation e1 = repositoryAdapter.save(new Evaluation(0, "Materia Alpha", "a@institucion.cl", 20, LocalDate.now().plusDays(5), "Pendiente"));
        Evaluation e2 = repositoryAdapter.save(new Evaluation(0, "Materia Beta", "b@institucion.cl", 25, LocalDate.now().plusDays(8), "Publicada"));

        List<Evaluation> all = repositoryAdapter.findAll();

        assertTrue(all.stream().anyMatch(e -> e.getId() == e1.getId()));
        assertTrue(all.stream().anyMatch(e -> e.getId() == e2.getId()));
    }

    @Test
    @DisplayName("Debe actualizar los datos de una evaluación")
    void shouldUpdateEvaluation() {
        Evaluation eval = repositoryAdapter.save(new Evaluation(0, "Materia Original", "doc@institucion.cl", 15, LocalDate.now().plusDays(10), "Pendiente"));

        eval.setSubject("Materia Modificada");
        eval.setCopies(40);
        eval.publish();

        Evaluation updated = repositoryAdapter.save(eval);

        assertEquals("Materia Modificada", updated.getSubject());
        assertEquals(40, updated.getCopies());
        assertEquals("Publicada", updated.getStatus());
    }

    @Test
    @DisplayName("Debe eliminar una evaluación por ID")
    void shouldDeleteEvaluationById() {
        Evaluation eval = repositoryAdapter.save(new Evaluation(0, "Materia Para Borrar", "doc@institucion.cl", 10, LocalDate.now().plusDays(10), "Pendiente"));
        int id = eval.getId();

        assertTrue(repositoryAdapter.existsById(id));
        repositoryAdapter.deleteById(id);

        assertFalse(repositoryAdapter.existsById(id));
        assertTrue(repositoryAdapter.findById(id).isEmpty());
    }
}
