package com.Deteccion_estrabismo.backend.Dto;

import com.Deteccion_estrabismo.backend.Entities.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private boolean success;
    private String error;
    private Long usuarioId;
    private Long entidadEspecificaId;
    private Rol rol;

    public static RegisterResponse success(Long usuarioId, Long entidadId, Rol rol) {
        return RegisterResponse.builder()
                .success(true)
                .error(null)
                .usuarioId(usuarioId)
                .entidadEspecificaId(entidadId)
                .rol(rol)
                .build();
    }

    public static RegisterResponse error(String message) {
        return RegisterResponse.builder()
                .success(false)
                .error(message)
                .usuarioId(null)
                .entidadEspecificaId(null)
                .rol(null)
                .build();
    }
}