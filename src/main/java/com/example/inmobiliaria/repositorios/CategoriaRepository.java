package com.example.inmobiliaria.repositorios;

import com.example.inmobiliaria.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Esto ya nos da findAll(), save(), etc. gratis.
}