package com.redcompany.receita.domain.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Fornecedor;

@Repository
public interface RepositorioDeFornecedor extends MongoRepository<Fornecedor, String> {
	

}
