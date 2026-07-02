package com.ucv.lab12.repository;

import com.ucv.lab12.model.Usuario;

import java.util.Optional;

public interface IUsuarioRepository {

    Optional<Usuario> findByUsuario(String usuario);

    void save(Usuario usuario);

    void updateUltimoAcceso(int idUsuario);
}
