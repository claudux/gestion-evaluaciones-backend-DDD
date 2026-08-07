package com.institucion.evaluaciones.domain.repository;

import com.institucion.evaluaciones.domain.model.Evaluation;
import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * PATRÓN TÁCTICO DDD / CLEAN ARCHITECTURE: PATRÓN REPOSITORIO (Contrato)
 * ============================================================================
 * Interfaz declarada en la Capa de Dominio.
 * 
 * Propósito:
 * - Define las operaciones de persistencia desde la perspectiva del negocio.
 * - Desacopla la lógica de aplicación de las bases de datos (PostgreSQL, MongoDB, In-Memory).
 * - Cumple con la Inversión de Dependencias (DIP).
 */
public interface EvaluationRepository {
    Evaluation save(Evaluation evaluation);
    Optional<Evaluation> findById(int id);
    List<Evaluation> findAll();
    void deleteById(int id);
}
