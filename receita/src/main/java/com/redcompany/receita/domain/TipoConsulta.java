package com.redcompany.receita.domain;

public enum TipoConsulta {
	RECEITA("receita"), 
	SINTEGRA("sintegra"), 
	SIMPLES("simples"), 
	SUFRAMA("suframa"),
	CPF("cpf");
	
	private final String tipo;

    private TipoConsulta(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
    
}
