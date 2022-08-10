package com.redcompany.receita.infra.exception;

public class TokenInvalidoException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;	
	
	public TokenInvalidoException() {
        super("token invalido, faça o login novamente.");        
    }	
    
}