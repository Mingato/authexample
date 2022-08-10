package com.redcompany.receita.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "codigoDeSeguranca")
public class CodigoDeSeguranca {
	
	@Id
	private String id;
	
	private Usuario usuario;
	
	private Tipo tipo;
	
	private Long dataVencimento; 
	
	private Boolean valido;
	
	public CodigoDeSeguranca(){
		this.valido = true; 
	}
	
	public CodigoDeSeguranca(String id, Usuario usuario, Tipo tipo, Long dataVencimento) {
		super();		
		this.id = id;
		this.usuario = usuario;
		this.tipo = tipo;
		this.dataVencimento = dataVencimento;
		this.valido = true;
	}


	public String getCodigo() {
		return id;
	}

	public void setCodigo(String id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Long getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Long dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public Boolean getValido() {
		return valido;
	}

	public void setValido(Boolean valido) {
		this.valido = valido;
	}


	public static enum Tipo { 
		ALTERA_SENHA; 
	};
	
	 
}
