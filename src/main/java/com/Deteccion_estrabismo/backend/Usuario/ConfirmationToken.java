package com.Deteccion_estrabismo.backend.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "Confirmacion_Token") // nombre de la coleccion de mongodb
public class ConfirmationToken{
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Id
    private String id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String usuarioId;

    public ConfirmationToken(String usuarioId){
        this.usuarioId=usuarioId;
        this.token= UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.expiresAt = createdAt.plusMinutes(15);
    }


}
