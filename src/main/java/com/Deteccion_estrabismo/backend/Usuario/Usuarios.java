package com.Deteccion_estrabismo.backend.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Usuarios") // nombre de la coleccion de mongodb
@Data
public class Usuarios {
    @Id
    private String id; //  Mongo crea  un ObjectID automaticamente

    private String nombres;
    private String apellidos;
    private Integer edad;
    private String correo;
    private String password; // se cifra con Bcrypt
    private String numeroTele;
    private Rol rol; // Pacientes, medicos o administradores
    private boolean enabled; //  para activar/desactivar usuario


    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getCorreo() {
        return correo;
    }


    public String getPassword() {
        return password;
    }


    public String getNumeroTele() {
        return numeroTele;
    }

    public Rol getrol() {
        return rol;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNumeroTele(String numeroTele) {
        this.numeroTele = numeroTele;
    }

    public void setRol(String rol) {
        this.rol = Rol.valueOf(rol);
    }

    public String getUsername() {
       return correo;
    }
}
