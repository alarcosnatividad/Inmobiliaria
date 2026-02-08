package com.example.inmobiliaria.iniciarbbdd;

import com.example.inmobiliaria.entidades.Categoria;
import com.example.inmobiliaria.repositorios.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InicializadorDatos implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {

        // Comprobamos si la tabla de categorías está vacía
        if (categoriaRepository.count() == 0) {

            // Si está vacía, creamos las categorías por defecto
            categoriaRepository.save(new Categoria("Pisos"));
            categoriaRepository.save(new Categoria("Chalets"));
            categoriaRepository.save(new Categoria("Áticos"));
            categoriaRepository.save(new Categoria("Garajes"));
            categoriaRepository.save(new Categoria("Oficinas"));

            System.out.println("✅ BASE DE DATOS: Categorías de prueba insertadas correctamente.");
        }
    }
}