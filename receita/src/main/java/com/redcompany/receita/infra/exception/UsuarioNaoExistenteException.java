package com.redcompany.receita.infra.exception;

/**
 * Created by vntclol on 02/10/2014.
 */
public class UsuarioNaoExistenteException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String usuario;

    public UsuarioNaoExistenteException(String usuario) {
        super("Usuario nao existente.");
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

}
