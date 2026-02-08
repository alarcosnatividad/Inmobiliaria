package com.example.inmobiliaria.controladores;

import com.example.inmobiliaria.entidades.Usuario;
import com.example.inmobiliaria.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice // <--- Esto indica que afecta a TODA la web
public class VariablesGlobales {

    @Autowired
    private UsuarioService usuarioService;

    // Este método se ejecuta antes de cualquier controlador
    // y mete el usuario logueado en la mochila con el nombre "usuarioLogueado"
    @ModelAttribute("usuarioLogueado")
    public Usuario agregarUsuarioGlobal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si hay alguien conectado y no es un usuario anónimo...
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            // Buscamos sus datos reales en la BBDD (aquí sí tenemos el ID)
            return usuarioService.buscarPorEmail(auth.getName());
        }

        return null; // Si no está logueado, mandamos null
    }
}
