package com.example.inmobiliaria.dto;

import com.example.inmobiliaria.entidades.Usuario;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;
import java.util.List;

public class PropiedadDto {

    // CAMBIO IMPORTANTE: 'Long' con mayúscula para permitir nulos
    private Long id;

    private String titulo;
    private String descripcion;
    private Double precio;
    private int metros;
    private int habitaciones;

    // IMPORTANTE: Esta lista recibe los archivos reales desde el HTML
    private List<MultipartFile> archivosImagen;

    // Esta lista sirve para enviar los nombres (texto) a la vista para mostrarlos
    private List<String> nombresImagenes;

    private Date fechaPublicacion;
    private Usuario autor;

    // Lógica para los Likes / Favoritos
    private int cantidadFavoritos;
    private String esFavoritoDelUsuario = "\uD83E\uDD0D"; // Corazón blanco por defecto

    private Long categoriaId; // Aquí guardaremos el ID (ej: 1 para Pisos)

    public PropiedadDto() {
    }

    // --- GETTERS Y SETTERS ---

    // CAMBIO: Devuelve Long
    public Long getId() {
        return id;
    }

    // CAMBIO: Recibe Long
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMetros() {
        return metros;
    }

    public void setMetros(int metros) {
        this.metros = metros;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(int habitaciones) {
        this.habitaciones = habitaciones;
    }

    public List<MultipartFile> getArchivosImagen() {
        return archivosImagen;
    }

    public void setArchivosImagen(List<MultipartFile> archivosImagen) {
        this.archivosImagen = archivosImagen;
    }

    public List<String> getNombresImagenes() {
        return nombresImagenes;
    }

    public void setNombresImagenes(List<String> nombresImagenes) {
        this.nombresImagenes = nombresImagenes;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public int getCantidadFavoritos() {
        return cantidadFavoritos;
    }

    public void setCantidadFavoritos(int cantidadFavoritos) {
        this.cantidadFavoritos = cantidadFavoritos;
    }

    public String getEsFavoritoDelUsuario() {
        return esFavoritoDelUsuario;
    }

    public void setEsFavoritoDelUsuario(String esFavoritoDelUsuario) {
        this.esFavoritoDelUsuario = esFavoritoDelUsuario;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }
}