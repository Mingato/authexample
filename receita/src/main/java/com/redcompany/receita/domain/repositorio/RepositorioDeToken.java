package com.redcompany.receita.domain.repositorio;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Token;

@Repository
public interface RepositorioDeToken extends MongoRepository<Token, String> {
	
	Optional<Token> findByIdUsuario(String idUsuario);
	
	Optional<Token> findByToken(String token);
}
