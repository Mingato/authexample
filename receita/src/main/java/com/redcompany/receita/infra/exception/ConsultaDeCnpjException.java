package com.redcompany.receita.infra.exception;

/**
 * Created by vntclol on 02/10/2014.
 */
public class ConsultaDeCnpjException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String status;
	private final String mensagem;

    public ConsultaDeCnpjException(String status, String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
        this.status = status;
    }

	public String getStatus() {
		return status;
	}

	public String getMensagem() {
		return mensagem;
	}

}
