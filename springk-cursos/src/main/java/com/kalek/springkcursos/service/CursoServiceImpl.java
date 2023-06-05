package com.kalek.springkcursos.service;

import com.kalek.springkcursos.clients.UsuarioClienteRest;
import com.kalek.springkcursos.entity.Curso;
import com.kalek.springkcursos.entity.CursoUsuario;
import com.kalek.springkcursos.entity.Usuario;
import com.kalek.springkcursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private UsuarioClienteRest usuarioClienteRest;

    @Override
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }

    @Override
    public Optional<Curso> buscarPorId(Integer id) {
        return cursoRepository.findById(id);
    }

    @Override
    public Optional<Curso> porIdConUsuarios(Integer id) {
        Optional<Curso> o = cursoRepository.findById(id);
        if(o.isPresent()){
            Curso curso= o.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Integer> ids = curso.getCursoUsuarios().stream().map(
                  cu->cu.getUsuarioId()).collect(Collectors.toList());
                List<Usuario> usuarios = usuarioClienteRest.obtenerAlumnoPorCurso(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    public void eliminar(Integer id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Integer id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }

    @Override
   @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Integer cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if (o.isPresent()) {
            Usuario usuarioMsvc =usuarioClienteRest.detalle(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Integer cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if (o.isPresent()){
            Usuario usuarionms= usuarioClienteRest.crear(usuario);

            Curso curso = o.get();
            CursoUsuario cursoUsuario=new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarionms.getId());

            curso.addCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarionms);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Integer cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);
        if (o.isPresent()){
            Usuario usuarionms= usuarioClienteRest.detalle(usuario.getId());
            Curso curso = o.get();
            CursoUsuario cursoUsuario=new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarionms.getId());
            curso.removeCursoUsuario(cursoUsuario);
            cursoRepository.save(curso);
            return Optional.of(usuarionms);
        }
        return Optional.empty();
    }
}
