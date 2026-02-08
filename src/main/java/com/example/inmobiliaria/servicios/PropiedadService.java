package com.example.inmobiliaria.servicios;

import com.example.inmobiliaria.dto.PropiedadDto;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface PropiedadService {

    // Método con 3 argumentos: DTO, Fotos y Email del dueño
    void guardarPropiedad(PropiedadDto propiedadDto, List<MultipartFile> archivos, String emailUsuario);

    List<PropiedadDto> obtenerTodas();
    List<PropiedadDto> obtenerPorCategoria(Long idCategoria);

    void eliminarPropiedad(Long id);
    PropiedadDto obtenerPorId(Long id);
}