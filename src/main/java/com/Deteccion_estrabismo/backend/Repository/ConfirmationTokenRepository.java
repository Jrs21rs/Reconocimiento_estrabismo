package com.Deteccion_estrabismo.backend.Repository;

import com.Deteccion_estrabismo.backend.Entities.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {

    //busqueda por token por su valor
    Optional<ConfirmationToken> findByToken(String token);

        //buscar tokens por usuario
        Optional<ConfirmationToken> findByUsuarioId(String usuarioId);
}
