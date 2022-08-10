package com.redcompany.receita.domain.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Cpf;

@Repository
public interface RepositorioDeCpf extends MongoRepository<Cpf, String> {

	Optional<Cpf> findByCpf(String cpf);
	
	List<Cpf> findByCpfAndDataConsultaLessThan(String cpf, Long dataConsulta);

}
