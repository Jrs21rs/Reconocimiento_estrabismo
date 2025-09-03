package com.Deteccion_estrabismo.backend.Controller;

import com.Deteccion_estrabismo.backend.Repository.PacientesRepository;
import com.Deteccion_estrabismo.backend.Service.PacientesService;
import com.Deteccion_estrabismo.backend.Usuario.Pacientes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Pacientes")
public class PacientesController {

    @Autowired
    private PacientesRepository pacientesRepository;
    @Autowired
    private PacientesService pacientesService;

    @PostMapping("/registro")
    public ResponseEntity<Pacientes> regostrarPacientes(@RequestBody Pacientes pacientes){
        Pacientes nuevoPaciente = pacientesService.registrarPacientes(pacientes);
        return ResponseEntity.ok(nuevoPaciente);
    }

}
