package com.redcompany.receita.infra.exception;

/**
 * Created by vntclol on 02/10/2014.
 */
public class QtdConsultasNaoExistenteException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String usuario;

    public QtdConsultasNaoExistenteException(String usuario) {
        super("quantidade de consultas não existe para o usuário.");
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

}
