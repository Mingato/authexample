package com.redcompany.receita.infra.schedules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.redcompany.receita.domain.servico.ServicoReceita;
import com.redcompany.receita.domain.servico.ServicoUsuario;

@Component
public class VerificadorDeCreditos {
    
    @Autowired
	private ServicoReceita servicoReceita;
    
    @Autowired
	private ServicoUsuario servicoUsuario;

    @Scheduled(cron = "0 0 0 * * *")
    public void process() {
    	System.out.println("Init schedule");
    	servicoReceita.consultarSaldo();
    	
    	servicoUsuario.renovarCreditosDosPLanos();
    	
    }
    
}