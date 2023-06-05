package com.kalek.springkusuarios.controller;

import com.kalek.springkusuarios.entity.Usuario;
import com.kalek.springkusuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Integer id){
        Optional<Usuario> usuarioOp=usuarioService.buscarPorId(id);
        if(usuarioOp.isPresent()){
            return ResponseEntity.ok(usuarioOp.get());
        }
        //return ResponseEntity.notFound().build();
        return new ResponseEntity<>(usuarioOp, HttpStatus.NOT_FOUND);
    }
    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){
        if(!usuario.getEmail().isEmpty() && usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("msj","El correo no se encuentra disponible en la BD"));
        }
        if(result.hasErrors()){
            return validar(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result,@PathVariable Integer id){

        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Usuario> usuarioOp=usuarioService.buscarPorId(id);
        if(usuarioOp.isPresent()){
            Usuario nusuario=usuarioOp.get();
            if(!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(nusuario.getEmail()) && usuarioService.buscarPorEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest().body(Collections.singletonMap("msj","El correo ya existe en la BD"));
            }
            nusuario.setNombre(usuario.getNombre());
            nusuario.setEmail(usuario.getEmail());
            nusuario.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(nusuario));
        }
        return new ResponseEntity<>(usuarioOp, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id){
        Optional<Usuario> usuarioOp=usuarioService.buscarPorId(id);
        if(usuarioOp.isPresent()){
            usuarioService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(usuarioOp, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnoPorCurso(@RequestParam List<Integer> ids){
        return ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }

    private ResponseEntity<Map<String,String>> validar(BindingResult result){
        Map<String,String> errores= new HashMap<>();
        result.getFieldErrors().forEach(err->{
            errores.put(err.getField(),"El campo " + err.getField()+" "+err.getDefaultMessage());
        });
        return new ResponseEntity<>(errores,HttpStatus.BAD_REQUEST);
    }

}
