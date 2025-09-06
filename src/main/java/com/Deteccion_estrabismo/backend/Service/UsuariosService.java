package com.Deteccion_estrabismo.backend.Service;

import com.Deteccion_estrabismo.backend.Repository.PacientesRepository;
import com.Deteccion_estrabismo.backend.Usuario.Pacientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PacientesService {
    @Autowired
    private PacientesRepository pacientesRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public  Pacientes registrarPacientes(Pacientes paciente){
        paciente.setPassword(passwordEncoder.encode(paciente.getPassword())); //Cifra la contraseña que se ingrese
        return pacientesRepository.save(paciente);

    }
}
