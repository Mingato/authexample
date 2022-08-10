package com.redcompany.receita.domain;

public class Atividade {

	private String text;
	
	private String code;

		
	
	public Atividade(String text, String code) {
		super();
		this.text = text;
		this.code = code;
	}
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
