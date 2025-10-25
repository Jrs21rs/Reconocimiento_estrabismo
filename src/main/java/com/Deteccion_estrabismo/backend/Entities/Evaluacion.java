package com.Deteccion_estrabismo.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "evaluaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Pacientes paciente;

    @Column
    private boolean resultado; // Ej: "Estrabismo leve, ojo izquierdo"

    @Column(length = 100)
    private String tipoEstrabismo; // Exotropía, Endotropía, etc.

    @Column(name = "fecha_evaluacion")
    private LocalDate fechaEvaluacion;

    @Column(name = "confianza_prediccion")
    private Float confianzaPrediccion;

    @Column(name = "imagen_path")
    private String imagenPath;
}
