package com.redcompany.receita.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "consulta_fornecedor")
public class Consulta_Fornecedor {

	@Id
	private String _id;
	
	private String servico;
	
	private TipoConsulta tipoConsulta;
	
	private Fornecedor fornecedor;
	
	private String url;
		
	private int prioridade;
	
	private boolean ativo;

	
	public Consulta_Fornecedor(String id, String servico, TipoConsulta tipoConsulta, Fornecedor fornecedor,
			String url,	int prioridade, boolean ativo) {
		super();
		this._id = id;
		this.servico = servico;
		this.tipoConsulta = tipoConsulta;
		this.fornecedor = fornecedor;
		this.url = url;
		this.prioridade = prioridade;
		this.ativo = ativo;
	}

	
	public String getId() {
		return _id;
	}

	public void setId(String id) {
		this._id = id;
	}

	public String getServico() {
		return servico;
	}

	public void setServico(String servico) {
		this.servico = servico;
	}

	public TipoConsulta getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(TipoConsulta tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
