package com.institucion.evaluaciones.infrastructure.rest;

import com.institucion.evaluaciones.application.EvaluationService;
import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.infrastructure.rest.dto.CreateEvaluationRequestDto;
import com.institucion.evaluaciones.infrastructure.rest.dto.ErrorResponse;
import com.institucion.evaluaciones.infrastructure.rest.dto.EvaluationResponseDto;
import com.institucion.evaluaciones.infrastructure.rest.dto.UpdateEvaluationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ============================================================================
 * CAPA DE INFRAESTRUCTURA: CONTROLADOR REST DE EVALUACIONES (CRUD COMPLETO)
 * Mapeo semántico de rutas (/api/v1/evaluations) y contratos OpenAPI Swagger
 * ============================================================================
 */
@RestController
@RequestMapping("/api/v1/evaluations")
@CrossOrigin(origins = "*")
@Tag(name = "Evaluaciones Académicas", description = "Endpoints para la gestión integral del ciclo de vida de instrumentos de evaluación (CRUD, publicación, impresión y control de copias)")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Operation(
            summary = "Listar todas las evaluaciones",
            description = "Retorna el catálogo completo de instrumentos de evaluación registrados en el repositorio relacional PostgreSQL."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Catálogo de evaluaciones recuperado exitosamente",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseDto.class)))
            )
    })
    @GetMapping
    public ResponseEntity<List<EvaluationResponseDto>> getAllEvaluations() {
        List<EvaluationResponseDto> dtos = evaluationService.getAllEvaluations()
                .stream()
                .map(EvaluationResponseDto::fromDomain)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Obtener evaluación por ID",
            description = "Busca y retorna el detalle completo de un instrumento de evaluación específico mediante su identificador numérico."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluación encontrada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvaluationResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "La evaluación con el ID solicitado no existe",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponseDto> getEvaluationById(
            @Parameter(description = "Identificador único de la evaluación", example = "1", required = true)
            @PathVariable("id") int id) {
        Evaluation evaluation = evaluationService.getEvaluationById(id);
        return ResponseEntity.ok(EvaluationResponseDto.fromDomain(evaluation));
    }

    @Operation(
            summary = "Registrar nueva evaluación",
            description = "Registra un nuevo instrumento de evaluación con validaciones de dominio: rango de copias (1 a 49) y fecha del examen futura."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Evaluación registrada exitosamente en estado 'Pendiente'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvaluationResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos (copias fuera de rango 1..49 o fecha pasada)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<EvaluationResponseDto> createEvaluation(
            @Valid @RequestBody CreateEvaluationRequestDto requestDto) {
        Evaluation created = evaluationService.createEvaluation(
                requestDto.subject(),
                requestDto.copies(),
                requestDto.examDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(EvaluationResponseDto.fromDomain(created));
    }

    @Operation(
            summary = "Actualizar evaluación existente",
            description = "Actualiza los campos editables de una evaluación académica previamente registrada."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluación actualizada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvaluationResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evaluación no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de actualización incompatibles con las reglas de dominio",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EvaluationResponseDto> updateEvaluation(
            @Parameter(description = "Identificador único de la evaluación a modificar", example = "1", required = true)
            @PathVariable("id") int id,
            @RequestBody UpdateEvaluationRequestDto updateDto) {
        Evaluation existing = evaluationService.getEvaluationById(id);
        updateDto.applyTo(existing);
        Evaluation updated = evaluationService.updateEvaluation(id, existing);
        return ResponseEntity.ok(EvaluationResponseDto.fromDomain(updated));
    }

    @Operation(
            summary = "Eliminar evaluación por ID",
            description = "Remueve físicamente una evaluación académica de la base de datos PostgreSQL."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Evaluación eliminada correctamente (sin contenido en la respuesta)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evaluación no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(
            @Parameter(description = "Identificador único de la evaluación a eliminar", example = "6", required = true)
            @PathVariable("id") int id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Publicar evaluación",
            description = "Transiciona el estado de la evaluación a 'Publicada', habilitándola para la orden de impresión institucional."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluación publicada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvaluationResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evaluación no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/{id}/publish")
    public ResponseEntity<EvaluationResponseDto> publishEvaluation(
            @Parameter(description = "Identificador único de la evaluación a publicar", example = "1", required = true)
            @PathVariable("id") int id) {
        Evaluation published = evaluationService.publishEvaluation(id);
        return ResponseEntity.ok(EvaluationResponseDto.fromDomain(published));
    }

    @Operation(
            summary = "Imprimir evaluación (Reglas de Negocio)",
            description = "Valida las 3 reglas de negocio institucionales: 1) Estado 'Publicada', 2) Copias entre 1 y 49, 3) Ejecución exactamente el día anterior al examen. Al aprobar, transiciona a estado 'Completa'."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evaluación impresa exitosamente y marcada como 'Completa'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EvaluationResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evaluación no encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Regla violada: La evaluación no se encuentra en estado 'Publicada'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Regla violada: Número de copias inválido o fecha de impresión no autorizada (no es el día anterior)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/{id}/print")
    public ResponseEntity<EvaluationResponseDto> printEvaluation(
            @Parameter(description = "Identificador único de la evaluación a imprimir", example = "1", required = true)
            @PathVariable("id") int id) {
        Evaluation printed = evaluationService.printEvaluation(id);
        return ResponseEntity.ok(EvaluationResponseDto.fromDomain(printed));
    }
}
