package com.institucion.evaluaciones.infrastructure.notification;

import com.institucion.evaluaciones.application.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationService.class);

    @Override
    public void sendAlert(String studentEmail, String message) {
        log.info("📢 [Notificación Académica] Para: {} | Mensaje: {}", studentEmail, message);
    }
}
