package com.Deteccion_estrabismo.backend.Service;

import com.Deteccion_estrabismo.backend.Dto.AuthResponse;
import com.Deteccion_estrabismo.backend.Dto.LoginRequest;
import com.Deteccion_estrabismo.backend.Dto.RegisterRequest;
import com.Deteccion_estrabismo.backend.Repository.ConfirmationTokenRepository;
import com.Deteccion_estrabismo.backend.Repository.UsuariosRepository;
import com.Deteccion_estrabismo.backend.Usuario.ConfirmationToken;
import com.Deteccion_estrabismo.backend.Usuario.Usuarios;
import com.google.rpc.context.AttributeContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    private  UsuariosRepository usuariosRepository;
    private  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private  JwtService jwtService;

    @Autowired
    private ConfirmationTokenRepository tokenRepository;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private EmailService emailService;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse Login(LoginRequest request) {
        //validar usuarios y password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getPassword()
                ));
        

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
    public AuthResponse confirmToken(String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Token inválido"));

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expirado");
        }

        Usuarios usuario = usuariosRepository.findById(confirmationToken.getUsuarioId())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        usuario.setEnabled(true);
        usuariosRepository.save(usuario);

        // 🚨 Ahora sí generamos JWT
        String jwt = jwtService.generateToken(usuario);

        return new AuthResponse(jwt, usuario.getrol().name(), usuario.getCorreo());
    }
    public String register(RegisterRequest request) {
        // 1. Crear usuario nuevo con enabled=false
        Usuarios usuario = new Usuarios();
        usuario.setNombres(request.getNombres());
        usuario.setApellidos(request.getApellidos());
        usuario.setEdad(request.getEdad());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword())); // cifrar
        usuario.setNumeroTele(request.getNumeroTele());
        usuario.setRol(request.getRol());
        usuario.setEnabled(true); // 🚨 Usuario aún no activado

        usuariosRepository.save(usuario);

        // 2. Generar token de confirmación (UUID)
        String confirmationToken = UUID.randomUUID().toString();

        ConfirmationToken tokenEntity = ConfirmationToken.builder()
                .token(confirmationToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24)) // expira en 24h
                .usuarioId(usuario.getId())
                .build();

        tokenRepository.save(tokenEntity);

        /* 3. Enviar correo con link de confirmación
        String link = "http://localhost:8080/api/auth/confirm?token=" + confirmationToken;
        emailService.enviarCorreo(
                usuario.getCorreo(),
                "Confirma tu cuenta",
                "Hola " + usuario.getNombres() + ",\n\nPor favor confirma tu cuenta haciendo clic en este enlace:\n" + link
        );*/ 

        // 4. Devolver mensaje de aviso (NO JWT todavía)
        return ResponseEntity.ok(tokenEntity.getToken()).getBody();
    }



}
