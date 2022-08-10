package com.redcompany.receita.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value= "saldo_sintegra")
public class SaldoSintegra {

	@Id
	private Long data;
	
	private String qtd_consultas_disponiveis;

	public SaldoSintegra(Long data, String qtd_consultas_disponiveis) {
		super();
		this.qtd_consultas_disponiveis = qtd_consultas_disponiveis;
		this.data = data;
	}

	public SaldoSintegra() {

	}

	public String getSaldo() {
		return qtd_consultas_disponiveis;
	}

	public void setSaldo(String qtd_consultas_disponiveis) {
		this.qtd_consultas_disponiveis = qtd_consultas_disponiveis;
	}

	public Long getData() {
		return data;
	}

	public void setData(Long data) {
		this.data = data;
	}
	
}
