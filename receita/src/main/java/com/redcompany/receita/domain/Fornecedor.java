package com.redcompany.receita.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Fornecedor {
	
	@Id
	private String _id;
	
	private String nome;
	
	private String url;
	
	private String tokenConsulta;
	

	public Fornecedor(String _id, String nome, String url, String tokenConsulta) {
		super();
		this._id = _id;
		this.nome = nome;
		this.url = url;
		this.tokenConsulta = tokenConsulta;
	}
	

	public String get_id() {
		return _id;
	}


	public void set_id(String _id) {
		this._id = _id;
	}



	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getTokenConsulta() {
		return tokenConsulta;
	}

	public void setTokenConsulta(String tokenConsulta) {
		this.tokenConsulta = tokenConsulta;
	}
	
	
	
}
