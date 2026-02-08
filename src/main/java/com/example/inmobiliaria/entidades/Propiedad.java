package com.example.inmobiliaria.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Propiedad {
    // el id siempre igual
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // atributos que vamos a tener: titulo,descripcion,precio,imagenUrl,metros,habitaciones,banios,tieneGarage,tieneTerraza
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private Double precio;
    private String imagenUrl;

    // Campos extra
    private Integer metros;
    private Integer habitaciones;
    private Integer banios;
    private Boolean tieneGaraje;
    private Boolean tieneTerraza;


    //-----------------RELACIONES--------------------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "categoria_id") // Esto creará la columna en la base de datos

    private Categoria categoria;

    @Temporal(TemporalType.DATE)
    private Date fechaPublicacion;

    @ManyToOne// muchos casas pertenecen a un usuario
    @JoinColumn(name = "usuario_id") // con esto en la bbdd en la tabla propiedad se crea una columna usuario_id
    private Usuario autor; // la variable es la que se pone en Usuario(autor)  @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)

    // una propiedad tiene muchas imagenes, propiedad no es la dueña de la relacion, si se borra la propiead borra las imagenes
    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL) // en imagen tengo esto ManytoOne
    // creo atributo que es una lista de imagenes
    private List<Imagen> imagenes = new ArrayList<>();
    // -------------------------------------------------------

    public Propiedad() {
        this.fechaPublicacion = new Date();
    }

    // --- Y ESTE ES EL MÉTODO QUE BUSCA TU SERVICIO ---
    public void agregarImagen(Imagen imagen) {
        imagenes.add(imagen);
        imagen.setPropiedad(this); // Esto vincula la foto a esta casa
    }
    // ------------------------------------------------

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public Integer getMetros() { return metros; }
    public void setMetros(Integer metros) { this.metros = metros; }
    public Integer getHabitaciones() { return habitaciones; }
    public void setHabitaciones(Integer habitaciones) { this.habitaciones = habitaciones; }
    public Integer getBanios() { return banios; }
    public void setBanios(Integer banios) { this.banios = banios; }
    public Boolean getTieneGaraje() { return tieneGaraje; }
    public void setTieneGaraje(Boolean tieneGaraje) { this.tieneGaraje = tieneGaraje; }
    public Boolean getTieneTerraza() { return tieneTerraza; }
    public void setTieneTerraza(Boolean tieneTerraza) { this.tieneTerraza = tieneTerraza; }
    public Date getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(Date fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public List<Imagen> getImagenes() { return imagenes; }
    public void setImagenes(List<Imagen> imagenes) { this.imagenes = imagenes; }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}