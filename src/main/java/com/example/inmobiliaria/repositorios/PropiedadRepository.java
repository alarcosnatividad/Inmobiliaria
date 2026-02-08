package com.example.inmobiliaria.repositorios;

import com.example.inmobiliaria.entidades.Categoria;
import com.example.inmobiliaria.entidades.Propiedad;
import com.example.inmobiliaria.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    // Método mágico: Spring crea la consulta SQL solo por el nombre
    // como nos devuelve un conjunto ..mas de uno por eso crea una lista
    // dame una lista de la clase propiedad busca por autor de la clase usuario , el atributo autor..
    List<Propiedad> findByAutor(Usuario autor);
    // dame una lista de la clase propiedad, encuentra por orden de fechaPublicacion descendente
    List<Propiedad> findAllByOrderByFechaPublicacionDesc();
    //
    List<Propiedad> findByCategoriaId(Long id );

    List<Propiedad> findByCategoria(Categoria categoria );


}