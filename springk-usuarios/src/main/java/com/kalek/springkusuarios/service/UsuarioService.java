package com.kalek.springkusuarios.service;

import com.kalek.springkusuarios.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> buscarPorId(Integer id);
    Usuario guardar(Usuario usuario);
    void eliminar(Integer id);

    List<Usuario> listarPorIds(Iterable<Integer> ids);
    Optional<Usuario> buscarPorEmail(String email);

}
