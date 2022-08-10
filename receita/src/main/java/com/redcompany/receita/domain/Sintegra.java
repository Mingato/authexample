package com.redcompany.receita.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document
public class Sintegra {

	private String code;
	private String status;
	private String message;
	
	
	@Id
	private String cnpj;
	
	@JsonInclude(Include.NON_NULL)
	private String nome_empresarial;
	
	@JsonInclude(Include.NON_NULL)
	private String nome_fantasia;
	
	@JsonInclude(Include.NON_NULL)
	private String inscricao_estadual;
	
	@JsonInclude(Include.NON_NULL)
	private String tipo_inscricao;
		
	@JsonInclude(Include.NON_NULL)
	private String data_situacao_cadastral;
	
	@JsonInclude(Include.NON_NULL)
	private String situacao_cnpj;
	
	@JsonInclude(Include.NON_NULL)
	private String situacao_ie;
	
	@JsonInclude(Include.NON_NULL)
	private String data_inicio_atividade;
	
	@JsonInclude(Include.NON_NULL)
	private String regime_tributacao;
	
	@JsonInclude(Include.NON_NULL)
	private String informacao_ie_como_destinatario;
	
	@JsonInclude(Include.NON_NULL)
	private String porte_empresa;
	
	@JsonInclude(Include.NON_NULL)
	private Cnae_principal cnae_principal;
	
	@JsonInclude(Include.NON_NULL)
	private String data_fim_atividade;
	
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
	private long dataConsulta;
	
	@JsonIgnore
	private boolean buscouDaReceita = false;
	
	public Sintegra() {
		super();
		this.code = "";
		this.status = "";
		this.message = "";
	}
	

	public Sintegra(String cnpj, String nome_empresarial, String nome_fantasia, String inscricao_estadual,
			String tipo_inscricao, String data_situacao_cadastral, String situacao_cnpj, String situacao_ie,
			String data_inicio_atividade, String regime_tributacao, String informacao_ie_como_destinatario,
			String porte_empresa, Cnae_principal cnae_principal, String data_fim_atividade, String uf, String cep,
			String municipio, String bairro, String logradouro, String complemento, String numero) {
		super();
		this.cnpj = cnpj;
		this.nome_empresarial = nome_empresarial;
		this.nome_fantasia = nome_fantasia;
		this.inscricao_estadual = inscricao_estadual;
		this.tipo_inscricao = tipo_inscricao;
		this.data_situacao_cadastral = data_situacao_cadastral;
		this.situacao_cnpj = situacao_cnpj;
		this.situacao_ie = situacao_ie;
		this.data_inicio_atividade = data_inicio_atividade;
		this.regime_tributacao = regime_tributacao;
		this.informacao_ie_como_destinatario = informacao_ie_como_destinatario;
		this.porte_empresa = porte_empresa;
		this.cnae_principal = cnae_principal;
		this.data_fim_atividade = data_fim_atividade;
		this.uf = uf;
		this.cep = cep;
		this.municipio = municipio;
		this.bairro = bairro;
		this.logradouro = logradouro;
		this.complemento = complemento;
		this.numero = numero;
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

	public String getNome_fantasia() {
		return nome_fantasia;
	}

	public void setNome_fantasia(String nome_fantasia) {
		this.nome_fantasia = nome_fantasia;
	}

	public String getInscricao_estadual() {
		return inscricao_estadual;
	}

	public void setInscricao_estadual(String inscricao_estadual) {
		this.inscricao_estadual = inscricao_estadual;
	}

	public String getTipo_inscricao() {
		return tipo_inscricao;
	}

	public void setTipo_inscricao(String tipo_inscricao) {
		this.tipo_inscricao = tipo_inscricao;
	}

	public String getData_situacao_cadastral() {
		return data_situacao_cadastral;
	}

	public void setData_situacao_cadastral(String data_situacao_cadastral) {
		this.data_situacao_cadastral = data_situacao_cadastral;
	}

	public String getSituacao_cnpj() {
		return situacao_cnpj;
	}

	public void setSituacao_cnpj(String situacao_cnpj) {
		this.situacao_cnpj = situacao_cnpj;
	}

	public String getSituacao_ie() {
		return situacao_ie;
	}

	public void setSituacao_ie(String situacao_ie) {
		this.situacao_ie = situacao_ie;
	}

	public String getData_inicio_atividade() {
		return data_inicio_atividade;
	}

	public void setData_inicio_atividade(String data_inicio_atividade) {
		this.data_inicio_atividade = data_inicio_atividade;
	}

	public String getRegime_tributacao() {
		return regime_tributacao;
	}

	public void setRegime_tributacao(String regime_tributacao) {
		this.regime_tributacao = regime_tributacao;
	}

	public String getInformacao_ie_como_destinatario() {
		return informacao_ie_como_destinatario;
	}

	public void setInformacao_ie_como_destinatario(String informacao_ie_como_destinatario) {
		this.informacao_ie_como_destinatario = informacao_ie_como_destinatario;
	}

	public String getPorte_empresa() {
		return porte_empresa;
	}

	public void setPorte_empresa(String porte_empresa) {
		this.porte_empresa = porte_empresa;
	}

	public Cnae_principal getCnae_principal() {
		return cnae_principal;
	}

	public void setCnae_principal(Cnae_principal cnae_principal) {
		this.cnae_principal = cnae_principal;
	}

	public String getData_fim_atividade() {
		return data_fim_atividade;
	}

	public void setData_fim_atividade(String data_fim_atividade) {
		this.data_fim_atividade = data_fim_atividade;
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
