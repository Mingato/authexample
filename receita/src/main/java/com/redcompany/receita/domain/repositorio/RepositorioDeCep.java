package com.redcompany.receita.domain.repositorio;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Cep;
import com.redcompany.receita.domain.Receita;

@Repository
public interface RepositorioDeCep extends MongoRepository<Cep, String> {


	Optional<Receita> findByCep(String cep);
	

}
