package com.Deteccion_estrabismo.backend.Controller;


import com.Deteccion_estrabismo.backend.Dto.*;
import com.Deteccion_estrabismo.backend.Repository.UsuariosRepository;
import com.Deteccion_estrabismo.backend.Service.AuthService;
import com.Deteccion_estrabismo.backend.Service.UsuariosService;
import com.Deteccion_estrabismo.backend.Usuario.Usuarios;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        //Se valida el usuario y se genera el token
        return ResponseEntity.ok(authService.Login(request));

    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/confirm")
    public AuthResponse confirm(@RequestParam String token) {
        return authService.confirmToken(token);
    }


}
