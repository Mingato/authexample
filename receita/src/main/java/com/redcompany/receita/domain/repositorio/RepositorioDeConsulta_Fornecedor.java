package com.redcompany.receita.domain.repositorio;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.redcompany.receita.domain.Consulta_Fornecedor;
import com.redcompany.receita.domain.TipoConsulta;

@Repository
public interface RepositorioDeConsulta_Fornecedor extends MongoRepository<Consulta_Fornecedor, String> {

	List<Consulta_Fornecedor> findByTipoConsultaOrderByPrioridadeDesc(TipoConsulta tipoConsulta);
}
