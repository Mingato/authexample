package com.redcompany.receita.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Result {
	
	private String status;
	
	private String message;
	
	@JsonInclude(Include.NON_NULL)
	private Receita receita;
	
	@JsonInclude(Include.NON_NULL)
	private Sintegra sintegra;
	
	@JsonInclude(Include.NON_NULL)
	private Simples simples;
	
	@JsonInclude(Include.NON_NULL)
	private Suframa suframa;

	
	public Result() {
		super();
	}
	
	public Result(String status, String message) {
		super();
		this.status = status;
		this.message = message;
		
		receita = null;
		sintegra = null;
		simples = null;
		suframa = null;
	}
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Receita getReceita() {
		return receita;
	}

	public void setReceita(Receita receita) {
		this.receita = receita;
	}

	public Sintegra getSintegra() {
		return sintegra;
	}

	public void setSintegra(Sintegra sintegra) {
		this.sintegra = sintegra;
	}

	public Simples getSimples() {
		return simples;
	}

	public void setSimples(Simples simples) {
		this.simples = simples;
	}

	public Suframa getSuframa() {
		return suframa;
	}

	public void setSuframa(Suframa suframa) {
		this.suframa = suframa;
	}
	
}
