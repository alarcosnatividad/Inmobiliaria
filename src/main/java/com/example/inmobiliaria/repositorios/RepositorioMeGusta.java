package com.example.inmobiliaria.repositorios;

import com.example.inmobiliaria.entidades.MeGusta;
import com.example.inmobiliaria.entidades.Propiedad;
import com.example.inmobiliaria.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



    @Repository
    public interface RepositorioMeGusta extends JpaRepository<MeGusta, Long> {

        // 1. Contar likes de una propiedad
        int countByPropiedad(Propiedad propiedad);

        // 2. Buscar si un usuario ya dio like a una propiedad
        List<MeGusta> findByPropiedadAndUsuario(Propiedad propiedad, Usuario usuario);

        // --- NUEVO: Buscar todos los likes de un usuario ---
        List<MeGusta> findByUsuario(Usuario usuario);
    }

