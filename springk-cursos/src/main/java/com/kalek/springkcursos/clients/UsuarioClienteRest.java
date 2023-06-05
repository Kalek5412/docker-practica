package com.kalek.springkcursos.clients;

import com.kalek.springkcursos.entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "msvc-usuarios", url="localhost:8001")
public interface UsuarioClienteRest {
    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Integer id);
    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerAlumnoPorCurso(@RequestParam Iterable<Integer> ids);

}
