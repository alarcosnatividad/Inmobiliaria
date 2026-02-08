package com.example.inmobiliaria.entidades;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreRemitente;
    private String telefono;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "propiedad_id")
    private Propiedad propiedad;

    public Mensaje() {
        this.fecha = new Date();
    }

    public Mensaje(String nombreRemitente, String telefono, String texto, Propiedad propiedad) {
        this.nombreRemitente = nombreRemitente;
        this.telefono = telefono;
        this.texto = texto;
        this.propiedad = propiedad;
        this.fecha = new Date();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreRemitente() { return nombreRemitente; }
    public void setNombreRemitente(String nombreRemitente) { this.nombreRemitente = nombreRemitente; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
    public Propiedad getPropiedad() { return propiedad; }
    public void setPropiedad(Propiedad propiedad) { this.propiedad = propiedad; }
}