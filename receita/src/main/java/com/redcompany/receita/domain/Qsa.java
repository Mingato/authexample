package com.redcompany.receita.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Qsa {

	
	private String nome;
	
	private String qual;
	
	@JsonInclude(Include.NON_NULL)
	private String pais_origem;
	
	@JsonInclude(Include.NON_NULL)
	private String nome_rep_legal;
	
	@JsonInclude(Include.NON_NULL)
	private String qual_rep_legal;	
	
	
	public Qsa(String nome, String qual, String pais_origem, String nome_rep_legal, String qual_rep_legal) {
		super();
		this.nome = nome;
		this.qual = qual;
		this.pais_origem = pais_origem;
		this.nome_rep_legal = nome_rep_legal;
		this.qual_rep_legal = qual_rep_legal;
	}

	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getQual() {
		return qual;
	}

	public void setQual(String qual) {
		this.qual = qual;
	}

	public String getPais_origem() {
		return pais_origem;
	}

	public void setPais_origem(String pais_origem) {
		this.pais_origem = pais_origem;
	}

	public String getNome_rep_legal() {
		return nome_rep_legal;
	}

	public void setNome_rep_legal(String nome_rep_legal) {
		this.nome_rep_legal = nome_rep_legal;
	}

	public String getQual_rep_legal() {
		return qual_rep_legal;
	}

	public void setQual_rep_legal(String qual_rep_legal) {
		this.qual_rep_legal = qual_rep_legal;
	}
		
}
