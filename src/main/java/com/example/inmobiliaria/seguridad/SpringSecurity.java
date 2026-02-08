package com.example.inmobiliaria.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // 1. RECURSOS ESTÁTICOS (Siempre permitidos: estilos, imágenes, subidas...)
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/propiedades/uploads/**", "/favicon.ico", "/webjars/**").permitAll()

                        // 2. RUTAS PÚBLICAS (Login, Registro, Inicio, Ver listado de casas)
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/", "/propiedades", "/propiedad/**").permitAll()

                        // 3. ZONA VIP (ADMINISTRADOR) 👮‍♂️
                        // Esta línea protege todo lo que empiece por /categorias/
                        // Funciona tanto si en tu BD el rol es "ADMIN" como "ROLE_ADMIN"
                        .requestMatchers("/categorias/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        // 4. RESTO DE RUTAS PRIVADAS (Cualquier usuario logueado puede entrar)
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}