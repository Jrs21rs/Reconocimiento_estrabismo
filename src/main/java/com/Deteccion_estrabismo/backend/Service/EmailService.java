package com.Deteccion_estrabismo.backend.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

import javax.sound.midi.MidiMessage;

@Service
@Data
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreo(String to, String subject, String body){
        try{
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper messageHelper=new MimeMessageHelper(message, true);

            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body,false);

            mailSender.send(message);

            System.out.println("correo Enviado");
        } catch(MessagingException e){
            throw new RuntimeException("error al enviar el correo:" + e.getMessage(), e);
        }

       /* SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(to);
        mensaje.setSubject(subject);
        mensaje.setText(body);
        mailSender.send(mensaje);*/
    }
}
