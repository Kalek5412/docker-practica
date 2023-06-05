package com.kalek.springkcursos.service;

import com.kalek.springkcursos.entity.Curso;
import com.kalek.springkcursos.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> buscarPorId(Integer id);

    Optional<Curso> porIdConUsuarios(Integer id);
    Curso guardar(Curso curso);
    void eliminar(Integer id);

    void eliminarCursoUsuarioPorId(Integer id);
    Optional<Usuario> asignarUsuario(Usuario usuario,Integer cursoId);
    Optional<Usuario> crearUsuario(Usuario usuario,Integer cursoId);
    Optional<Usuario> eliminarUsuario(Usuario usuario,Integer cursoId);
}
