package com.example.inmobiliaria.servicios.impl;

import com.example.inmobiliaria.dto.PropiedadDto;
import com.example.inmobiliaria.entidades.Categoria;
import com.example.inmobiliaria.entidades.Imagen;
import com.example.inmobiliaria.entidades.Propiedad;
import com.example.inmobiliaria.entidades.Usuario;
import com.example.inmobiliaria.repositorios.CategoriaRepository;
import com.example.inmobiliaria.repositorios.PropiedadRepository;
import com.example.inmobiliaria.servicios.FileProcessingService;
import com.example.inmobiliaria.servicios.PropiedadService;
import com.example.inmobiliaria.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropiedadServiceImpl implements PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private FileProcessingService fileProcessingService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void guardarPropiedad(PropiedadDto propiedadDto, List<MultipartFile> archivos, String emailUsuario) {
        Propiedad propiedad = new Propiedad();

        // Si estamos editando (viene con ID), cargamos la original para no perder datos
        if (propiedadDto.getId() != null) {
            propiedad = propiedadRepository.findById(propiedadDto.getId()).orElse(new Propiedad());
        }

        // 1. Datos básicos
        propiedad.setTitulo(propiedadDto.getTitulo());
        propiedad.setDescripcion(propiedadDto.getDescripcion());
        propiedad.setPrecio(propiedadDto.getPrecio());
        propiedad.setMetros(propiedadDto.getMetros());
        propiedad.setHabitaciones(propiedadDto.getHabitaciones());

        // Solo actualizamos fecha si es nueva
        if (propiedad.getId() == null) {
            propiedad.setFechaPublicacion(new Date());
        }

        // 2. ASIGNAR DUEÑO
        // Solo si es nueva (para no cambiar de dueño al editar)
        if (propiedad.getAutor() == null) {
            Usuario autor = usuarioService.buscarPorEmail(emailUsuario);
            propiedad.setAutor(autor);
        }

        // 3. ASIGNAR CATEGORÍA (¡CORREGIDO: ESTO VA FUERA DE LAS FOTOS!)
        if (propiedadDto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(propiedadDto.getCategoriaId()).orElse(null);
            propiedad.setCategoria(categoria);
        }

        // 4. LÓGICA DE FOTOS
        if (archivos != null && !archivos.isEmpty()) {
            for (MultipartFile archivo : archivos) {
                if (archivo != null && !archivo.isEmpty()) {
                    // a) Subir al disco
                    String nombreArchivo = fileProcessingService.uploadFile(archivo, null);

                    // b) Crear entidad Imagen
                    Imagen nuevaImagen = new Imagen();
                    nuevaImagen.setNombreArchivo(nombreArchivo);

                    // c) Vincular a la propiedad
                    propiedad.agregarImagen(nuevaImagen);
                }
            }
        }

        // 5. Guardar en BD
        propiedadRepository.save(propiedad);
    }

    @Override
    public List<PropiedadDto> obtenerTodas() {
        List<Propiedad> listaEntidades = propiedadRepository.findAll();

        // Usamos el método auxiliar para convertir cada una
        return listaEntidades.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PropiedadDto> obtenerPorCategoria(Long idCategoria) {
        // 1. Buscamos en la BD filtrando por categoría
        List<Propiedad> listaEntidad = propiedadRepository.findByCategoriaId(idCategoria);

        // 2. Convertimos a DTO usando el mismo método auxiliar
        return listaEntidad.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    // =======================================================
    // MÉTODO AUXILIAR PRIVADO (PARA NO REPETIR CÓDIGO)
    // =======================================================
    private PropiedadDto convertirADto(Propiedad p) {
        PropiedadDto dto = new PropiedadDto();
        dto.setId(p.getId());
        dto.setTitulo(p.getTitulo());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        dto.setMetros(p.getMetros());
        dto.setHabitaciones(p.getHabitaciones());

        // Mapear Categoría al DTO
        if (p.getCategoria() != null) {
            dto.setCategoriaId(p.getCategoria().getId());
        }

        // Mapear Fotos al DTO
        if (p.getImagenes() != null && !p.getImagenes().isEmpty()) {
            List<String> nombres = p.getImagenes().stream()
                    .map(Imagen::getNombreArchivo)
                    .collect(Collectors.toList());
            dto.setNombresImagenes(nombres);
        }

        return dto;
    }

    @Override
    public void eliminarPropiedad(Long id) {
        propiedadRepository.deleteById(id);
    }
    @Override
    public PropiedadDto obtenerPorId(Long id) {
        // 1. Buscamos la entidad en la base de datos
        Propiedad propiedad = propiedadRepository.findById(id).orElse(null);

        // 2. Si no existe, devolvemos nulo
        if (propiedad == null) {
            return null;
        }

        // 3. Convertimos la Entidad a DTO para que el formulario la entienda
        return convertirADto(propiedad);
    }
}