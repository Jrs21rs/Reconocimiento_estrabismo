package com.Deteccion_estrabismo.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "pacientes")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pacientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nombres;
    @Column(nullable = false)
    private Integer documentoIdentidad;
    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private TipoDocumento tipoDocumento;
    @Column(nullable = false, length = 100)
    private String apellidos;
    @Column(nullable = false, length = 100)
    private Date fechaNacimiento;

    @Column(nullable = false)
    private String genero; // "Masculino", "Femenino", "Otro"

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Responsable responsable; // vínculo con el acudiente

    @Column(length = 255)
    private String fotoPaciente; // imagen tomada para análisis de estrabismo

}
