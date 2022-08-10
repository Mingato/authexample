package com.redcompany.receita.domain.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Plano;

@Repository
public interface RepositorioDePlanos extends MongoRepository<Plano, String> {


	List<Plano> findByIdUsuario(String idUsuario);
	

}
