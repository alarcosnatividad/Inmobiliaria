package com.example.inmobiliaria.entidades;

import jakarta.persistence.*;

@Entity
public class MeGusta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Quien da el like

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad; // A qué casa le da like

    // Constructores
    public MeGusta() {}

    public MeGusta(Usuario usuario, Propiedad propiedad) {
        this.usuario = usuario;
        this.propiedad = propiedad;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
}