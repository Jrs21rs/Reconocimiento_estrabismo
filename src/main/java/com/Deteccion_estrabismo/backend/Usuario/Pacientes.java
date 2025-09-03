package com.Deteccion_estrabismo.backend.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Pacientes") // nombre de la coleccion de mongodb
@Data
public class Pacientes {
    @Id
    private Integer id; //  Mongo crea  un ObjectID automaticamente

    private String nombres;
    private String apellidos;
    private Integer edad;
    private String correo;
    private String password; // se cifra con Bcrypt
    private String numeroTele;
    private String rol;


}
