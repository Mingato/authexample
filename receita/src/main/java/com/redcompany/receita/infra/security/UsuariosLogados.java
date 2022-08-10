package com.redcompany.receita.infra.security;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.redcompany.receita.domain.Usuario;
import com.redcompany.receita.domain.servico.ServicoUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by vntclol on 30/09/2014.
 */
@Component
public class UsuariosLogados {

    @Autowired
    private ServicoUsuario servicoUsuario;

    private final List<Usuario> logados = Lists.newArrayList();

    public void login(Usuario usuario){
    	usuario = servicoUsuario.buscarPorId(usuario.getId());
        this.logados.add(usuario);
    }

    public List<Usuario> logados(){
        HashSet<Usuario> usuarios = Sets.newHashSet(this.logados);
        return Lists.newArrayList(usuarios);
    }

    public void logout(String usuarioLogoutId){
        Iterator<Usuario> usuarioIterator = logados.iterator();
        while (usuarioIterator.hasNext()){
            Usuario usuario = usuarioIterator.next();
            if(usuario.getId().equals(usuarioLogoutId)){
                usuarioIterator.remove();
                break;
            }
        }
    }

    public boolean estaLogado(String idUsuario){
        Iterator<Usuario> usuarioIterator = logados.iterator();
        while (usuarioIterator.hasNext()){
            Usuario usuario = usuarioIterator.next();
            if(usuario.getId().equals(idUsuario)){
                return true;
            }
        }
        return false;
    }

    public Usuario seleciona(String idUsuario){
        for (Usuario usuario : servicoUsuario.buscaTodos()) {
            if(usuario.getId().equals(idUsuario)){
                return usuario;
            }
        }
        throw new RuntimeException("Usuario nao logado");
    }

}
