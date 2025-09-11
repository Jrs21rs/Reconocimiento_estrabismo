package com.Deteccion_estrabismo.backend.Controller;


import com.Deteccion_estrabismo.backend.Dto.AuthResponse;
import com.Deteccion_estrabismo.backend.Dto.LoginRequest;
import com.Deteccion_estrabismo.backend.Dto.RegisterRequest;
import com.Deteccion_estrabismo.backend.Repository.UsuariosRepository;
import com.Deteccion_estrabismo.backend.Service.AuthService;
import com.Deteccion_estrabismo.backend.Service.UsuariosService;
import com.Deteccion_estrabismo.backend.Usuario.Usuarios;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    @Autowired
    private UsuariosRepository usuariosRepository;
    @Autowired
    private UsuariosService usuariosService;
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        //Se valida el usuario y se genera el token
        return ResponseEntity.ok(authService.Login(request));

    }


    @PostMapping(value = "/registro", produces = "application/json")
    public ResponseEntity<AuthResponse> register( @RequestBody RegisterRequest request){

        return ResponseEntity.ok(authService.register(request));
    }
}
