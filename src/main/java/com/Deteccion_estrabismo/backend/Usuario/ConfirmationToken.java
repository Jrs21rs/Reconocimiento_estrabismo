package com.Deteccion_estrabismo.backend.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Confirmacion_Token") // nombre de la coleccion de mongodb
public class ConfirmationToken{
    @Id
    private String id;

    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;


    private String usuarioId;


}
