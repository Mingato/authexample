package com.redcompany.receita.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document
public class Simples {

	private String code;	
	private String status;
	private String message;
	
	@Id
	private String cnpj;
	
	@JsonInclude(Include.NON_NULL)
	private String cnpj_matriz;
	
	@JsonInclude(Include.NON_NULL)
	private String nome_empresarial;
	
	@JsonInclude(Include.NON_NULL)
	private String situacao_simples_nacional;
	
	@JsonInclude(Include.NON_NULL)
	private String situacao_simei;
		
	@JsonInclude(Include.NON_NULL)
	private String situacao_simples_nacional_anterior;
	
	@JsonInclude(Include.NON_NULL)
	private String situacao_simei_anterior;
	
	@JsonInclude(Include.NON_NULL)
	private String agendamentos;
	
	@JsonInclude(Include.NON_NULL)
	private String eventos_futuros_simples_nacional;
	
	@JsonInclude(Include.NON_NULL)
	private String eventos_futuros_simples_simei;
	
	@JsonInclude(Include.NON_NULL)
	private long dataConsulta;
	
	@JsonIgnore
	private boolean buscouDaReceita = false;
	
	public Simples() {
		super();
		
		this.code = "";
		this.status = "";
		this.message = "";
	}
	

	public Simples(String cnpj, String cnpj_matriz, String nome_empresarial, String situacao_simples_nacional,
			String situacao_simei, String situacao_simples_nacional_anterior, String situacao_simei_anterior,
			String agendamentos, String eventos_futuros_simples_nacional, String eventos_futuros_simples_simei) {
		super();
		this.cnpj = cnpj;
		this.cnpj_matriz = cnpj_matriz;
		this.nome_empresarial = nome_empresarial;
		this.situacao_simples_nacional = situacao_simples_nacional;
		this.situacao_simei = situacao_simei;
		this.situacao_simples_nacional_anterior = situacao_simples_nacional_anterior;
		this.situacao_simei_anterior = situacao_simei_anterior;
		this.agendamentos = agendamentos;
		this.eventos_futuros_simples_nacional = eventos_futuros_simples_nacional;
		this.eventos_futuros_simples_simei = eventos_futuros_simples_simei;
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

	public String getCnpj_matriz() {
		return cnpj_matriz;
	}

	public void setCnpj_matriz(String cnpj_matriz) {
		this.cnpj_matriz = cnpj_matriz;
	}

	public String getNome_empresarial() {
		return nome_empresarial;
	}

	public void setNome_empresarial(String nome_empresarial) {
		this.nome_empresarial = nome_empresarial;
	}

	public String getSituacao_simples_nacional() {
		return situacao_simples_nacional;
	}

	public void setSituacao_simples_nacional(String situacao_simples_nacional) {
		this.situacao_simples_nacional = situacao_simples_nacional;
	}

	public String getSituacao_simei() {
		return situacao_simei;
	}

	public void setSituacao_simei(String situacao_simei) {
		this.situacao_simei = situacao_simei;
	}

	public String getSituacao_simples_nacional_anterior() {
		return situacao_simples_nacional_anterior;
	}

	public void setSituacao_simples_nacional_anterior(String situacao_simples_nacional_anterior) {
		this.situacao_simples_nacional_anterior = situacao_simples_nacional_anterior;
	}

	public String getSituacao_simei_anterior() {
		return situacao_simei_anterior;
	}

	public void setSituacao_simei_anterior(String situacao_simei_anterior) {
		this.situacao_simei_anterior = situacao_simei_anterior;
	}

	public String getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(String agendamentos) {
		this.agendamentos = agendamentos;
	}

	public String getEventos_futuros_simples_nacional() {
		return eventos_futuros_simples_nacional;
	}

	public void setEventos_futuros_simples_nacional(String eventos_futuros_simples_nacional) {
		this.eventos_futuros_simples_nacional = eventos_futuros_simples_nacional;
	}

	public String getEventos_futuros_simples_simei() {
		return eventos_futuros_simples_simei;
	}

	public void setEventos_futuros_simples_simei(String eventos_futuros_simples_simei) {
		this.eventos_futuros_simples_simei = eventos_futuros_simples_simei;
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
