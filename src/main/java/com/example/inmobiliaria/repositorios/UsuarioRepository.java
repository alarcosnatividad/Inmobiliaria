
package com.example.inmobiliaria.repositorios;

import com.example.inmobiliaria.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Este método es el que usa el servicio para buscar si el email ya existe
    // el servicio vendrá y le dirá al repositorio que encuentre por emaeil.. para esto tengo que tener el metodo creado
    Usuario findByEmail(String email);
}