package com.redcompany.receita.infra.exception;

/**
 * Created by vntclol on 02/10/2014.
 */
public class UsuarioJaExistenteException extends RuntimeException {

	private static final long serialVersionUID = 6278640355706219601L;

	private final String usuario;

    public UsuarioJaExistenteException(String usuario) {
        super("Usuario já existente.");
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

}
