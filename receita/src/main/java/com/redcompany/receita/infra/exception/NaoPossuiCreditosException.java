package com.redcompany.receita.infra.exception;

/**
 * Created by vntclol on 02/10/2014.
 */
public class NaoPossuiCreditosException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4L;

    public NaoPossuiCreditosException() {
        super("Usuario nao possui creditos o suficiente para a consulta.");
        System.out.println("NaoPossuiCreditosException");
    }

}
