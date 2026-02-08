package com.example.inmobiliaria.entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // esto es para evitar conflictos con la BBDD
public class Usuario {

    private static final long serialVersionUID = 1L;// para serializar

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)// aquí le estoy diciendo que esa columna no puede nula,tiene que haber un nombre
    private String name;

    @Column(nullable=false, unique=true)// aquí le estoy diciendo que la columna no puede ser nula, tiene que haber un email y además único, no se puede repetir
    private String email;

    @Column(nullable=false) // contraseña no puede ser nula
    private String password;

    private String avatar;

    // --- RELACIONES IMPORTANTES ---

    // 1. ROLES
    // Un usuario puede tener roles (ADMIN, USER)
    // Necesitas tener creada la clase Rol
    //Spring CREA AUTOMÁTICAMENTE en tu base de datos una tabla llamada usuarios_roles (une los dos nombres).
    //En esa tabla mete dos columnas: usuario_id y rol_id.
    @ManyToMany(fetch = FetchType.EAGER)//el ansioso trae al usuario y de forma automatica el rol

    // atributo que es la lista de la clase Rol, al que llamo roles
    private List<Rol> roles = new ArrayList<>();

    // 2. PROPIEDADES (Requisito: "una lista de casas por usuario")
    // Un usuario puede publicar muchas casas
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)// autor es la propiedad que se define en propriedad

    // una lista de la clase propiedad llamada propiedades es la que he puesto en la relacion
    private List<Propiedad> propiedades = new ArrayList<>();

    // ---------------------------------------------------------
    // AHORA GENERA AQUÍ DEBAJO EL CÓDIGO AUTOMÁTICO
    // ---------------------------------------------------------

    // 1. Constructor Vacío (OBLIGATORIO)
    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public List<Propiedad> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(List<Propiedad> propiedades) {
        this.propiedades = propiedades;
    }
    // 2. Getters y Setters (Géralos con Alt+Insert)
    // ... aquí irán todas las líneas que genera IntelliJ ...
}