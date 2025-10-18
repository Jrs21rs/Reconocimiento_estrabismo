package com.Deteccion_estrabismo.backend.Dto;

import com.Deteccion_estrabismo.backend.Entities.Rol;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegisterMedicoRequest extends RegisterRequest {
    private String especialidad;
    private String numerolicencia;
    private String institucion;//Clinica donde trabaja
    private String ciudad;
    private String descripcion; //breve bio del medico
    @Override
    public Rol getRol(){
        return Rol.MEDICO;
    }
}
