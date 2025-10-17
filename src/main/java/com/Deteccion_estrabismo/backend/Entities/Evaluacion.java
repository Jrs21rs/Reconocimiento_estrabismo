package com.Deteccion_estrabismo.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
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
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Pacientes paciente;

    @ManyToOne
    @JoinColumn(name = "responsable_id", nullable = false)
    private Responsable responsable;

    @Column(length = 500)
    private String resultado; // Ej: "Estrabismo leve, ojo izquierdo"

    @Column(length = 100)
    private String tipoEstrabismo; // Exotropía, Endotropía, etc.

    private LocalDate fechaEvaluacion;
}
