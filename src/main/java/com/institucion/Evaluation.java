package com.institucion;

import java.time.LocalDate;

//clase que permite crear objetos Evaluation, para encapsular datos y evitar hardcoreo.
/**
 * Clase adaptadora de compatibilidad para com.institucion.Evaluation.
 * Delega en la entidad del dominio com.institucion.evaluaciones.domain.model.Evaluation.
 */
public class Evaluation extends com.institucion.evaluaciones.domain.model.Evaluation {

    public Evaluation(int id, String studentEmail, LocalDate evaluationDate) {
        super(id, studentEmail, evaluationDate);
    }
}
