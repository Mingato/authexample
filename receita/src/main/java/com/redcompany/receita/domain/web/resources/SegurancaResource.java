package com.redcompany.receita.domain.web.resources;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.redcompany.receita.domain.Usuario;
import com.redcompany.receita.domain.servico.ServicoSeguranca;
import com.redcompany.receita.domain.servico.ServicoUsuario;
import com.redcompany.receita.infra.util.PegarUsernameDoHead;


@Controller
@RequestMapping(value = "/seguranca")
public class SegurancaResource {
	
	@Autowired
	private ServicoUsuario servicoUsuario;
	
	@Autowired
	private ServicoSeguranca servicoSeguranca;
	
	@Autowired
    private PegarUsernameDoHead pegarUsernameDoHead; 
	

	@RequestMapping(value = "/senha/esqueci", method = RequestMethod.POST)
    public ResponseEntity<String> esqueci(@RequestBody String email){
		if(servicoSeguranca.esqueciSenha(email)){
			return new ResponseEntity<>("", HttpStatus.OK);	
		}
		
		return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
    }
	
	@RequestMapping(value = "/senha/trocar/{codigo}", method = RequestMethod.PUT)
    public ResponseEntity<String> trocar(@PathVariable("codigo") String codigo, @RequestBody String password){
		if(servicoSeguranca.alterarSenha(codigo, password)){
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		
		return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }
	//   seguranca/senha/atualizar/
	
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
