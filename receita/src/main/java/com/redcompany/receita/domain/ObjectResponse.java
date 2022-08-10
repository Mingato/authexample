package com.redcompany.receita.domain;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ObjectResponse {
	private Meta meta;
	
	private List<Object> data;
	
	public ObjectResponse(List<Object> objects){
		data = objects;
		meta = new Meta(data.size());
	}
	
	public ObjectResponse(List<Object> objects, Meta meta){
		data = objects;
		meta.setTotal(objects.size());
		this.meta = meta;		
	}
	
	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}
}
