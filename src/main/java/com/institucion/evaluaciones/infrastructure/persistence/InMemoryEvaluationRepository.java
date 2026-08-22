package com.institucion.evaluaciones.infrastructure.persistence;

import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.domain.repository.EvaluationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ============================================================================
 * CLEAN ARCHITECTURE: CAPA DE INFRAESTRUCTURA (Infrastructure Layer)
 * Repositorio en memoria con datos semilla idénticos al catálogo institucional
 * ============================================================================
 */
@Repository
public class InMemoryEvaluationRepository implements EvaluationRepository {

    private final Map<Integer, Evaluation> database = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public InMemoryEvaluationRepository() {
        save(new Evaluation(1, "Matemáticas Avanzadas", "docente.mate@institucion.cl", 35, LocalDate.of(2026, 8, 10), "Publicada"));
        save(new Evaluation(2, "Historia Universal", "docente.historia@institucion.cl", 20, LocalDate.of(2026, 8, 12), "Pendiente"));
        save(new Evaluation(3, "Programación en Java", "docente.java@institucion.cl", 48, LocalDate.of(2026, 7, 1), "Completa"));
        save(new Evaluation(4, "Bases de Datos", "docente.bd@institucion.cl", 40, LocalDate.of(2026, 8, 18), "Pendiente"));
        save(new Evaluation(5, "Ingeniería de Software", "docente.soft@institucion.cl", 45, LocalDate.of(2026, 8, 22), "Pendiente"));
        save(new Evaluation(6, "Arquitectura de Sistemas", "docente.arq@institucion.cl", 30, LocalDate.of(2026, 8, 25), "Publicada"));
        save(new Evaluation(7, "Estructuras de Datos", "docente.ed@institucion.cl", 49, LocalDate.of(2026, 7, 15), "Completa"));
        save(new Evaluation(8, "Inteligencia Artificial", "docente.ia@institucion.cl", 25, LocalDate.of(2026, 9, 5), "Pendiente"));
        save(new Evaluation(9, "Física Cuántica", "docente.fisica@institucion.cl", 15, LocalDate.of(2026, 9, 10), "Publicada"));
        save(new Evaluation(10, "Cálculo Multivariable", "docente.calculo@institucion.cl", 49, LocalDate.of(2026, 7, 28), "Completa"));
    }

    @Override
    public Evaluation save(Evaluation evaluation) {
        database.put(evaluation.getId(), evaluation);
        if (evaluation.getId() >= idGenerator.get()) {
            idGenerator.set(evaluation.getId() + 1);
        }
        return evaluation;
    }

    @Override
    public Optional<Evaluation> findById(int id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Evaluation> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deleteById(int id) {
        database.remove(id);
    }

    @Override
    public boolean existsById(int id) {
        return database.containsKey(id);
    }

    public void clear() {
        database.clear();
    }

    public int generateNextId() {
        return idGenerator.getAndIncrement();
    }
}
