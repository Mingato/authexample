package com.redcompany.receita.domain.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Sintegra;

@Repository
public interface RepositorioDeSintegra extends MongoRepository<Sintegra, String> {

	Optional<Sintegra> findByCnpj(String cnpj);

	List<Sintegra> findByCnpjAndDataConsultaLessThan(String cnpj, Long dataConsulta);
	
	List<Sintegra> findByCnpjAndDataConsultaGreaterThan(String cnpj, Long dataConsulta);
	
}
