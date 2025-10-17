package com.Deteccion_estrabismo.backend.Dto;

import com.Deteccion_estrabismo.backend.Entities.Rol;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public abstract class RegisterRequest {

    private String nombres;
    private String apellidos;
    private Integer edad;
    private String correo;
    private String password; // se cifra con Bcrypt
    private String numeroTele;
    public abstract Rol getRol();

}



