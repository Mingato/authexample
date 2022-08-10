package com.redcompany.receita.domain.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.SaldoSintegra;

@Repository
public interface RepositorioDeSaldoSintegra extends MongoRepository<SaldoSintegra, Long> {

}
