package com.institucion.evaluaciones.infrastructure.persistence.repository;

import com.institucion.evaluaciones.infrastructure.persistence.entity.EvaluationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ============================================================================
 * INTERFAZ SPRING DATA JPA REPOSITORY
 * Provee operaciones CRUD sobre la tabla 'tbl_evaluations' en PostgreSQL
 * ============================================================================
 */
@Repository
public interface SpringDataEvaluationRepository extends JpaRepository<EvaluationJpaEntity, Integer> {
}
