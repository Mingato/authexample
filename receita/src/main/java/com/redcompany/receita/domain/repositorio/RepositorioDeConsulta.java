package com.redcompany.receita.domain.repositorio;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Consulta;

@Repository
public interface RepositorioDeConsulta extends MongoRepository<Consulta, String> {

	Optional<Consulta> findById(String id);
	
	Optional<Consulta> findByIdUsuario(String idUsuario);

	List<Consulta> findByIdUsuarioAndDataConsultaGreaterThanOrderByDataConsulta(String idUsuario, Long tempo);

	List<Consulta> findByIdUsuarioAndDataConsultaBetweenOrderByDataConsulta(String idUsuario,
			Long beginData, Long endData);

}
