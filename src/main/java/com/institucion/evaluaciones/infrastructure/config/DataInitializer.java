package com.institucion.evaluaciones.infrastructure.config;

import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.domain.repository.EvaluationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * ============================================================================
 * SEMILLERO DE DATOS INICIALES (DATA SEEDER)
 * Precarga la base de datos PostgreSQL en entorno de desarrollo ('dev') si está vacía
 * ============================================================================
 */
@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final EvaluationRepository evaluationRepository;

    public DataInitializer(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    @Override
    public void run(String... args) {
        if (evaluationRepository.findAll().isEmpty()) {
            log.info("🌱 Base de datos PostgreSQL vacía detectada. Inicializando catálogo de evaluaciones académicas...");

            // Evaluación lista para imprimir hoy (Examen es mañana -> día anterior es hoy)
            evaluationRepository.save(new Evaluation(0, "Matemáticas Avanzadas", "docente.mate@institucion.cl", 35, LocalDate.now().plusDays(1), "Publicada"));
            // Evaluación pendiente para mañana (Al publicarla hoy, queda lista para imprimir hoy)
            evaluationRepository.save(new Evaluation(0, "Historia Universal", "docente.historia@institucion.cl", 20, LocalDate.now().plusDays(1), "Pendiente"));
            // Evaluación histórica ya impresa
            evaluationRepository.save(new Evaluation(0, "Programación en Java", "docente.java@institucion.cl", 48, LocalDate.now().minusDays(5), "Completa"));
            // Evaluaciones futuras (aún no corresponde imprimir según la regla del día anterior)
            evaluationRepository.save(new Evaluation(0, "Bases de Datos", "docente.bd@institucion.cl", 40, LocalDate.now().plusDays(5), "Pendiente"));
            evaluationRepository.save(new Evaluation(0, "Ingeniería de Software", "docente.soft@institucion.cl", 45, LocalDate.now().plusDays(7), "Pendiente"));
            evaluationRepository.save(new Evaluation(0, "Arquitectura de Sistemas", "docente.arq@institucion.cl", 30, LocalDate.now().plusDays(4), "Publicada"));

            log.info("✅ Catálogo inicial de evaluaciones precargado exitosamente en PostgreSQL (6 registros creados).");
        } else {
            log.info("ℹ️ La base de datos ya contiene evaluaciones registradas. Se omite el sembrado inicial.");
        }
    }
}
