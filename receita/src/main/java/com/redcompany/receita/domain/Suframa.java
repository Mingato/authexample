package com.redcompany.receita.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document
public class Suframa {

	private String code;
	private String status;
	private String message;
	
	@Id
	private String cnpj;
	
	@JsonInclude(Include.NON_NULL)
	private String nome_empresarial;
	
	@JsonInclude(Include.NON_NULL)
	private String inscricao_suframa;
	
	@JsonInclude(Include.NON_NULL)
	private String data_validade_cadastral;
	
	@JsonInclude(Include.NON_NULL)
	private String situacao_cadastral;

	@JsonInclude(Include.NON_NULL)
	private String uf;
	
	@JsonInclude(Include.NON_NULL)
	private String cep;
	
	@JsonInclude(Include.NON_NULL)
	private String municipio;
	
	@JsonInclude(Include.NON_NULL)
	private String bairro;
	
	@JsonInclude(Include.NON_NULL)
	private String logradouro;
	
	@JsonInclude(Include.NON_NULL)
	private String complemento;
	
	@JsonInclude(Include.NON_NULL)
	private String numero;
	
	@JsonInclude(Include.NON_NULL)
	private String endereco_eletronico;
	
	@JsonInclude(Include.NON_NULL)
	private String telefone;
	
	@JsonInclude(Include.NON_NULL)
	private String tipo_incentivo;
	
	@JsonInclude(Include.NON_NULL)
	private String observacao;	

	@JsonInclude(Include.NON_NULL)
	private long dataConsulta;
	
	@JsonIgnore
	private boolean buscouDaReceita = false;
	
	public Suframa() {
		super();
		
		this.code = "";
		this.status = "";
		this.message = "";
	}
	

	public Suframa(String cnpj, String nome_empresarial, String inscricao_suframa, String data_validade_cadastral,
			String situacao_cadastral, String uf, String cep, String municipio, String bairro, String logradouro,
			String complemento, String numero, String endereco_eletronico, String telefone, String tipo_incentivo,
			String observacao) {
		super();
		this.cnpj = cnpj;
		this.nome_empresarial = nome_empresarial;
		this.inscricao_suframa = inscricao_suframa;
		this.data_validade_cadastral = data_validade_cadastral;
		this.situacao_cadastral = situacao_cadastral;
		this.uf = uf;
		this.cep = cep;
		this.municipio = municipio;
		this.bairro = bairro;
		this.logradouro = logradouro;
		this.complemento = complemento;
		this.numero = numero;
		this.endereco_eletronico = endereco_eletronico;
		this.telefone = telefone;
		this.tipo_incentivo = tipo_incentivo;
		this.observacao = observacao;
		this.dataConsulta = new Date().getTime();
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
	
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getNome_empresarial() {
		return nome_empresarial;
	}

	public void setNome_empresarial(String nome_empresarial) {
		this.nome_empresarial = nome_empresarial;
	}

	public String getInscricao_suframa() {
		return inscricao_suframa;
	}

	public void setInscricao_suframa(String inscricao_suframa) {
		this.inscricao_suframa = inscricao_suframa;
	}

	public String getData_validade_cadastral() {
		return data_validade_cadastral;
	}

	public void setData_validade_cadastral(String data_validade_cadastral) {
		this.data_validade_cadastral = data_validade_cadastral;
	}

	public String getSituacao_cadastral() {
		return situacao_cadastral;
	}

	public void setSituacao_cadastral(String situacao_cadastral) {
		this.situacao_cadastral = situacao_cadastral;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEndereco_eletronico() {
		return endereco_eletronico;
	}

	public void setEndereco_eletronico(String endereco_eletronico) {
		this.endereco_eletronico = endereco_eletronico;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getTipo_incentivo() {
		return tipo_incentivo;
	}

	public void setTipo_incentivo(String tipo_incentivo) {
		this.tipo_incentivo = tipo_incentivo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public long getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(long dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public boolean isBuscouDaReceita() {
		return buscouDaReceita;
	}

	public void setBuscouDaReceita(boolean buscouDaReceita) {
		this.buscouDaReceita = buscouDaReceita;
	}
}
