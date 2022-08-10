package com.redcompany.receita.domain.web.resources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Strings;
import com.redcompany.receita.domain.Meta;
import com.redcompany.receita.domain.Perfil;
import com.redcompany.receita.domain.Plano;
import com.redcompany.receita.domain.Result;
import com.redcompany.receita.domain.S3File;
import com.redcompany.receita.domain.Usuario;
import com.redcompany.receita.domain.UsuariosResponse;
import com.redcompany.receita.domain.servico.ServicoDeCriacaoUsuario;
import com.redcompany.receita.domain.servico.ServicoUsuario;
import com.redcompany.receita.infra.util.PegarUsernameDoHead;

/**
 * Created by vntclol on 30/09/2014.
 */
@Controller
@RequestMapping(value = "/api/usuario")
public class UsuarioResource {

    @Autowired
    private ServicoUsuario servicoUsuario;

    @Autowired
    private ServicoDeCriacaoUsuario servicoDeCriacaoUsuario;

    @Autowired
    private PegarUsernameDoHead pegarUsernameDoHead; 
    
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Usuario> obter(@PathVariable("id") String id){
    	Usuario usuario = servicoUsuario.buscarPorId(id);
    	if(usuario != null){
    		return new ResponseEntity<>(usuario, HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(usuario, HttpStatus.NOT_FOUND);
    }
    

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "/adicionar",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Usuario> adiciona(@RequestBody Usuario usuario){
    	usuario = servicoDeCriacaoUsuario.novoUsuario(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "/alterar",method = RequestMethod.PUT,consumes = "application/json")
    public ResponseEntity<Usuario> altera(@RequestBody Usuario usuario){
    	System.out.println("alterando usuario . . .");
        usuario = servicoDeCriacaoUsuario.alterarOutroUsuario(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @RequestMapping(value = "/seAlterar",method = RequestMethod.POST,consumes = "application/json")
    public ResponseEntity<Usuario> seAltera(@RequestBody Usuario usuario, @RequestHeader("Authorization") String token){
    	String email = pegarUsernameDoHead.pegarUsername(token);
    	Usuario usuarioLogado = servicoUsuario.buscaPorEmail(email);
    	//if(usuarioLogado.getId().equals(usuario.getId())){
    	usuario.setId(usuarioLogado.getId());
    	usuario.setPerfil(usuarioLogado.getPerfil());
		usuario = servicoDeCriacaoUsuario.alterarProprioUsuario(usuario);
		return new ResponseEntity<>(usuario, HttpStatus.OK);
    	//}
    	
    	//return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }
    
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "/deletar/{id}",method = RequestMethod.DELETE)    
    public ResponseEntity<Boolean> remove(@PathVariable("id") String id){
    	if (!Strings.isNullOrEmpty(id)) {
    		servicoDeCriacaoUsuario.removeUsuario(id);
    		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    	}
    	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/detalhe",method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Usuario> detalhe (@RequestBody Usuario usuario){
        return new ResponseEntity<>(servicoUsuario.buscarPorId(usuario.getId()), HttpStatus.OK);
    }
        
	@RequestMapping(value = "/infoPessoais",method = RequestMethod.POST)
    public ResponseEntity<Usuario> buscaInformacoesPessoais(@RequestHeader("Authorization") String token){
    	String  email = pegarUsernameDoHead.pegarUsername(token);
        return new ResponseEntity<>(servicoUsuario.buscaPorEmail(email), HttpStatus.OK);
    }

    @RequestMapping(value = "/todos",method = RequestMethod.GET)
    public ResponseEntity<List<Usuario>> todos(){
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(servicoUsuario.buscaTodos());
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/todos",method = RequestMethod.POST)
    public ResponseEntity<List<Usuario>> todosPOST(){
        List<Usuario> todos = new ArrayList<>();
        todos.addAll(servicoUsuario.buscaTodos());
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/senha/alterar", method = RequestMethod.POST)
    public ResponseEntity<String> alerar(@RequestParam(value = "novaSenha", required=true) String novaSenha, @RequestHeader("Authorization") String token){
    	String email = pegarUsernameDoHead.pegarUsername(token);
		Usuario logado = servicoUsuario.buscaPorEmail(email);
		logado = servicoUsuario.buscarPorId(logado.getId());
		
		logado.setSenha(passwordEncoder().encode(novaSenha));
	
		servicoUsuario.save(logado);
		System.out.println("Senha trocada");
		return new ResponseEntity<>("", HttpStatus.OK);
    }
    
    @RequestMapping(value="/upload-imagem", method=RequestMethod.POST)
    public ResponseEntity<Usuario> examplePost(@RequestBody MultipartFile file, @RequestHeader("Authorization") String token) throws IOException{
    	String email = pegarUsernameDoHead.pegarUsername(token);
    	Usuario usuario = servicoUsuario.buscaPorEmail(email);
        S3File imagem = new S3File();
        if (file != null) {
            File convFile = S3File.convert(file, "imgPerfil/" + usuario.getId() + ".jpg");
            imagem = S3File.create(usuario.getId() + ".jpg", convFile.getPath(), file.getContentType(), "imgPerfil");
            servicoUsuario.atualizaImagem(usuario.getId(), imagem);
        }

        return new ResponseEntity<>(servicoUsuario.buscarPorId(usuario.getId()), HttpStatus.OK);
    }

	    
	@RequestMapping(value = "/todos/detalhe",method = RequestMethod.POST)
	public ResponseEntity<UsuariosResponse> todosDetalhePOST(){
		List<Usuario> todos = new ArrayList<>();
		todos.addAll(servicoUsuario.buscaTodos());
		UsuariosResponse usuarios = new UsuariosResponse(todos);
		
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/todos/detalhe",method = RequestMethod.GET)
	public ResponseEntity<UsuariosResponse> todosDetalheGET(){
		List<Usuario> todos = new ArrayList<>();
		todos.addAll(servicoUsuario.buscaTodos());        
		UsuariosResponse usuarios = new UsuariosResponse(todos);
		
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////// PLANOS ////////////////////////////////////////////////////////////////////////
	
	@RequestMapping(value = "/planos", method = RequestMethod.GET)
	public ResponseEntity<Plano> getPlano(@RequestHeader("Authorization") String token){
		String email = pegarUsernameDoHead.pegarUsername(token);
		Usuario usuario = servicoUsuario.buscaPorEmail(email);
		
		Plano qtdConsultas = servicoUsuario.getPlanoPorIdUsuario(usuario.getId());
		
		return new ResponseEntity<>(qtdConsultas, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	@RequestMapping(value = "/planos/{id}", method = RequestMethod.GET)
	public ResponseEntity<Plano> getPlanos(@PathVariable("id") String id){
		Usuario usuario = servicoUsuario.buscarPorId(id);
		
		Plano qtdConsultas = servicoUsuario.getPlanoPorIdUsuario(usuario.getId());
		
		return new ResponseEntity<>(qtdConsultas, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	@RequestMapping(value = "/planos/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Plano> alterarPlanos(@PathVariable("id") String id, @RequestBody Plano qtdConsultas){
		qtdConsultas = servicoUsuario.alterartQuantidadeDeConsultas(qtdConsultas);
		
		return new ResponseEntity<>(qtdConsultas, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMINISTRADOR')")
	@RequestMapping(value = "/planos/renovar/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Result> renovarPlanos(@PathVariable("id") String id){
		servicoUsuario.renovarCreditosDoPLanoPorUsuario(id);
		
		Result result = new Result("OK", "Planos Renovados");
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////METRONIC//////////////////////////////

	@RequestMapping(value = "/todos/detalhe/filtro",method = RequestMethod.POST)
	public ResponseEntity<UsuariosResponse> todosDetalhePOST(@RequestBody String str){
		List<Usuario> todos = new ArrayList<>();        
		
		Meta meta = servicoUsuario.strToMeta(str);
		
		todos.addAll(servicoUsuario.buscaCompleta(meta));
		
		UsuariosResponse usuarios = new UsuariosResponse(todos, meta);
		
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/filtro",method = RequestMethod.POST)
	public ResponseEntity<UsuariosResponse> detalhePOST(@RequestBody Meta meta){
		List<Usuario> todos = new ArrayList<>();        
				
		todos.addAll(servicoUsuario.buscaCompleta(meta));
		
		UsuariosResponse usuarios = new UsuariosResponse(todos, meta);
		
		return new ResponseEntity<>(usuarios, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/perfis", method = RequestMethod.GET)
    public ResponseEntity<String> obterPerfil(){
        return new ResponseEntity<>(Perfil.getPerfisJSON(), HttpStatus.OK);
    }
	
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
    
	
}
