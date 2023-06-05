package com.kalek.springkcursos.controller;

import com.kalek.springkcursos.entity.Curso;
import com.kalek.springkcursos.entity.Usuario;
import com.kalek.springkcursos.service.CursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController

public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Integer id){
        Optional<Curso> cursoOp= cursoService.porIdConUsuarios(id);//cursoService.buscarPorId(id);
        if(cursoOp.isPresent()){
            return ResponseEntity.ok(cursoOp.get());
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/")
    public ResponseEntity<?> crear(@RequestBody Curso curso,BindingResult  result){
        if(result.hasErrors()){
            return validar(result);
        }
        Curso cursoDb = cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Curso curso,BindingResult result, @PathVariable Integer id){
        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Curso> cursoOp= cursoService.buscarPorId(id);
        if(cursoOp.isPresent()){
            Curso ncurso=cursoOp.get();
            ncurso.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(ncurso));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id){
        Optional<Curso> cursoOp= cursoService.buscarPorId(id);
        if(cursoOp.isPresent()){
            cursoService.eliminar(cursoOp.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Integer cursoId){
    Optional<Usuario> o;
    try{
        o=cursoService.asignarUsuario(usuario, cursoId);
        }catch (FeignException e){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                  Collections.singletonMap("mensaje","No existe el usuario "+
                          "el id o error en la comunicacion: " + e.getMessage()));
        }
        if(o.isPresent()){
        return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crarUsuario(@RequestBody Usuario usuario, @PathVariable Integer cursoId){
        Optional<Usuario> o;
        try{
            o=cursoService.crearUsuario(usuario,cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Collections.singletonMap("mensaje","No se logro crear el usuario "+
                            "el id o error en la comunicacion: " + e.getMessage()));
        }if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }
    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Integer cursoId){
        Optional<Usuario> o;
        try{
            o=cursoService.eliminarUsuario(usuario,cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Collections.singletonMap("mensaje","No existe el usuario "+
                            "el id o error en la comunicacion: " + e.getMessage()));
        }if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> eliminarCursoUsuarioPorId(@PathVariable Integer id){
        cursoService.eliminarCursoUsuarioPorId((id));
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<Map<String,String>> validar(BindingResult result){
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

}
