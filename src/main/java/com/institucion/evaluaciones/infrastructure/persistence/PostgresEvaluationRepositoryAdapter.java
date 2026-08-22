package com.institucion.evaluaciones.infrastructure.persistence;

import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.domain.repository.EvaluationRepository;
import com.institucion.evaluaciones.infrastructure.persistence.entity.EvaluationJpaEntity;
import com.institucion.evaluaciones.infrastructure.persistence.repository.SpringDataEvaluationRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ============================================================================
 * ADAPTADOR DE PERSISTENCIA JPA / POSTGRESQL
 * Implementa el puerto de dominio EvaluationRepository usando Spring Data JPA
 * ============================================================================
 */
@Repository
@Primary
public class PostgresEvaluationRepositoryAdapter implements EvaluationRepository {

    private final SpringDataEvaluationRepository springDataRepository;

    public PostgresEvaluationRepositoryAdapter(SpringDataEvaluationRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    @Transactional
    public Evaluation save(Evaluation evaluation) {
        EvaluationJpaEntity entity = EvaluationJpaEntity.fromDomain(evaluation);
        EvaluationJpaEntity savedEntity = springDataRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evaluation> findById(int id) {
        return springDataRepository.findById(id)
                .map(EvaluationJpaEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Evaluation> findAll() {
        return springDataRepository.findAll()
                .stream()
                .map(EvaluationJpaEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        springDataRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(int id) {
        return springDataRepository.existsById(id);
    }
}
