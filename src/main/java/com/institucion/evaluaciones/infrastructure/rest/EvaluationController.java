package com.institucion.evaluaciones.infrastructure.rest;

import com.institucion.evaluaciones.application.EvaluationService;
import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.infrastructure.rest.dto.CreateEvaluationRequestDto;
import com.institucion.evaluaciones.infrastructure.rest.dto.EvaluationResponseDto;
import com.institucion.evaluaciones.infrastructure.rest.dto.UpdateEvaluationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================================================================
 * CAPA DE INFRAESTRUCTURA: CONTROLADOR REST DE EVALUACIONES (CRUD COMPLETO)
 * Mapeo semántico de rutas (/api/v1/evaluations) y documentación OpenAPI Swagger
 * ============================================================================
 */
@RestController
@RequestMapping("/api/v1/evaluations")
@CrossOrigin(origins = "*")
@Tag(name = "Evaluaciones Académicas", description = "Operaciones CRUD y flujo de estados (registro, publicación e impresión) de evaluaciones")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Operation(summary = "Listar todas las evaluaciones", description = "Retorna el catálogo completo de evaluaciones académicas registradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<EvaluationResponseDto>> getAllEvaluations() {
        List<EvaluationResponseDto> dtos = evaluationService.getAllEvaluations()
                .stream()
                .map(EvaluationResponseDto::fromDomain)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener evaluación por ID", description = "Busca y retorna el detalle de una evaluación académica específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación encontrada"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponseDto> getEvaluationById(@PathVariable("id") int id) {
        Evaluation evaluation = evaluationService.getEvaluationById(id);
        return ResponseEntity.ok(EvaluationResponseDto.fromDomain(evaluation));
    }

    @Operation(summary = "Registrar nueva evaluación", description = "Crea una nueva evaluación académica aplicando validaciones de copias y fecha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evaluación registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (copias fuera de rango o fecha pasada)")
    })
    @PostMapping
    public ResponseEntity<EvaluationResponseDto> createEvaluation(@RequestBody CreateEvaluationRequestDto requestDto) {
        Evaluation created = evaluationService.createEvaluation(
                requestDto.subject(),
                requestDto.copies(),
                requestDto.examDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(EvaluationResponseDto.fromDomain(created));
    }

    @Operation(summary = "Actualizar evaluación existente", description = "Actualiza los campos de una evaluación académica según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EvaluationResponseDto> updateEvaluation(
            @PathVariable("id") int id,
            @RequestBody UpdateEvaluationRequestDto updateDto) {
        Evaluation existing = evaluationService.getEvaluationById(id);
        updateDto.applyTo(existing);
        Evaluation updated = evaluationService.updateEvaluation(id, existing);
        return ResponseEntity.ok(EvaluationResponseDto.fromDomain(updated));
    }

    @Operation(summary = "Eliminar evaluación por ID", description = "Elimina físicamente una evaluación académica de la base de datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evaluación eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable("id") int id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Publicar evaluación", description = "Transiciona el estado de la evaluación a 'Publicada' permitiendo su posterior impresión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación publicada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    @PostMapping("/{id}/publish")
    public ResponseEntity<EvaluationResponseDto> publishEvaluation(@PathVariable("id") int id) {
        Evaluation published = evaluationService.publishEvaluation(id);
        return ResponseEntity.ok(EvaluationResponseDto.fromDomain(published));
    }

    @Operation(summary = "Imprimir evaluación", description = "Valida las 3 reglas de negocio de impresión y marca la evaluación como 'Completa'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evaluación impresa exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evaluación no encontrada"),
            @ApiResponse(responseCode = "422", description = "La evaluación no está en estado 'Publicada'"),
            @ApiResponse(responseCode = "400", description = "Violación de reglas de copias o fecha de impresión")
    })
    @PostMapping("/{id}/print")
    public ResponseEntity<EvaluationResponseDto> printEvaluation(@PathVariable("id") int id) {
        Evaluation printed = evaluationService.printEvaluation(id);
        return ResponseEntity.ok(EvaluationResponseDto.fromDomain(printed));
    }
}
