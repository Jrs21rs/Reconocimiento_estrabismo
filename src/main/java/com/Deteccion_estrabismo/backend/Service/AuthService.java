package com.Deteccion_estrabismo.backend.Service;

import com.Deteccion_estrabismo.backend.Dto.*;
import com.Deteccion_estrabismo.backend.Repository.ConfirmationTokenRepository;
import com.Deteccion_estrabismo.backend.Repository.UsuariosRepository;
import com.Deteccion_estrabismo.backend.Entities.ConfirmationToken;
import com.Deteccion_estrabismo.backend.Entities.Rol;
import com.Deteccion_estrabismo.backend.Entities.Usuarios;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        try {
            //validar usuarios y password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getPassword()
                    ));

            //buscar usuario en mongo
            Usuarios usuarios = usuariosRepository.findByCorreo(request.getCorreo())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            //generacion de token
            String token = jwtService.generateToken(usuarios);

            // Log de depuración
            log.info("Usuario logueado: {} con rol: {} => Token generado: {}",
                    usuarios.getCorreo(), usuarios.getrol(), token);

            return AuthResponse.builder()
                    .token(token)
                    .error(null)
                    .build();

        } catch (Exception e) {
            return AuthResponse.builder()
                    .token(null)
                    .error(e.getMessage())
                    .build();
        }
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

        return new AuthResponse(jwt, null);
    }
    public RegisterResponse register(RegisterRequest request) {
        Usuarios usuario = new Usuarios();

        try {
            if(usuariosRepository.findByCorreo(usuario.getCorreo()).isPresent()){
                return RegisterResponse.builder()
                        .success(false)
                        .error("El correo ya está registrado")
                        .build();

            }else{  // 1. Crear usuario nuevo con enabled=false
                usuario.setNombres(request.getNombres());
                usuario.setApellidos(request.getApellidos());
                usuario.setEdad(request.getEdad());
                usuario.setCorreo(request.getCorreo());
                usuario.setPassword(passwordEncoder.encode(request.getPassword())); // cifrar
                usuario.setNumeroTele(request.getNumeroTele());
                usuario.setRol(Rol.PACIENTE);

                usuario.setEnabled(false);

                Usuarios usuarioGuardado = usuariosRepository.save(usuario);

                // 2. Generar token de confirmación (UUID)
                String confirmationToken = UUID.randomUUID().toString();

                ConfirmationToken tokenEntity = ConfirmationToken.builder()
                        .token(confirmationToken)
                        .createdAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusHours(24))
                        .usuarioId(usuarioGuardado.getId())
                        .build();

                tokenRepository.save(tokenEntity);
                //3. Contruir el link de confirmacion
                String link = "http://localhost:8080/auth/confirm?token=" + confirmationToken;

                //4. enviar correo
                emailService.enviarCorreo(
                        usuario.getCorreo(),
                        "Confirma tu cuenta en Detecteye",
                        "Bienvenido " + usuario.getNombres() +
                                ",\n\n DetectEye requiere que confirmes que eres tu para que sea mas seguro para ti " +
                                "por favor confirma tu cuenta en el siguiente enlace \n"+ link +"\n\n El enlace expirara en 24 horas"
                );


                return RegisterResponse.builder()
                        .success(true)
                        .error(null)
                        .build();}



        } catch (Exception e) {
            return RegisterResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }

    public RegisterResponse UpdatePaciente(String correo, UpdateRequest request){
    try{
        Usuarios usuario = (usuariosRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException(("usuario no encontrado"))));
        // Actualizar campos si no son nulos
        // Actualizar campos si son válidos
        if (request.getNombres() != null && !request.getNombres().isBlank()) {
            usuario.setNombres(request.getNombres());
        }
        if (request.getApellidos() != null && !request.getApellidos().isBlank()) {
            usuario.setApellidos(request.getApellidos());
        }
        if (request.getEdad() != null && request.getEdad() > 0) {
            usuario.setEdad(request.getEdad());
        }
        if (request.getNumeroTele() != null && !request.getNumeroTele().isBlank()) {
            usuario.setNumeroTele(request.getNumeroTele());
        }

        usuariosRepository.save(usuario);
        return RegisterResponse.builder()
                .success(true)
                .error(null)
                .build();

    }catch(Exception e){
        return RegisterResponse.builder()
                .success(false)
                .error(e.getMessage())
                .build();
        }
    }



}
