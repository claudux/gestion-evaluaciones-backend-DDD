package com.institucion.evaluaciones.application;

/**
 * CAPA DE APLICACIÓN (Application Layer): Puerto/Interfaz para notificaciones.
 */
public interface NotificationService {
    void sendAlert(String studentEmail, String message);
    default void notify(String message) {
        sendAlert("sistema@institucion.cl", message);
    }
}
