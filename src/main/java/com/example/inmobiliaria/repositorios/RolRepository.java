package com.example.inmobiliaria.repositorios;

import com.example.inmobiliaria.entidades.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Este método lo usamos para buscar el rol "ROLE_USER"
    Rol findByNombre(String nombre);
}
