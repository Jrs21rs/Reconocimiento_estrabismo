package com.Deteccion_estrabismo.backend.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;



@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityScan
@Table(name = "Usuarios") // nombre de la tabla en la base de datos
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto-increment 
    private String id; //  Mongo crea  un ObjectID automaticamente

    @Column(nullable = false, length = 100)
    private String nombres;
    @Column(nullable = false, length = 100)
    private String apellidos;
    @Column(nullable = false)
    private Integer edad;
    @Column(nullable = false, unique = true, length = 150)
    private String correo;
    @Column(nullable = false)
    private String password; // se cifra con Bcrypt
    @Column(nullable = false, length = 15)
    private String numeroTele;
     @Enumerated(EnumType.STRING) // para guardar el rol como texto
    @Column(nullable = false, length = 50)
    private Rol rol; // Pacientes, medicos o administradores
    private boolean enabled;//  para activar/desactivar




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

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getUsername() {
       return this.correo;
    }
}
