package com.redcompany.receita.infra.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.redcompany.receita.domain.Result;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	
    @ExceptionHandler(value = {UsuarioNaoExistenteException.class, ConsultaDeCnpjException.class, QtdConsultasNaoExistenteException.class})
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        
    	Result result = new Result();
    	String bodyOfResponse = ex.getMessage();
    	result.setStatus("ERROR");
    	result.setMessage(bodyOfResponse);
    	result.setReceita(null);
    	result.setSintegra(null);
    	result.setSimples(null);
    	result.setSuframa(null);
        LOGGER.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, result, new HttpHeaders(), HttpStatus.OK, request);
    }

    @ExceptionHandler(value = { RuntimeException.class, PaserErrorException.class})
    protected ResponseEntity<Object> handleInternalServerError(RuntimeException ex, WebRequest request) {
    	Result result = new Result();
    	String bodyOfResponse = ex.getMessage();
    	result.setStatus("ERROR");
    	result.setMessage(bodyOfResponse);
    	result.setReceita(null);
    	result.setSintegra(null);
    	result.setSimples(null);
    	result.setSuframa(null);
        LOGGER.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, result, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = { UsuarioJaExistenteException.class, UsuarioComEmailJaExistenteException.class, NaoPossuiCreditosException.class})
    protected ResponseEntity<Object> handleInternalBadRequest(RuntimeException ex, WebRequest request) {
    	Result result = new Result();
    	String bodyOfResponse = ex.getMessage();
    	result.setStatus("ERROR");
    	result.setMessage(bodyOfResponse);
    	result.setReceita(null);
    	result.setSintegra(null);
    	result.setSimples(null);
    	result.setSuframa(null);
        LOGGER.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, result, new HttpHeaders(), HttpStatus.OK, request);
    }
    
    @ExceptionHandler(value = { TokenInvalidoException.class})
    protected ResponseEntity<Object> handleTokenInvalido(RuntimeException ex, WebRequest request) {
    	Result result = new Result();
    	String bodyOfResponse = ex.getMessage();
    	result.setStatus("ERROR");
    	result.setMessage(bodyOfResponse);
    	result.setReceita(null);
    	result.setSintegra(null);
    	result.setSimples(null);
    	result.setSuframa(null);
        LOGGER.error(bodyOfResponse, ex);
        return handleExceptionInternal(ex, result, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

}
