package com.Deteccion_estrabismo.backend.Dto;

import com.Deteccion_estrabismo.backend.Entities.Rol;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    private String nombres;
    private String apellidos;
    private Integer edad;
    private String correo;
    private String password; // se cifra con Bcrypt
    private String numeroTele;
    private Rol rol;
}



