package com.redcompany.receita.domain.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Suframa;

@Repository
public interface RepositorioDeSuframa extends MongoRepository<Suframa, String> {

	Optional<Suframa> findByCnpj(String cnpj);

	List<Suframa> findByCnpjAndDataConsultaLessThan(String cnpj, Long dataConsulta);
	
	List<Suframa> findByCnpjAndDataConsultaGreaterThan(String cnpj, Long dataConsulta);
}
