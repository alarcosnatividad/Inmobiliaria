package com.example.inmobiliaria.servicios;

import com.example.inmobiliaria.entidades.Usuario;

public interface UsuarioService {

    // 1. Método para guardar usuarios nuevos (ESTE ES EL QUE TE FALTA)
    void registrar(Usuario usuario);

    // 2. Método para buscar por email (Para el login/registro)
    Usuario buscarPorEmail(String email);

    public Usuario buscarPorId(Long id);
}