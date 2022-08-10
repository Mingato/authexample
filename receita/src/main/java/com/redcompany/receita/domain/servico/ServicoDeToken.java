package com.redcompany.receita.domain.servico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.redcompany.receita.domain.Token;
import com.redcompany.receita.domain.Usuario;
import com.redcompany.receita.domain.repositorio.RepositorioDeToken;

@Component
public class ServicoDeToken {

	@Autowired 
	private RepositorioDeToken repositorioDeToken; 
	
	
	public Token gerarNovoToken(Usuario usuario) {
    	String tokenString = passwordEncoder().encode(usuario.getId() + "" + usuario.getNome() + "" + usuario.getEmail() + usuario.getSenha());
    	
    	tokenString = tokenString.replaceAll("/", "A");
    	tokenString = tokenString.replace('$', 'G');
    	tokenString = tokenString.replace('.', 'U');
    	
    	Token token = repositorioDeToken.save(new Token(usuario.getId(), tokenString));
    	
    	return token;
	}
	
	public boolean validarToken(String token) {
    	if(repositorioDeToken.findByToken(token).isPresent()) {
    		return true;
    	}
    	
    	return false;
    }
    
    public Token buscarTokenByIdUsuario(String idUsuario) {
    	Optional<Token> token = repositorioDeToken.findByIdUsuario(idUsuario);
    	
    	if(token.isPresent()) {
    		return token.get();
    	}
    	
    	return null;
    }
    
    public Token buscarTokenByToken(String token) {
    	Optional<Token> tokenAcessoApi = repositorioDeToken.findByToken(token);
    	
    	if(tokenAcessoApi.isPresent()) {
    		return tokenAcessoApi.get();
    	}
    	
    	return null;
    }
	
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
}
