package com.Deteccion_estrabismo.backend.Service;

import com.Deteccion_estrabismo.backend.Repository.UsuariosRepository;
import com.Deteccion_estrabismo.backend.Usuario.Usuarios;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuariosService {
    @Autowired
    private UsuariosRepository usuariosRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuarios registrarUsuarios(Usuarios usuarios){
        usuarios.setPassword(passwordEncoder.encode(usuarios.getPassword())); //Cifra la contraseña que se ingrese
        return usuariosRepository.save(usuarios);

    }
}
