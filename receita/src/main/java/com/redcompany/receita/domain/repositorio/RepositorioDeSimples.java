package com.redcompany.receita.domain.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Simples;

@Repository
public interface RepositorioDeSimples extends MongoRepository<Simples, String> {

	Optional<Simples> findByCnpj(String cnpj);

	List<Simples> findByCnpjAndDataConsultaLessThan(String cnpj, Long dataConsulta);
	
	List<Simples> findByCnpjAndDataConsultaGreaterThan(String cnpj, Long dataConsulta);
}
