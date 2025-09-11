package com.Deteccion_estrabismo.backend.Service;

import com.Deteccion_estrabismo.backend.Dto.AuthResponse;
import com.Deteccion_estrabismo.backend.Dto.LoginRequest;
import com.Deteccion_estrabismo.backend.Dto.RegisterRequest;
import com.Deteccion_estrabismo.backend.Repository.UsuariosRepository;
import com.Deteccion_estrabismo.backend.Usuario.Usuarios;
import com.google.rpc.context.AttributeContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private  UsuariosRepository usuariosRepository;
    private  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private  JwtService jwtService;
    @Autowired
    private  AuthenticationManager authenticationManager;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse Login(LoginRequest request) {
        //validar usuarios y password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getPassword()
                )


        );

        //buscar usuario en mogo
        Usuarios usuarios = usuariosRepository.findBycorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        //generacion de token
        String token = jwtService.generateToken(usuarios);

        // 🔹 Agregar log de depuración
        log.info("Usuario logueado: {} con rol: {} => Token generado: {}",
                usuarios.getCorreo(), usuarios.getrol(), token);
     AuthResponse response = new AuthResponse(token, usuarios.getrol().name(), usuarios.getCorreo());
    return response;

    }
    public AuthResponse register(RegisterRequest request) {
        // Crear usuario nuevo
        Usuarios usuario = new Usuarios();
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEdad(request.getEdad());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword())); // cifrar
        usuario.setNumeroTele(request.getNumeroTele());
        usuario.setRol(request.getRol());

        usuariosRepository.save(usuario);


        // Generar token para que inicie sesión automáticamente
        String token = jwtService.generateToken(usuario);


        // 🔹 Agregar log de depuración
        log.info("Usuario registrado: {} con rol: {} => Token generado: {}",
                usuario.getCorreo(), usuario.getrol(), token);

        AuthResponse response = new AuthResponse(token, usuario.getrol().name(), usuario.getCorreo());

        return response;
    }



}
