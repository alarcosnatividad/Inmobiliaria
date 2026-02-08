package com.example.inmobiliaria.controladores;

import com.example.inmobiliaria.entidades.Categoria;
import com.example.inmobiliaria.entidades.Propiedad;
import com.example.inmobiliaria.repositorios.CategoriaRepository;
import com.example.inmobiliaria.repositorios.PropiedadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categorias")
public class ControladorAdmin {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PropiedadRepository propiedadRepository;

    // 1. MOSTRAR FORMULARIO (Nueva)
    @GetMapping("/nueva")
    public String mostrarFormulario(Model model) {
        model.addAttribute("categoria", new Categoria());
        model.addAttribute("listaCategorias", categoriaRepository.findAll());

        // NOVEDAD: Mandamos todas las casas para que puedas elegir
        model.addAttribute("todasLasPropiedades", propiedadRepository.findAll());

        return "categoria_form";
    }

    // 2. GUARDAR (La Magia)
    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria,
                                   @RequestParam(required = false) List<Long> idsPropiedadesSeleccionadas) { // Recibimos la lista de checkbox marcados

        // Primero guardamos la categoría para asegurarnos de que tiene ID
        Categoria categoriaGuardada = categoriaRepository.save(categoria);

        // Si has marcado alguna casa en la lista...
        if (idsPropiedadesSeleccionadas != null) {
            for (Long idPropiedad : idsPropiedadesSeleccionadas) {
                // Buscamos la casa
                Propiedad propiedad = propiedadRepository.findById(idPropiedad).orElse(null);
                if (propiedad != null) {
                    // Le asignamos esta nueva categoría
                    propiedad.setCategoria(categoriaGuardada);
                    propiedadRepository.save(propiedad);
                }
            }
        }

        return "redirect:/categorias/nueva";
    }

    // 3. EDITAR
    @GetMapping("/editar/{id}")
    public String editarCategoria(@PathVariable Long id, Model model) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);

        model.addAttribute("categoria", categoria);
        model.addAttribute("listaCategorias", categoriaRepository.findAll());

        // NOVEDAD: Mandamos también las casas aquí
        model.addAttribute("todasLasPropiedades", propiedadRepository.findAll());

        return "categoria_form";
    }

    // 4. BORRAR (El método seguro que hicimos antes)
    @GetMapping("/borrar/{id}")
    public String borrarCategoria(@PathVariable Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        if(categoria != null) {
            if (categoria.getPropiedades() != null) {
                for (Propiedad p : categoria.getPropiedades()) {
                    p.setCategoria(null);
                    propiedadRepository.save(p);
                }
            }
            categoriaRepository.deleteById(id);
        }
        return "redirect:/categorias/nueva";
    }
}