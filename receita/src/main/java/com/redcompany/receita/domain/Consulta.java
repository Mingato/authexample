package com.redcompany.receita.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Consulta {
	
	@Id
	private String _id;
	
	private String idUsuario;
	
	private Long dataConsulta;
	
	private TipoConsulta tipoConsulta;
	
	private String  parametro;
	
	private String  chave;
	
	private String  servico;	
	
	@JsonIgnore
	private Fornecedor  fornecedor;
	
	private String source;
	
	public Consulta() {
		super();
	}
	
	public Consulta(String id, String idUsuario, Long dataConsulta) {
		super();
		this._id = id;
		this.dataConsulta = dataConsulta;
	}
		

	public Consulta(String id, String idUsuario, Long dataConsulta, TipoConsulta tipoConsulta, String parametro,
			String chave, String servico, Fornecedor fornecedor, String source) {
		super();
		this._id = id;
		this.idUsuario = idUsuario;
		this.dataConsulta = dataConsulta;
		this.tipoConsulta = tipoConsulta;
		this.parametro = parametro;
		this.chave = chave;
		this.servico = servico;
		this.fornecedor = fornecedor;
		this.source = source;
	}
	

	public String getId() {
		return _id;
	}

	public void setId(String _id) {
		this._id = _id;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(Long dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public TipoConsulta getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(TipoConsulta tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getServico() {
		return servico;
	}

	public void setServico(String servico) {
		this.servico = servico;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
}
