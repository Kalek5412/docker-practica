package com.kalek.springkusuarios.repository;

import com.kalek.springkusuarios.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario,Integer> {
    Optional<Usuario> findByEmail(String email);
    
    @Query("select u from Usuario u where u.email=?1")
    Optional<Usuario> porEmail(String email);
}
