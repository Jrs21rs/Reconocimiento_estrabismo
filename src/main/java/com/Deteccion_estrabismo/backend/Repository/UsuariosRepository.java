package com.Deteccion_estrabismo.backend.Repository;
import com.Deteccion_estrabismo.backend.Usuario.Usuarios;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;


public interface UsuariosRepository extends MongoRepository<Usuarios,String> {

    Optional<Usuarios> findBycorreo(String correo);


    Optional<Usuarios> findById(String Id);
}
