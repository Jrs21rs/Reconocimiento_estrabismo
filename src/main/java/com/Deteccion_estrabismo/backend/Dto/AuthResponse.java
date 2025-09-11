package com.Deteccion_estrabismo.backend.Dto;

import com.Deteccion_estrabismo.backend.Usuario.Rol;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private String rol;
    private String correo;

}
