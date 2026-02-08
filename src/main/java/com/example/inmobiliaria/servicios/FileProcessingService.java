package com.example.inmobiliaria.servicios;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileProcessingService {
    // Definimos qué se puede hacer, pero no "cómo" se hace
    List<String> fileList();
    String uploadFile(MultipartFile multipartFile, String fileName);
    Resource downloadFile(String fileName);
}
