package com.Deteccion_estrabismo.backend.Controller;

import com.Deteccion_estrabismo.backend.Dto.RegisterResponse;
import com.Deteccion_estrabismo.backend.Dto.UpdateRequest;
import com.Deteccion_estrabismo.backend.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Pacientes")
public class PacientesController {
    @Autowired
    private AuthService authService;
    @PutMapping("/Update")
    public ResponseEntity<RegisterResponse> updatePaciente(@RequestBody UpdateRequest request, Authentication authentication){
        System.out.println("Recibido: " + request);
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(RegisterResponse.builder()
                            .success(false)
                            .error("Usuario no autenticado")
                            .build());
        }
        String correo = authentication.getName();

        // Llamamos al servicio, que ya se encarga de buscar la entidad
        RegisterResponse response = authService.UpdatePaciente(correo, request);

        return ResponseEntity.ok(authService.UpdatePaciente(correo,request));

    }


}
