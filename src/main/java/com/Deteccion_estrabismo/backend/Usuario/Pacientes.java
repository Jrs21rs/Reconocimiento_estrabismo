package com.Deteccion_estrabismo.backend.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Pacientes") // nombre de la coleccion de mongodb
public class Pacientes {
    @Id
    private String id; //  Mongo crea  un ObjectID automaticamente

    private String nombres;
    private String apellidos;
    private Integer edad;
    private String correo;
    private String password; // se cifra con Bcrypt
    private String numeroTele;
    private String rol;
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

    public String getRol() {
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
        this.rol = rol;
    }

}
