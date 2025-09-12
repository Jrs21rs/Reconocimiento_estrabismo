package com.Deteccion_estrabismo.backend.Service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

@Service
@Data
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreo(String to, String subject, String body){

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(to);
        mensaje.setSubject(subject);
        mensaje.setText(body);
        mailSender.send(mensaje);
    }
}
