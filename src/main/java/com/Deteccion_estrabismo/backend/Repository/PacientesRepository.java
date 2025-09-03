package com.Deteccion_estrabismo.backend.Repository;
import com.Deteccion_estrabismo.backend.Usuario.Pacientes;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;


public interface PacientesRepository extends MongoRepository<Pacientes,Integer> {

    Optional<Pacientes> findBycorreo(String correo);
}
