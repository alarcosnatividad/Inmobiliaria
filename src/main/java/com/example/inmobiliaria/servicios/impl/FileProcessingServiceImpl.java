package com.example.inmobiliaria.servicios.impl;

import com.example.inmobiliaria.servicios.FileProcessingService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service; // <--- ESTO ES LO QUE ARREGLA EL ERROR
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service // ¡Sin esto, Spring no encuentra el archivo!
public class FileProcessingServiceImpl implements FileProcessingService {

    // Nombre de la carpeta donde se guardarán las fotos
    private final Path rootPath = Paths.get("uploads");

    @Override
    public List<String> fileList() {
        try (Stream<Path> walk = Files.walk(rootPath, 1)) {
            return walk.filter(p -> !p.equals(rootPath))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public String uploadFile(MultipartFile archivo, String nombrePersonalizado) {
        try {
            // 1. Crear la carpeta 'uploads' si no existe
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            // 2. Generar un nombre único
            String nombreFinal = (nombrePersonalizado != null && !nombrePersonalizado.isEmpty())
                    ? nombrePersonalizado
                    : UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();

            // 3. Guardar el archivo
            Files.copy(archivo.getInputStream(), this.rootPath.resolve(nombreFinal));

            return nombreFinal;

        } catch (IOException e) {
            throw new RuntimeException("No se pudo subir el archivo: " + e.getMessage());
        }
    }

    @Override
    public Resource downloadFile(String fileName) {
        try {
            Path file = rootPath.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("No se puede leer el archivo: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
}