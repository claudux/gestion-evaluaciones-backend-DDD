package com.institucion;
import com.institucion.exception.EvaluationNotPublishedException;
import com.institucion.exception.InvalidEvaluationDateException;
import com.institucion.exception.InvalidCopyQuantityException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvaluationStatusPrinterServiceTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private EvaluationStatusPrinterService printerService;
    private Evaluation validEvaluation;

    @BeforeEach
    void setUp(){
        validEvaluation = new Evaluation(1, "claudio@latam.cl", LocalDate.now().plusDays(1));
    }

    @Test
    @DisplayName("Debe procesar o imprimir la evaluacion de forma exitosa y verificar con Mockito")
    void testRequestPrintJobSuccess(){
        //arrange
        int copies = 40;
        validEvaluation.publish();
        //act
        printerService.requestPrintJob(validEvaluation, copies);

        //assert: validar mock con llamada con los parametros
        verify(notificationService, times(1)).sendAlert("claudio@latam.cl", "Your printing job for evaluation 1 with 40 copies has been approved.");
    }

    @Test
    @DisplayName("Lanzar IllegalStateException si el estado no es Pendiente")
    void testRequestPrintJobInvalidStatus(){
        //arrange

        //act y assert
        assertThrows(EvaluationNotPublishedException.class, () -> {
            printerService.requestPrintJob(validEvaluation,5);
        });

        //verificar
        verifyNoInteractions(notificationService);
    }

    @Test
    @DisplayName("Lanzar InvalidEvaluationDateException si se intenta imprimir cuando no corresponde")
    void testResquestPrintJobSameDayException(){
        //arrange
        Evaluation todayEvaluation = new Evaluation(2, "claudio@latam.cl", LocalDate.now());
        todayEvaluation.publish();
        //act y assert
        assertThrows(InvalidEvaluationDateException.class, () -> {
            printerService.requestPrintJob(todayEvaluation, 5);
        });

        //verificar
        verifyNoInteractions(notificationService);
    }

    @Test
    void testEvaluationNotPublishedExceptionConstructor() {
        // Arrange
        String expectedMessage = "La evaluación no está publicada";

        // Act
        EvaluationNotPublishedException exception = new EvaluationNotPublishedException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testInvalidEvaluationDateExceptionConstructor() {
        // Arrange
        String expectedMessage = "La fecha de impresión debe ser anterior al examen";

        // Act
        InvalidEvaluationDateException exception = new InvalidEvaluationDateException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testInvalidCopyQuantityExceptionConstructor() {
        // Arrange
        String expectedMessage = "La cantidad de copias solicitada no es válida";

        // Act
        InvalidCopyQuantityException exception = new InvalidCopyQuantityException(expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {-5, 0, 101}) // evaluar limite inferior y superior
    void testRequestPrintJobInvalidCopyQuantityThrowsException(int invalidCopies) {
        // Arrange
        // crear una evaluacion que será ejecutada en 2 dias mas
        Evaluation evaluation = new Evaluation(1, "claudio@latam.cl", LocalDate.now().plusDays(2));
        evaluation.publish();

        // Act & Assert
        // verificar que se lance la excepcion
        assertThrows(InvalidCopyQuantityException.class, () -> {
            printerService.requestPrintJob(evaluation, invalidCopies);
        });
    }

    @Test
    @DisplayName("Debe persistir en el repositorio cuando este se encuentra inyectado")
    void testRequestPrintJobWithRepositorySave() {
        com.institucion.evaluaciones.domain.repository.EvaluationRepository mockRepo = mock(com.institucion.evaluaciones.domain.repository.EvaluationRepository.class);
        com.institucion.evaluaciones.application.EvaluationStatusPrinterService serviceWithRepo = 
            new com.institucion.evaluaciones.application.EvaluationStatusPrinterService(notificationService, mockRepo);

        validEvaluation.publish();
        serviceWithRepo.requestPrintJob(validEvaluation, 10);

        verify(notificationService, times(1)).sendAlert(eq("claudio@latam.cl"), anyString());
        verify(mockRepo, times(1)).save(validEvaluation);
    }
}
