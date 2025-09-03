package com.Deteccion_estrabismo.backend.Conifg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())//Desactivar  CSRF para pruebas con POSTMAN
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/Pacientes/registro").permitAll() // publico
                        .anyRequest().authenticated()// el resto pide login
                )
                .formLogin(login -> login.disable())//quitar el login por formulario
                .httpBasic(httpBasic -> httpBasic.disable());// Quitar el basic  Auth mientras se usa
        return http.build();
    }
}
