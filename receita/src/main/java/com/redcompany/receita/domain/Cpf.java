package com.redcompany.receita.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Cpf {

	private String code;
	private String status;
	private String message;
	
	@Id
	private String cpf;
	
	private String nome;
	
	private String data_nascimento;
	
	private String situacao_cadastral;
	
	private String data_inscricao;
	
	private String digito_verificador;
	
	private String comprovante;
	
	private String version;
	
	@JsonIgnore
	private Long dataConsulta;
	
	public Cpf() {
		super();
	}

	public Cpf(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public Cpf(String code, String status, String message, String cpf, String nome, String data_nascimento,
			String situacao_cadastral, String data_inscricao, String digito_verificador, String comprovante,
			String version) {
		super();
		this.code = code;
		this.status = status;
		this.message = message;
		this.cpf = cpf;
		this.nome = nome;
		this.data_nascimento = data_nascimento;
		this.situacao_cadastral = situacao_cadastral;
		this.data_inscricao = data_inscricao;
		this.digito_verificador = digito_verificador;
		this.comprovante = comprovante;
		this.version = version;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(String data_nascimento) {
		this.data_nascimento = data_nascimento;
	}

	public String getSituacao_cadastral() {
		return situacao_cadastral;
	}

	public void setSituacao_cadastral(String situacao_cadastral) {
		this.situacao_cadastral = situacao_cadastral;
	}

	public String getData_inscricao() {
		return data_inscricao;
	}

	public void setData_inscricao(String data_inscricao) {
		this.data_inscricao = data_inscricao;
	}

	public String getDigito_verificador() {
		return digito_verificador;
	}

	public void setDigito_verificador(String digito_verificador) {
		this.digito_verificador = digito_verificador;
	}

	public String getComprovante() {
		return comprovante;
	}

	public void setComprovante(String comprovante) {
		this.comprovante = comprovante;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(Long dataConsulta) {
		this.dataConsulta = dataConsulta;
	}
	
}
