package com.ucv.lab12.service;

import com.ucv.lab12.model.Usuario;
import com.ucv.lab12.repository.IUsuarioRepository;
import com.ucv.lab12.security.PasswordUtil;

import java.util.Optional;

public class AuthService implements IAuthService {

    private final IUsuarioRepository usuarioRepository;

    public AuthService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> autenticar(String usuario, String clave) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return Optional.empty();
        }
        if (clave == null || clave.isEmpty()) {
            return Optional.empty();
        }

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByUsuario(usuario.trim());
        if (usuarioEncontrado.isEmpty()) {
            return Optional.empty();
        }

        Usuario actual = usuarioEncontrado.get();
        if (!actual.isActivo()) {
            return Optional.empty();
        }

        if (!PasswordUtil.verificar(clave, actual.getSaltClave(), actual.getHashClave())) {
            return Optional.empty();
        }

        usuarioRepository.updateUltimoAcceso(actual.getIdUsuario());
        return Optional.of(actual);
    }
}
