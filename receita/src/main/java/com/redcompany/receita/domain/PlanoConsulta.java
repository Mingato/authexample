package com.redcompany.receita.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class PlanoConsulta {

	private TipoPlano tipoPlano;
	
	private int quantidadeRestante;
	
	private int quantidade;
	
	private int diasRestantes;
	
	private int dias;

	public PlanoConsulta(TipoPlano tipoPlano, int quantidade, int dias) {
		super();
		this.tipoPlano = tipoPlano;
		this.quantidadeRestante = quantidade;
		this.quantidade = quantidade;
		this.diasRestantes = dias;
		this.dias = dias;
	}

	public TipoPlano getTipoPlano() {
		return tipoPlano;
	}

	public void setTipoConsulta(TipoPlano tipoPlano) {
		this.tipoPlano = tipoPlano;
	}

	public int getQuantidadeRestante() {
		return quantidadeRestante;
	}

	public void setQuantidadeRestante(int quantidadeRestante) {
		this.quantidadeRestante = quantidadeRestante;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public int getDiasRestantes() {
		return diasRestantes;
	}

	public void setDiasRestantes(int diasRestantes) {
		this.diasRestantes = diasRestantes;
	}

	public int getDias() {
		return dias;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}
	
	
	public static enum TipoPlano{
		CNPJ, CPF, CEP
	}
}
