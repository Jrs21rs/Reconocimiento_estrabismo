package com.Deteccion_estrabismo.backend.Repository;

import com.Deteccion_estrabismo.backend.Usuario.ConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationToken, String> {

    //busqueda por token por su valor
    Optional<ConfirmationToken> findByToken(String token);

    //buscar tokens por usuario
    Optional<ConfirmationToken> findByUsuarioId(String usuarioId);
}
