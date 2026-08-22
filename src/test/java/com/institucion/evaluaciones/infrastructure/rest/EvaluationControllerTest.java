package com.institucion.evaluaciones.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institucion.evaluaciones.application.EvaluationService;
import com.institucion.evaluaciones.domain.exception.EvaluationNotFoundException;
import com.institucion.evaluaciones.domain.exception.EvaluationNotPublishedException;
import com.institucion.evaluaciones.domain.exception.InvalidCopyQuantityException;
import com.institucion.evaluaciones.domain.exception.InvalidEvaluationDateException;
import com.institucion.evaluaciones.domain.model.Evaluation;
import com.institucion.evaluaciones.infrastructure.rest.dto.CreateEvaluationRequestDto;
import com.institucion.evaluaciones.infrastructure.rest.dto.UpdateEvaluationRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EvaluationController.class)
class EvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EvaluationService evaluationService;

    @Test
    @DisplayName("GET /api/v1/evaluations retorna lista de evaluaciones con HTTP 200")
    void shouldReturnAllEvaluations() throws Exception {
        Evaluation eval1 = new Evaluation(1, "Estructuras de Datos", "p.cft@institucion.cl", 30, LocalDate.now().plusDays(20), "Publicada");
        Evaluation eval2 = new Evaluation(2, "Bases de Datos", "d.bd@institucion.cl", 25, LocalDate.now().plusDays(15), "Completa");

        when(evaluationService.getAllEvaluations()).thenReturn(List.of(eval1, eval2));

        mockMvc.perform(get("/api/v1/evaluations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].subject").value("Estructuras de Datos"))
                .andExpect(jsonPath("$[0].status").value("Publicada"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status").value("Completa"));
    }

    @Test
    @DisplayName("GET /api/v1/evaluations/{id} con ID existente retorna HTTP 200")
    void shouldReturnEvaluationById() throws Exception {
        Evaluation eval = new Evaluation(1, "Estructuras de Datos", "p.cft@institucion.cl", 30, LocalDate.now().plusDays(20), "Publicada");

        when(evaluationService.getEvaluationById(1)).thenReturn(eval);

        mockMvc.perform(get("/api/v1/evaluations/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subject").value("Estructuras de Datos"));
    }

    @Test
    @DisplayName("GET /api/v1/evaluations/{id} con ID inexistente retorna HTTP 404")
    void shouldReturn404WhenEvaluationNotFound() throws Exception {
        when(evaluationService.getEvaluationById(999)).thenThrow(new EvaluationNotFoundException(999));

        mockMvc.perform(get("/api/v1/evaluations/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("POST /api/v1/evaluations crea una nueva evaluación y retorna HTTP 201")
    void shouldCreateEvaluationSuccessfully() throws Exception {
        CreateEvaluationRequestDto requestDto = new CreateEvaluationRequestDto(
                "Arquitectura Limpia",
                25,
                LocalDate.now().plusDays(10)
        );

        Evaluation created = new Evaluation(5, "Arquitectura Limpia", 25, LocalDate.now().plusDays(10));

        when(evaluationService.createEvaluation(anyString(), anyInt(), any(LocalDate.class)))
                .thenReturn(created);

        mockMvc.perform(post("/api/v1/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.subject").value("Arquitectura Limpia"))
                .andExpect(jsonPath("$.status").value("Pendiente"));
    }

    @Test
    @DisplayName("PUT /api/v1/evaluations/{id} actualiza una evaluación existente y retorna HTTP 200")
    void shouldUpdateEvaluationSuccessfully() throws Exception {
        UpdateEvaluationRequestDto updateDto = new UpdateEvaluationRequestDto(
                "Arquitectura Limpia y DDD",
                "docente.expert@institucion.cl",
                35,
                LocalDate.now().plusDays(15),
                "Publicada"
        );

        Evaluation existing = new Evaluation(1, "Arquitectura Limpia", "docente@institucion.cl", 25, LocalDate.now().plusDays(10), "Pendiente");
        Evaluation updated = new Evaluation(1, "Arquitectura Limpia y DDD", "docente.expert@institucion.cl", 35, LocalDate.now().plusDays(15), "Publicada");

        when(evaluationService.getEvaluationById(1)).thenReturn(existing);
        when(evaluationService.updateEvaluation(eq(1), any(Evaluation.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/evaluations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.subject").value("Arquitectura Limpia y DDD"))
                .andExpect(jsonPath("$.copies").value(35))
                .andExpect(jsonPath("$.status").value("Publicada"));
    }

    @Test
    @DisplayName("DELETE /api/v1/evaluations/{id} elimina una evaluación y retorna HTTP 204")
    void shouldDeleteEvaluationSuccessfully() throws Exception {
        doNothing().when(evaluationService).deleteEvaluation(1);

        mockMvc.perform(delete("/api/v1/evaluations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/evaluations/{id} con ID inexistente retorna HTTP 404")
    void shouldReturn404WhenDeletingNonExistentEvaluation() throws Exception {
        doThrow(new EvaluationNotFoundException(999)).when(evaluationService).deleteEvaluation(999);

        mockMvc.perform(delete("/api/v1/evaluations/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("POST /api/v1/evaluations/{id}/publish publica la evaluación y retorna HTTP 200")
    void shouldPublishEvaluationSuccessfully() throws Exception {
        Evaluation published = new Evaluation(1, "Estructuras de Datos", "p.cft@institucion.cl", 30, LocalDate.now().plusDays(20), "Publicada");

        when(evaluationService.publishEvaluation(1)).thenReturn(published);

        mockMvc.perform(post("/api/v1/evaluations/1/publish"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("Publicada"));
    }

    @Test
    @DisplayName("POST /api/v1/evaluations con fecha pasada retorna HTTP 400")
    void shouldReturn400WhenCreatingEvaluationWithPastDate() throws Exception {
        CreateEvaluationRequestDto requestDto = new CreateEvaluationRequestDto(
                "Historia Antigua",
                20,
                LocalDate.now().minusDays(5)
        );

        when(evaluationService.createEvaluation(anyString(), anyInt(), any(LocalDate.class)))
                .thenThrow(new InvalidEvaluationDateException("No se puede registrar una evaluación con fecha en el pasado."));

        mockMvc.perform(post("/api/v1/evaluations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request - Invalid Date"));
    }

    @Test
    @DisplayName("POST /api/v1/evaluations/{id}/print imprime la evaluación y retorna HTTP 200")
    void shouldPrintEvaluationSuccessfully() throws Exception {
        Evaluation printed = new Evaluation(1, "Estructuras de Datos", "p.cft@institucion.cl", 30, LocalDate.now().plusDays(20), "Completa");

        when(evaluationService.printEvaluation(1)).thenReturn(printed);

        mockMvc.perform(post("/api/v1/evaluations/1/print")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("Completa"));
    }

    @Test
    @DisplayName("POST /api/v1/evaluations/{id}/print sobre evaluación no publicada retorna HTTP 422")
    void shouldReturn422WhenPrintingUnpublishedEvaluation() throws Exception {
        when(evaluationService.printEvaluation(1))
                .thenThrow(new EvaluationNotPublishedException("Cannot print an evaluation that is not published."));

        mockMvc.perform(post("/api/v1/evaluations/1/print")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Unprocessable Entity - Evaluation Not Published"))
                .andExpect(jsonPath("$.message").value("Cannot print an evaluation that is not published."));
    }

    @Test
    @DisplayName("POST /api/v1/evaluations/{id}/print con fecha en el pasado o hoy retorna HTTP 400")
    void shouldReturn400WhenPrintDateIsInvalid() throws Exception {
        when(evaluationService.printEvaluation(1))
                .thenThrow(new InvalidEvaluationDateException("La impresión solo puede realizarse antes o el día anterior a la evaluación."));

        mockMvc.perform(post("/api/v1/evaluations/1/print")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request - Invalid Date"));
    }
}
