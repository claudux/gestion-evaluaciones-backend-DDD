package com.institucion.evaluaciones.domain.repository;

import com.institucion.evaluaciones.domain.model.Evaluation;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * PATRÓN TÁCTICO DDD / CLEAN ARCHITECTURE: PUERTO DE REPOSITORIO DE EVALUACIONES
 * ============================================================================
 * Define el contrato de persistencia de dominio puro desacoplado de la base de datos.
 */
public interface EvaluationRepository {
    Evaluation save(Evaluation evaluation);
    Optional<Evaluation> findById(int id);
    List<Evaluation> findAll();
    void deleteById(int id);
    boolean existsById(int id);
}
