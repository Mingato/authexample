package com.redcompany.receita.infra.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.redcompany.receita.domain.Usuario;
import com.redcompany.receita.domain.repositorio.RepositorioDeUsuario;

@Service
public class AppUsersDetailsService implements UserDetailsService {

	@Autowired
	private RepositorioDeUsuario repositorioDeUsuario;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = repositorioDeUsuario.findByEmail(email);
		if (usuario == null) {
			throw new UsernameNotFoundException("Email inválido");
		}
		return new User(usuario.getEmail(), usuario.getSenha(), getPermissoes(usuario));
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(usuario.getPerfil()));
		return authorities;
	}

}
