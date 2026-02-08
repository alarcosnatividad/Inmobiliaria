package com.example.inmobiliaria.controladores;

import com.example.inmobiliaria.dto.PropiedadDto;
import com.example.inmobiliaria.repositorios.CategoriaRepository;
import com.example.inmobiliaria.servicios.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/crud") // Todo lo de aquí empieza por /crud
public class ControladorCRUD {

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. LISTA DE GESTIÓN (Tabla privada)
    @GetMapping("/propiedades")
    public String listar(Model model) {
        model.addAttribute("listaPropiedades", propiedadService.obtenerTodas());
        return "listaPropiedades";
    }

    // 2. FORMULARIO PARA CREAR
    @GetMapping("/propiedades/insertar")
    public String mostrarFormulario(Model model) {
        model.addAttribute("propiedadDto", new PropiedadDto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "formularioPropiedad";
    }

    // 3. GUARDAR LOS DATOS
    @PostMapping("/propiedades/guardar")
    public String guardar(@ModelAttribute PropiedadDto propiedadDto,
                          @RequestParam("archivosImagen") List<MultipartFile> files,
                          Authentication authentication) {
        String emailUsuario = authentication.getName();
        propiedadService.guardarPropiedad(propiedadDto, files, emailUsuario);
        return "redirect:/crud/propiedades";
    }

    // 4. ELIMINAR PROPIEDAD
    @GetMapping("/propiedades/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        // Llamamos al servicio para que borre
        propiedadService.eliminarPropiedad(id);

        // Al terminar, volvemos a cargar la lista
        return "redirect:/crud/propiedades";
    }
    // 5. MOSTRAR FORMULARIO DE EDICIÓN
    @GetMapping("/propiedades/modificar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        PropiedadDto dto = propiedadService.obtenerPorId(id);

        // Cargamos los datos de la casa en el modelo
        model.addAttribute("propiedadDto", dto);

        // Cargamos también las categorías para el desplegable
        model.addAttribute("categorias", categoriaRepository.findAll());

        // Reutilizamos el MISMO formulario de crear (ahora saldrá relleno)
        return "formularioPropiedad";
    }
}