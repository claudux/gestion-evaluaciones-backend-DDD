package com.institucion.evaluaciones.application;

//interfaz para enviar mensaje al estudiante cuando cambie el estado de su evaluación.
/**
 * CAPA DE APLICACIÓN (Application Layer): Puerto/Interfaz para notificaciones.
 */
public interface NotificationService {
    void sendAlert(String studentEmail, String message);
}
