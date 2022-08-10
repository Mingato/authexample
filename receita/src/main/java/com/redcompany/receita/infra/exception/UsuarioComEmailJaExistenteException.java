package com.redcompany.receita.infra.exception;

/**
 * Created by vntclol on 02/10/2014.
 */
public class UsuarioComEmailJaExistenteException extends RuntimeException {

	private static final long serialVersionUID = 8520065334622549473L;

	private final String email;

    public UsuarioComEmailJaExistenteException(String email) {
        super(String.format("Usuario com email %s já cadastrado",email));
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
