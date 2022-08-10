package com.redcompany.receita.domain.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.CodigoDeSeguranca;

@Repository
public interface RepositorioDeCodigoDeSeguranca extends MongoRepository<CodigoDeSeguranca, String> {

}
