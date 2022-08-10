package com.redcompany.receita.domain;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UsuariosResponse {
	
	private Meta meta;
	
	private List<Usuario> data;
	
	public UsuariosResponse(List<Usuario> usuarios){
		data = usuarios;
		meta = new Meta(data.size());
	}
	
	public UsuariosResponse(List<Usuario> usuarios, Meta meta){
		data = usuarios;
		meta.setTotal(usuarios.size());
		this.meta = meta;		
	}
	
	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public List<Usuario> getData() {
		return data;
	}

	public void setData(List<Usuario> data) {
		this.data = data;
	}

}
