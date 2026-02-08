package com.example.inmobiliaria.entidades;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Categoria {

    // genero el id que siempre es igual
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // -------------------pongo los atributos, nombre ----------------------------------------
    private String nombre; // (chalet,pisos,apartamentos)

    // Relación inversa: Una categoría tiene muchas propiedades 1-N
    // (Esto es opcional, pero útil para saber cuántas casas hay en una categoría)
    // además le digo que esta relación está controlada por propiedad
    @OneToMany(mappedBy = "categoria")

    // atributo que es una lista de la clase propiedad a la que llamo propiedades
    private List<Propiedad> propiedades;

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public List<Propiedad> getPropiedades() { return propiedades; }
    public void setPropiedades(List<Propiedad> propiedades) { this.propiedades = propiedades; }
}