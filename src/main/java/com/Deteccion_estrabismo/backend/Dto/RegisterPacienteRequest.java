package com.Deteccion_estrabismo.backend.Dto;

import com.Deteccion_estrabismo.backend.Entities.Rol;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegisterPacienteRequest extends RegisterRequest {
    private String genero; // "Masculino", "Femenino", "Otro"
    private String fotoPaciente; // imagen tomada para análisis de estrabismo
    private String resultadoDeteccion; // "Normal", "Estrabismo leve", etc.
    private String tipoEstrabismo; // opcional: exotropía, endotropía, etc.
    private String observaciones; // anotaciones médicas
    private Integer documentoIdentidadResponsable;
    private String parentesco;

    @Override
    public Rol getRol() {
        return Rol.PACIENTE;
    }

}
