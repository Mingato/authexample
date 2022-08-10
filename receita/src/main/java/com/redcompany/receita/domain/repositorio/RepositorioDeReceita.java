package com.redcompany.receita.domain.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Receita;

@Repository
public interface RepositorioDeReceita extends MongoRepository<Receita, String> {

	Receita findByEmail(String email);

	Optional<Receita> findByCnpj(String cnpj);
	
	List<Receita> findByCnpjAndDataConsultaLessThan(String cnpj, Long dataConsulta);
	
	List<Receita> findByCnpjAndDataConsultaGreaterThan(String cnpj, Long dataConsulta);

}
