package com.Deteccion_estrabismo.backend.Controller;


import com.Deteccion_estrabismo.backend.Repository.UsuariosRepository;
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
    return ResponseEntity.ok(new AuthResponse());

    }


    @PostMapping("/registro")
    public ResponseEntity<Usuarios> RegistrarUsuarios(@RequestBody Usuarios usuarios){
        Usuarios nuevoUsuario = usuariosService.registrarUsuarios(usuarios);
        return ResponseEntity.ok(nuevoUsuario);
    }
}
