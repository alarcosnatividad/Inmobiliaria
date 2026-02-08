package com.example.inmobiliaria.servicios.impl;

import com.example.inmobiliaria.entidades.Rol;
import com.example.inmobiliaria.entidades.Usuario;
import com.example.inmobiliaria.repositorios.RolRepository;
import com.example.inmobiliaria.repositorios.UsuarioRepository;
import com.example.inmobiliaria.servicios.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. MÉTODO PARA REGISTRAR (Encripta clave y asigna rol)
    @Override
    public void registrar(Usuario usuario) {
        // Encriptamos la contraseña antes de guardarla (para que no se vea "1234" en la BD)
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asignamos el Rol "ROLE_USER" por defecto
        // Buscamos si existe el rol, y si no, lo creamos al vuelo
        Rol rolUser = rolRepository.findByNombre("ROLE_USER");
        if (rolUser == null) {
            rolUser = new Rol("ROLE_USER");
            rolRepository.save(rolUser);
        }

        usuario.setRoles(Collections.singletonList(rolUser));

        // Guardamos en la base de datos
        usuarioRepository.save(usuario);
    }

    // 2. MÉTODO PARA BUSCAR POR EMAIL (Necesario para el AuthController)
    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public  Usuario buscarPorId(Long id) {

        return usuarioRepository.findById(id).orElse(null);
    }




}
