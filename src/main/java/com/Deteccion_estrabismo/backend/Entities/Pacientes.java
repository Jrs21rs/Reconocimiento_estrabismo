package com.Deteccion_estrabismo.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="pacientes")
@PrimaryKeyJoinColumn(name = "usuario_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pacientes extends Usuarios{
    @Column(nullable = false)
    private String documentoIdentidad; // si aplica (puede ser opcional para menores)

    @Column(nullable = false)
    private String genero; // "Masculino", "Femenino", "Otro"

    @Column(nullable = false)
    private String Institucion;//clinica o institucion donde hace examenes regulares

    @Column(nullable = false)
    private String fechaNacimiento;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Responsable responsable; // vínculo con el acudiente

    @Column(length = 255)
    private String fotoPaciente; // imagen tomada para análisis de estrabismo

    @Column(length = 50)
    private String resultadoDeteccion; // "Normal", "Estrabismo leve", etc.

    @Column(length = 100)
    private String tipoEstrabismo; // opcional: exotropía, endotropía, etc.

        @Column(length = 500)
    private String observaciones; // anotaciones médicas
    @ManyToMany(mappedBy = "pacientes")
    private List<Medico> medicos;



}
