package com.redcompany.receita.domain.servico;

import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.redcompany.receita.domain.CodigoDeSeguranca;
import com.redcompany.receita.domain.Usuario;
import com.redcompany.receita.domain.repositorio.RepositorioDeCodigoDeSeguranca;
import com.redcompany.receita.domain.repositorio.RepositorioDeUsuario;


@Component
public class ServicoSeguranca {
	
	private RepositorioDeCodigoDeSeguranca repositorioDeSeguranca;
	
	private RepositorioDeUsuario repositorioDeUsuario;
    
	
	@Autowired
    public ServicoSeguranca(RepositorioDeCodigoDeSeguranca repositorioDeSeguranca,
    		RepositorioDeUsuario repositorioDeUsuario) {
        this.repositorioDeSeguranca = repositorioDeSeguranca;
        this.repositorioDeUsuario = repositorioDeUsuario;
    }

	Logger sampLogger = Logger.getLogger("SampleServletFilter");
    
    /*Cria um codigo de seguranca e envia para o email como parametro
     * um link para se alterar a senha*/
    public boolean esqueciSenha(String email){
    	Usuario usuario = repositorioDeUsuario.findByEmail(email);
    	if(usuario != null){
    		CodigoDeSeguranca codigoDeSeguranca = criarCodigoDeSeguranca(usuario);
    		    
    		return enviarEmail(codigoDeSeguranca);
    	}
    	
    	return false;
    }
    
    public boolean alterarSenha(String codigo, String password){	
		Optional<CodigoDeSeguranca> codigoDeSeguranca = repositorioDeSeguranca.findById(codigo);
		CodigoDeSeguranca codSeg = codigoDeSeguranca.get();
		
		if(codSeg != null){
			if(codSeg.getValido()){
				if(codSeg.getTipo().equals(CodigoDeSeguranca.Tipo.ALTERA_SENHA)){
					if(codSeg.getDataVencimento() > (new Date().getTime())){
						sampLogger.log(Level.INFO, "REDCOMPANY SEGURANCA: data correta");
						Usuario usuario = repositorioDeUsuario.findByEmail(codSeg.getUsuario().getEmail());
						usuario.setSenha(passwordEncoder().encode(password));
						repositorioDeUsuario.save(usuario);
						sampLogger.log(Level.INFO, "REDCOMPANY SEGURANCA: senha alterada");
						
						codSeg.setValido(false);
						repositorioDeSeguranca.save(codSeg);
						sampLogger.log(Level.INFO, "REDCOMPANY SEGURANCA: codigodeseguranca alterado");
						
						return true;
					}
				}
			}
		}
    	
    	return false;
    }
    
    
    private boolean enviarEmail(CodigoDeSeguranca codigoDeSeguranca) {
    	try
	    {
    		final String fromEmail = "gunter@redcompany.com.br"; //requires valid gmail id
    		final String password = "3war4Ret"; // correct password for gmail id
    		final String toEmail = codigoDeSeguranca.getUsuario().getEmail(); // can be any email id 
    		
    		System.out.println("TLSEmail Start");
    		Properties props = new Properties();
    		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
    		props.put("mail.smtp.port", "587"); //TLS Port
    		props.put("mail.smtp.auth", "true"); //enable authentication
    		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
    		
                    //create Authenticator object to pass in Session.getInstance argument
    		Authenticator auth = new Authenticator() {
    			//override the getPasswordAuthentication method
    			protected PasswordAuthentication getPasswordAuthentication() {
    				return new PasswordAuthentication(fromEmail, password);
    			}
    		};
    		Session session = Session.getInstance(props, auth);
    		
    		MimeMessage msg = new MimeMessage(session);
  	      	//set message headers
  	      	msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
  	      	msg.addHeader("format", "flowed");
  	      	msg.addHeader("Content-Transfer-Encoding", "8bit");
  	      	msg.setFrom(new InternetAddress(fromEmail, "REDCOMPANY"));
  	      	msg.setReplyTo(InternetAddress.parse(toEmail, false));

  	      	msg.setSubject("WM3 - ESQUECEU A SENHA", "UTF-8");

  	      	msg.setText("acesse o link para alterar a senha\n\n"+
  	      				"http://ec2-52-12-253-165.us-west-2.compute.amazonaws.com:8080/alteracaoDeSenha.html?codigo="+
  	      				codigoDeSeguranca.getCodigo(), "UTF-8");

  	      	msg.setSentDate(new Date());

  	      	msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
  	      	System.out.println("Message is ready");
      	  	Transport.send(msg); 
    		
      	  	return true;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
    	    
        return false;
	}


	private CodigoDeSeguranca criarCodigoDeSeguranca(Usuario usuario){
    	CodigoDeSeguranca codigo = new CodigoDeSeguranca();
		codigo.setCodigo(null);
		codigo.setTipo(CodigoDeSeguranca.Tipo.ALTERA_SENHA);
		codigo.setUsuario(usuario);
		codigo.setValido(true);
		codigo.setDataVencimento(addDays(new Date().getTime(), 1));
		
		return repositorioDeSeguranca.save(codigo);
    }
    
    private static Long addDays(Long timestamp, int days){
    	timestamp +=  days * 24 * 3600 * 1000;     	
        return timestamp;
    }
    
    public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
