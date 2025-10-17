package com.Deteccion_estrabismo.backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="medicos")
@PrimaryKeyJoinColumn(name= "usuario_id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico extends Usuarios{
    @Column(nullable= false, length= 150)
    private String especialidad;
    @Column(length=50)
    private String numerolicencia;
    @Column(length=100)
    private String institucion;//Clinica donde trabaja
    @Column(length=100)
    private String ciudad;
    @Column(length=500)
    private String descripcion; //breve bio del medico
    @ManyToMany
    @JoinTable(
            name = "medico_paciente",
            joinColumns = @JoinColumn(name = "medico_id"),
            inverseJoinColumns = @JoinColumn(name = "paciente_id")
    )
    private List<Pacientes> pacientes;



}
