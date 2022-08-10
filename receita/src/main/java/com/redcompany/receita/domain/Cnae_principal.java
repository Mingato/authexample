package com.redcompany.receita.domain;

public class Cnae_principal{
	
	private String code;
	
	private String text;

	
	public Cnae_principal() {
		super();			
	}
	
	public Cnae_principal(String code, String text) {
		super();
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
			
}
