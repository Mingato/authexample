package com.redcompany.receita.domain.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.redcompany.receita.domain.Perfil;
import com.redcompany.receita.domain.Usuario;
import com.redcompany.receita.infra.exception.UsuarioComEmailJaExistenteException;


@Component
public class ServicoDeCriacaoUsuario {

	@Autowired
	private ServicoUsuario servicoUsuario;

	@Autowired
	private ServicoDeToken servicoDeToken;


    public Usuario novoUsuario(Usuario usuario) {
        if (servicoUsuario.existeUsuarioEmail(usuario.getEmail())) {
            throw new UsuarioComEmailJaExistenteException(usuario.getEmail());
        }
        
        usuario.setId(null);
        usuario.setSenha(passwordEncoder().encode(usuario.getSenha()));
        
        usuario = servicoUsuario.save(usuario);
        
        servicoDeToken.gerarNovoToken(usuario);
        
        return usuario;
    }

    

	public Usuario alterarOutroUsuario(Usuario usuario) {
		System.out.println("alterarOutroUsuario");
        if (usuario.getId() != null) {
        	Usuario usuarioAlterado = servicoUsuario.buscarPorId(usuario.getId());
        	
        	usuarioAlterado.setEmail(usuario.getEmail());
        	usuarioAlterado.setNome(usuario.getNome());
        	usuarioAlterado.setRazaoSocial(usuario.getRazaoSocial());
        	usuarioAlterado.setPerfil(usuario.getPerfil());        	
        	usuarioAlterado.setTelefone(usuario.getTelefone());
        	
        	usuarioAlterado.setSenha(passwordEncoder().encode(usuario.getSenha()));
    
            return servicoUsuario.save(usuarioAlterado);
        }
        
        return null;
    }
    
    
    public Usuario alterarProprioUsuario(Usuario usuario) {
        if (usuario.getId() != null) {
        	Usuario usuarioAlterado = servicoUsuario.buscarPorId(usuario.getId());        	
        	
        	usuarioAlterado.setNome(usuario.getNome());
        	usuarioAlterado.setEmail(usuario.getEmail());
        	usuarioAlterado.setRazaoSocial(usuario.getRazaoSocial());
        	usuarioAlterado.setTelefone(usuario.getTelefone());
        	usuario.setSenha(passwordEncoder().encode(usuario.getSenha()));
        	
            return servicoUsuario.save(usuarioAlterado);
        }
        
        return null;
    }

    public void removeUsuario(String id) {        	
        servicoUsuario.remove(id);              
    }
    
    public Usuario alteraUsuario(Usuario usuario, Usuario usuarioLogado) {
        if (usuario.getId() != null) {
            if(usuarioLogado.getId().equals(usuario.getId()) || 
               usuarioLogado.getPerfil().equalsIgnoreCase(Perfil.REPRESENTANTE.toString())){
            	
            	Usuario usuarioAlterado = servicoUsuario.buscarPorId(usuario.getId());        	
            	
            	usuarioAlterado.setEmail(usuario.getEmail());
            	usuarioAlterado.setNome(usuario.getNome());
            	usuarioAlterado.setRazaoSocial(usuario.getRazaoSocial());
            	usuarioAlterado.setPerfil(usuario.getPerfil());            	
            	usuarioAlterado.setTelefone(usuario.getTelefone());
            	
            	usuario = servicoUsuario.save(usuarioAlterado);
            }
        }
        return usuario;
    }

    public void removeUsuario(Usuario usuario, Usuario usuarioLogado) {
        if (usuario.getId() != null) {
        	if(usuarioLogado.getPerfil().equalsIgnoreCase(Perfil.ADMINISTRADOR.toString())){
        		servicoUsuario.remove(usuario.getId());
        	}
        }
    }

    public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
}
