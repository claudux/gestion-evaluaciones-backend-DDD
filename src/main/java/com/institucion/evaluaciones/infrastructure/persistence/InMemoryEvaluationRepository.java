package com.institucion.evaluaciones.infrastructure.persistence;

import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.domain.repository.EvaluationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ============================================================================
 * CLEAN ARCHITECTURE: CAPA DE INFRAESTRUCTURA (Infrastructure Layer)
 * ============================================================================
 * Implementación concreta en memoria del repositorio de evaluaciones.
 * 
 * Permite ejecutar pruebas, simular persistencia y alimentar los casos de uso
 * sin requerir una conexión a base de datos real en esta etapa.
 */
public class InMemoryEvaluationRepository implements EvaluationRepository {

    private final Map<Integer, Evaluation> database = new ConcurrentHashMap<>();

    @Override
    public Evaluation save(Evaluation evaluation) {
        database.put(evaluation.getId(), evaluation);
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
}
