package com.Deteccion_estrabismo.backend.Service;

import com.Deteccion_estrabismo.backend.Dto.*;
import com.Deteccion_estrabismo.backend.Entities.*;
import com.Deteccion_estrabismo.backend.Repository.ConfirmationTokenRepository;
import com.Deteccion_estrabismo.backend.Repository.UsuariosRepository;
import com.Deteccion_estrabismo.backend.Repository.MedicoRepository;
import com.Deteccion_estrabismo.backend.Repository.PacientesRepository;
import com.Deteccion_estrabismo.backend.Repository.ResponsableRepository;
import com.Deteccion_estrabismo.backend.Repository.AdministradorRepository;
import com.Deteccion_estrabismo.backend.util.BuildObjectMapper;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class AuthService {
    private UsuariosRepository usuariosRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private JwtService jwtService;
    private final BuildObjectMapper mapper;
    private final PacientesRepository pacientesRepository;
    private final AdministradorRepository administradorRepository;
    private final MedicoRepository medicoRepository;
    private final ResponsableRepository responsableRepository;
    private ConfirmationTokenRepository tokenRepository;
    private AuthenticationManager authenticationManager;
    private EmailService emailService;

    public AuthService(AdministradorRepository administradorRepository, BuildObjectMapper mapper,
            PacientesRepository pacientesRepository, MedicoRepository medicoRepository,
            ResponsableRepository responsableRepository, AuthenticationManager authenticationManager,
            EmailService emailService, JwtService jwtService,
            ConfirmationTokenRepository tokenRepository, UsuariosRepository usuariosRepository) {
        this.administradorRepository = administradorRepository;
        this.mapper = mapper;
        this.pacientesRepository = pacientesRepository;
        this.medicoRepository = medicoRepository;
        this.responsableRepository = responsableRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.usuariosRepository = usuariosRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthResponse Login(LoginRequest request) {
        try {
            // validar usuarios y password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(),
                            request.getPassword()));

            // buscar usuario
            Usuarios usuarios = usuariosRepository.findByCorreo(request.getCorreo())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // generacion de token
            String token = jwtService.generateToken(usuarios);

            // Log de depuración
            log.info("Usuario logueado: {} con rol: {} => Token generado: {}",
                    usuarios.getCorreo(), usuarios.getRol(), token);

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

        Usuarios usuario = usuariosRepository.findById(Long.valueOf(confirmationToken.getUsuarioId()))
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        usuario.setEnabled(true);
        usuariosRepository.save(usuario);

        // 🚨 Ahora sí generamos JWT
        String jwt = jwtService.generateToken(usuario);

        return new AuthResponse(jwt, null);
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        try {
            // Validar correo único
            if (usuariosRepository.findByCorreo(request.getCorreo()).isPresent()) {
                return RegisterResponse.error("El correo ya está registrado");
            }

            Usuarios usuario;

            switch (request.getRol()) {
                case PACIENTE -> usuario = crearPaciente((RegisterPacienteRequest) request);
                case MEDICO -> usuario = crearMedico((RegisterMedicoRequest) request);
                case RESPONSABLE -> usuario = crearResponsable((RegisterResponsableRequest) request);
                case ADMIN -> usuario = crearAdministrador((RegisterAdminRequest) request);
                default -> throw new IllegalArgumentException("Rol no válido");
            }

            // Generar y enviar token
            enviarTokenConfirmacion(usuario);

            return RegisterResponse.success();

        } catch (Exception e) {
            log.error("Error en registro: {}", e.getMessage());
            return RegisterResponse.error(e.getMessage());
        }
    }

    private Pacientes crearPaciente(RegisterPacienteRequest request) {
        Pacientes paciente = mapper.converterTo(request, Pacientes.class);
        paciente.setPassword(passwordEncoder.encode(request.getPassword()));
        paciente.setRol(request.getRol());
        paciente.setEnabled(false);

        if (request.getResponsableId() != null) {
            Responsable responsable = responsableRepository.findById(request.getResponsableId())
                    .orElseThrow(() -> new RuntimeException("Responsable no encontrado"));
            paciente.setResponsable(responsable);
        }

        return pacientesRepository.save(paciente);
    }

    private Medico crearMedico(RegisterMedicoRequest request) {
        Medico medico = mapper.converterTo(request, Medico.class);
        medico.setPassword(passwordEncoder.encode(request.getPassword()));
        medico.setRol(request.getRol());
        medico.setEnabled(false);
        return medicoRepository.save(medico);
    }

    private Responsable crearResponsable(RegisterResponsableRequest request) {
        Responsable responsable = mapper.converterTo(request, Responsable.class);
        responsable.setPassword(passwordEncoder.encode(request.getPassword()));
        responsable.setRol(request.getRol());
        responsable.setEnabled(false);
        return responsableRepository.save(responsable);
    }

    private Administrador crearAdministrador(RegisterAdminRequest request) {
        Administrador administrador = mapper.converterTo(request, Administrador.class);
        administrador.setPassword(passwordEncoder.encode(request.getPassword()));
        administrador.setRol(request.getRol());
        administrador.setEnabled(false);
        return administradorRepository.save(administrador);
    }

    private void copiarPropiedadesBase(Usuarios source, Usuarios target) {
        target.setId(source.getId());
        target.setNombres(source.getNombres());
        target.setApellidos(source.getApellidos());
        target.setEdad(source.getEdad());
        target.setCorreo(source.getCorreo());
        target.setPassword(source.getPassword());
        target.setNumeroTele(source.getNumeroTele());
        target.setRol(source.getRol());
        target.setEnabled(source.isEnabled());
    }

    private void enviarTokenConfirmacion(Usuarios usuario) {
        String confirmationToken = UUID.randomUUID().toString();

        ConfirmationToken tokenEntity = ConfirmationToken.builder()
                .token(confirmationToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusHours(24))
                .usuarioId(String.valueOf(usuario.getId()))
                .build();

        tokenRepository.save(tokenEntity);

        String link = "http://localhost:8080/auth/confirm?token=" + confirmationToken;

        emailService.enviarCorreo(
                usuario.getCorreo(),
                "Confirma tu cuenta en Detecteye - " + usuario.getRol(),
                construirMensajeEmail(usuario, link));
    }

    private String construirMensajeEmail(Usuarios usuario, String link) {
        return switch (usuario.getRol()) {
            case MEDICO -> "Bienvenido Dr./Dra. " + usuario.getNombres() +
                    ",\n\nConfirma tu cuenta para comenzar a evaluar pacientes:\n" + link;
            case RESPONSABLE -> "Bienvenido/a " + usuario.getNombres() +
                    ",\n\nConfirma tu cuenta para gestionar pacientes:\n" + link;
            case ADMIN -> "Bienvenido Administrador " + usuario.getNombres() +
                    ",\n\nConfirma tu cuenta para acceder al sistema:\n" + link;
            default -> "Bienvenido " + usuario.getNombres() +
                    ",\n\nConfirma tu cuenta:\n" + link;
        } + "\n\nEl enlace expirará en 24 horas";
    }

    public RegisterResponse UpdatePaciente(String correo, UpdateRequest request) {
        try {
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

        } catch (Exception e) {
            return RegisterResponse.builder()
                    .success(false)
                    .error(e.getMessage())
                    .build();
        }
    }

}
