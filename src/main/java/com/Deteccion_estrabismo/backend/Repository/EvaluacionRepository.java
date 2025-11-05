package com.Deteccion_estrabismo.backend.Repository;

<<<<<<< HEAD

=======
>>>>>>> 5e3fd6bdcb401eda56502e9cf0d46e58f172dfb9
import com.Deteccion_estrabismo.backend.Entities.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByPacienteId(Long pacienteId);
<<<<<<< HEAD
}
=======

    List<Evaluacion> findByResultado(boolean resultado);

    List<Evaluacion> findByPacienteResponsableId(Long responsableId);

    List<Evaluacion> findByPacienteDocumentoIdentidad(Integer documentoIdentidad);

}
>>>>>>> 5e3fd6bdcb401eda56502e9cf0d46e58f172dfb9
