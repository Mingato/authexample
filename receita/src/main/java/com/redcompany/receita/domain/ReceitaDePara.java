package com.redcompany.receita.domain;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ReceitaDePara {
//TODO:
	@Id
	private String fornecedor;

	private String status;
	
	private String message;
	
	private String cnpj;
	
	private String tipo;//matriz, filial
	
	private String abertura;//abertura
	
	private String nome;//razaoSocial
	
	private String fantasia;
	
	private ArrayList<Atividade> atividade_principal;
	
	private ArrayList<Atividade> atividades_secundarias;
	
	private String natureza_juridica;
	
	private String logradouro;
	
	private String numero;
	
	private String complemento;
	
	private String cep;
	
	private String bairro;
	
	private String municipio;
	
	private String uf;
	
	private String email;
	
	private String telefone;
	
	private String efr;
	
	private String situacao;
	
	private String data_situacao;
	
	private String motivo_situacao;
	
	private String situacao_especial;
	
	private String data_situacao_especial;
	
	private String capital_social;
	
	private ArrayList<Qsa> qsa;
		
	private Object extra;
	
	
	
}
