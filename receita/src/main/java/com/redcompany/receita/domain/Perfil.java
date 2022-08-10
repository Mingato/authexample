package com.redcompany.receita.domain;


public enum Perfil {

	ADMINISTRADOR("ADMINISTRADOR"),
	
    REPRESENTANTE("REPRESENTANTE"),

    USUARIO("USUARIO");

    private final String nome;

    private Perfil(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    
    public static String getPerfisJSON(){
    	return "[{'perfil':'ADMINISTRADOR'}" + 
    		   ",{'perfil':'USUARIO'} " +
    		   ",{'perfil':'REPRESENTANTE'}]";    	
    }

}
