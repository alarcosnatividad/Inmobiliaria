package com.example.inmobiliaria.entidades;

import jakarta.persistence.*;

@Entity
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Aquí guardaremos "ROLE_ADMIN" o "ROLE_USER"

    // --- CONSTRUCTORES ---
    public Rol() {
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    // --- GETTERS Y SETTERS (Genéralos con Alt+Insert si prefieres, o copia estos) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}