package com.redcompany.receita.domain;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document
public class Receita {

	private String code;
	private String status;
	private String message;
	
	@Id
	private String cnpj;
	
	@JsonInclude(Include.NON_NULL)
	private String tipo;//matriz, filial
	
	@JsonInclude(Include.NON_NULL)
	private String abertura;//abertura
	
	@JsonInclude(Include.NON_NULL)
	private String nome;//razaoSocial
	
	@JsonInclude(Include.NON_NULL)
	private String fantasia;
	
	@JsonInclude(Include.NON_NULL)
	private ArrayList<Atividade> atividade_principal;
	
	@JsonInclude(Include.NON_NULL)
	private ArrayList<Atividade> atividades_secundarias;
	
	@JsonInclude(Include.NON_NULL)
	private String natureza_juridica;
	
	@JsonInclude(Include.NON_NULL)
	private String logradouro;
	
	@JsonInclude(Include.NON_NULL)
	private String numero;
	
	@JsonInclude(Include.NON_NULL)
	private String complemento;
	
	@JsonInclude(Include.NON_NULL)
	private String cep;
	
	@JsonInclude(Include.NON_NULL)
	private String bairro;
	
	@JsonInclude(Include.NON_NULL)
	private String municipio;
	
	@JsonInclude(Include.NON_NULL)
	private String uf;
	
	@JsonInclude(Include.NON_NULL)
	private String email;
	
	@JsonInclude(Include.NON_NULL)
	private String telefone;
	
	@JsonInclude(Include.NON_NULL)
	private String efr;
	
	@JsonInclude(Include.NON_NULL)
	private String situacao;
	
	@JsonInclude(Include.NON_NULL)
	private String data_situacao;
	
	@JsonInclude(Include.NON_NULL)
	private String motivo_situacao;
	
	@JsonInclude(Include.NON_NULL)
	private String situacao_especial;
	
	@JsonInclude(Include.NON_NULL)
	private String data_situacao_especial;
	
	@JsonInclude(Include.NON_NULL)
	private String capital_social;
	
	@JsonInclude(Include.NON_NULL)
	private ArrayList<Qsa> qsa;
		
	@JsonInclude(Include.NON_NULL)
	private Object extra;

	@JsonInclude(Include.NON_NULL)
	private String file;
	
	@JsonInclude(Include.NON_NULL)
	private long dataConsulta = new Date().getTime();
	
	@JsonIgnore
	private boolean buscouDaReceita = false;
	
	public Receita() {
		super();
		this.code = "";
		this.status = "";
		this.message = "";
	}
	
	public Receita(String cnpj, String tipo, String abertura, String nome, String fantasia,
			ArrayList<Atividade> atividade_principal, ArrayList<Atividade> atividades_secundarias,
			String natureza_juridica, String logradouro, String numero, String complemento, String cep, String bairro,
			String municipio, String uf, String email, String telefone, String efr, String situacao,
			String data_situacao, String motivo_situacao, String situacao_especial, String data_situacao_especial,
			String capital_social, ArrayList<Qsa> qsa, Object extra, String file) {
		super();
		this.cnpj = cnpj;
		this.tipo = tipo;
		this.abertura = abertura;
		this.nome = nome;
		this.fantasia = fantasia;
		this.atividade_principal = atividade_principal;
		this.atividades_secundarias = atividades_secundarias;
		this.natureza_juridica = natureza_juridica;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.cep = cep;
		this.bairro = bairro;
		this.municipio = municipio;
		this.uf = uf;
		this.email = email;
		this.telefone = telefone;
		this.efr = efr;
		this.situacao = situacao;
		this.data_situacao = data_situacao;
		this.motivo_situacao = motivo_situacao;
		this.situacao_especial = situacao_especial;
		this.data_situacao_especial = data_situacao_especial;
		this.capital_social = capital_social;
		this.qsa = qsa;
		this.extra = extra;
		this.file = file;
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAbertura() {
		return abertura;
	}

	public void setAbertura(String abertura) {
		this.abertura = abertura;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFantasia() {
		return fantasia;
	}

	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}

	public ArrayList<Atividade> getAtividade_principal() {
		return atividade_principal;
	}

	public void setAtividade_principal(ArrayList<Atividade> atividade_principal) {
		this.atividade_principal = atividade_principal;
	}
	
	public void setAtividade_principal(Atividade atividade_principal) {
		ArrayList<Atividade> atividade = new ArrayList<Atividade>();
		atividade.add(atividade_principal);
		this.atividade_principal = atividade;
	}

	public ArrayList<Atividade> getAtividades_secundarias() {
		return atividades_secundarias;
	}

	public void setAtividades_secundarias(ArrayList<Atividade> atividades_secundarias) {
		this.atividades_secundarias = atividades_secundarias;
	}

	public String getNatureza_juridica() {
		return natureza_juridica;
	}

	public void setNatureza_juridica(String natureza_juridica) {
		this.natureza_juridica = natureza_juridica;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEfr() {
		return efr;
	}

	public void setEfr(String efr) {
		this.efr = efr;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getData_situacao() {
		return data_situacao;
	}

	public void setData_situacao(String data_situacao) {
		this.data_situacao = data_situacao;
	}

	public String getMotivo_situacao() {
		return motivo_situacao;
	}

	public void setMotivo_situacao(String motivo_situacao) {
		this.motivo_situacao = motivo_situacao;
	}

	public String getSituacao_especial() {
		return situacao_especial;
	}

	public void setSituacao_especial(String situacao_especial) {
		this.situacao_especial = situacao_especial;
	}

	public String getData_situacao_especial() {
		return data_situacao_especial;
	}

	public void setData_situacao_especial(String data_situacao_especial) {
		this.data_situacao_especial = data_situacao_especial;
	}

	public String getCapital_social() {
		return capital_social;
	}

	public void setCapital_social(String capital_social) {
		this.capital_social = capital_social;
	}

	public ArrayList<Qsa> getQsa() {
		return qsa;
	}

	public void setQsa(ArrayList<Qsa> qsa) {
		this.qsa = qsa;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}
	
	public long getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(long dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean isBuscouDaReceita() {
		return buscouDaReceita;
	}

	public void setBuscouDaReceita(boolean buscouDaReceita) {
		this.buscouDaReceita = buscouDaReceita;
	}

	@Override
	public String toString() {
		return "Receita [code=" + code + ", status=" + status + ", message=" + message + ", cnpj=" + cnpj + ", tipo="
				+ tipo + ", abertura=" + abertura + ", nome=" + nome + ", fantasia=" + fantasia
				+ ", atividade_principal=" + atividade_principal + ", atividades_secundarias=" + atividades_secundarias
				+ ", natureza_juridica=" + natureza_juridica + ", logradouro=" + logradouro + ", numero=" + numero
				+ ", complemento=" + complemento + ", cep=" + cep + ", bairro=" + bairro + ", municipio=" + municipio
				+ ", uf=" + uf + ", email=" + email + ", telefone=" + telefone + ", efr=" + efr + ", situacao="
				+ situacao + ", data_situacao=" + data_situacao + ", motivo_situacao=" + motivo_situacao
				+ ", situacao_especial=" + situacao_especial + ", data_situacao_especial=" + data_situacao_especial
				+ ", capital_social=" + capital_social + ", qsa=" + qsa + ", extra=" + extra + ", file=" + file
				+ ", dataConsulta=" + dataConsulta + ", buscouDaReceita=" + buscouDaReceita + "]";
	}
	
	

}
