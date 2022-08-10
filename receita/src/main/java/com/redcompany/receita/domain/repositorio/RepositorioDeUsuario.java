package com.redcompany.receita.domain.repositorio;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Usuario;

/**
 * Created by vntclol on 30/09/2014.
 */
@Repository
public interface RepositorioDeUsuario extends MongoRepository<Usuario,String>{

    public Usuario findByEmail(String email);

    public Usuario findByEmailIgnoreCase(String email);
    
    public List<Usuario> findByNomeLikeIgnoreCaseOrderByNomeAsc(String nome);
    
    public List<Usuario> findByNomeLikeIgnoreCaseOrderByNomeAsc(String nome, Pageable pageable);
    
    public List<Usuario> findByNomeLikeIgnoreCaseOrderByNomeDesc(String nome, Pageable pageable);

    public List<Usuario> findByNomeLikeIgnoreCase(String nome, Pageable pageable);
}
