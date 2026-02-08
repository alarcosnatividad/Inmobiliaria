package com.example.inmobiliaria.controladores;

import com.example.inmobiliaria.entidades.Usuario;
import com.example.inmobiliaria.servicios.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private UsuarioService usuarioService;

    // Inyección por constructor (Estilo Profe: Más limpio que @Autowired)
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 1. Pantalla de Login
    @GetMapping("/auth/login")
    public String loginForm() {
        return "login";
    }

    // 2. Pantalla de Registro (GET)
    @GetMapping("/auth/registro")
    public String showRegistrationForm(Model model){
        // Creamos un usuario vacío para el formulario
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        return "registro";
    }

    // 3. Procesar Registro (POST)
    @PostMapping("/auth/registro/guardar")
    public String registration(@ModelAttribute("usuario") Usuario usuario,
                               BindingResult result,
                               Model model){

        // VALIDACIÓN: Comprobamos si el email ya existe
        Usuario existing = usuarioService.buscarPorEmail(usuario.getEmail());

        if (existing != null && existing.getEmail() != null && !existing.getEmail().isEmpty()) {
            // Si existe, mandamos un error al formulario
            result.rejectValue("email", null, "Ya existe una cuenta registrada con ese email");
        }

        // Si hay errores (ej: email repetido), volvemos al formulario
        if (result.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "registro";
        }

        // Si todo está bien, guardamos
        usuarioService.registrar(usuario);
        return "redirect:/auth/login?registrado=true";
    }
}