package com.redcompany.receita.domain.web.resources;

import com.google.common.base.Strings;
import com.redcompany.receita.domain.*;
import com.redcompany.receita.domain.servico.ServicoDeToken;
import com.redcompany.receita.domain.servico.ServicoReceita;
import com.redcompany.receita.domain.servico.ServicoUsuario;
import com.redcompany.receita.infra.util.PegarUsernameDoHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vntclol on 30/09/2014.
 */
@Controller
@RequestMapping(value = "/api/receita")
public class ReceitaResource {

    @Autowired
    private ServicoReceita servicoReceita;
    
    @Autowired
    private PegarUsernameDoHead pegarUsernameDoHead; 
    
    @Autowired
    private ServicoUsuario servicoUsuario;    
    
    @Autowired
	private ServicoDeToken servicoDeToken;
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<Receita> tesGet(){
    	System.out.println("GET /test");
    	return new ResponseEntity<>(new Receita("adas", "", "", "", "",null, null, "", "", "", "", "", "",
    			"", "", "", "", "", "", "", "", "", "", "", null, null, ""), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/teste", method = RequestMethod.GET)
    public ResponseEntity<String> testGet(){
    	System.out.println("GET /teste");
    	String retorno = "Bem vindo absdiuabhs";
    	return new ResponseEntity<>(retorno, HttpStatus.OK);
    }

    @RequestMapping(value = "/teste/{str}", method = RequestMethod.GET)
    public ResponseEntity<String> testeGet(@PathVariable("str") String str){
    	System.out.println("GET /teste/{str}");
    	String retorno = "Olá mudo: " + str;
    	return new ResponseEntity<>(retorno, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/teste/{str}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<String> testePost(@PathVariable("str") String str){
    	System.out.println("POST /teste/{str}");
    	String retorno = "Olá mudo: " + str;
    	return new ResponseEntity<>(retorno, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Receita> obter(@PathVariable("id") String id){
    	Receita receita = servicoReceita.buscarPorId(id);
    	if(receita != null){
    		return new ResponseEntity<>(receita, HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(receita, HttpStatus.NOT_FOUND);
    }
    
    
    @RequestMapping(value = "/cnpj/{cnpj}", method = RequestMethod.GET)
    public ResponseEntity<Result> buscarPorCnpj(
    		@PathVariable("cnpj") String cnpj,
    		@RequestParam(value = "token", required=true) String tokenAcessoApi,
    		@RequestParam(value = "tp_consulta", required=false) String tp_consulta,
    		@RequestParam(value = "source", required=false) String source,
    		@RequestParam(value = "consulta_max_age", required=false) String consulta_max_age){ //refresh: flag para fazer nova consulta na receita
    		//@RequestHeader("Authorization") String token){
    	
    	//String email = pegarUsernameDoHead.pegarUsername(token);
    	Usuario usuarioLogado = null;//servicoUsuario.buscaPorEmail(email);
    	if(source == null) {source = "";}
    	if(consulta_max_age == null) { consulta_max_age = "";}
    	
    	if(servicoDeToken.validarToken(tokenAcessoApi)) {
    		String id = servicoDeToken.buscarTokenByToken(tokenAcessoApi).getIdUsuario();
    		if(id != null) {
	    		usuarioLogado = servicoUsuario.buscarPorId(id);
	    		
	    		Result result = null;
		    	System.out.println("usuarioLogado.getId(): " + usuarioLogado.getId());
		    	    	
		    	if(tp_consulta == null) {
		    		result = servicoReceita.buscaCompletaPorCNPJ(cnpj, usuarioLogado.getId(), source, consulta_max_age);
		    	}else {
		    		result = servicoReceita.buscaPorCNPJ(cnpj, usuarioLogado.getId(), tp_consulta, source, consulta_max_age);
		    	}
		    	
		    	if(result != null){
		    		result = servicoReceita.setVerificarConsultasRealizadas(result, tp_consulta);
		    		return new ResponseEntity<>(result, HttpStatus.OK);
		    	}
		    	
		    	return new ResponseEntity<>(new Result("INFO","Não Conseguiu realizar Consultas!"), HttpStatus.OK);
		    
    		}
    	}
    	
    	return new ResponseEntity<>(new Result("ERROR","Token inválido!"), HttpStatus.OK);
    }

    @RequestMapping(value = "/cep/{cep}", method = RequestMethod.GET)
    public ResponseEntity<Cep> buscarPorCep(
    		@PathVariable("cep") String cep,
    		@RequestParam(value = "token", required=true) String tokenAcessoApi){
    	
    	Cep result = null;
    	
    	if(servicoDeToken.validarToken(tokenAcessoApi)) {
    		result = servicoReceita.buscarCep(cep);
	    	    	
	    	return new ResponseEntity<>(result, HttpStatus.OK);
    	}	    
    	
    	return new ResponseEntity<>(result, HttpStatus.OK);
    }
    

    @RequestMapping(value = "/cpf/{cpf}/{dataNascimento}", method = RequestMethod.GET)
    public ResponseEntity<Cpf> buscarPorCpf(
    		@PathVariable("cpf") String cpf,
    		@PathVariable("dataNascimento") String dataNascimento,
    		@RequestParam(value = "token", required=true) String tokenAcessoApi,
    		@RequestParam(value = "source", required=false) String source,
    		@RequestParam(value = "consulta_max_age", required=false) String consulta_max_age){ //refresh: flag para fazer nova consulta na receita
    		//@RequestHeader("Authorization") String token){
    	
    	//String email = pegarUsernameDoHead.pegarUsername(token);
    	Usuario usuarioLogado = null;//servicoUsuario.buscaPorEmail(email);
    	if(consulta_max_age == null) { consulta_max_age = "";}
    	if(source == null) {source = "";}
    	
    	if(servicoDeToken.validarToken(tokenAcessoApi)) {
    		usuarioLogado = servicoUsuario.buscarPorId(servicoDeToken.buscarTokenByToken(tokenAcessoApi).getIdUsuario());
    		
    		Cpf result = null;
	    	System.out.println("usuarioLogado.getId(): " + usuarioLogado.getId());
	    	    	
    		result = servicoReceita.buscarPorCpf(cpf, dataNascimento, usuarioLogado.getId(), source, consulta_max_age);
	    	
	    	
	    	if(result != null){
	    		return new ResponseEntity<>(result, HttpStatus.OK);
	    	}
	    	
	    	return new ResponseEntity<>(result, HttpStatus.OK);
    	}
    	
    	return new ResponseEntity<>(new Cpf("ERROR","Token inválido!"), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/consultas/{tempo}", method = RequestMethod.GET)
    public ResponseEntity<List<Consulta>> buscarConsultas(@PathVariable("tempo") Long tempo, @RequestHeader("Authorization") String token){
    	String email = pegarUsernameDoHead.pegarUsername(token);
    	Usuario usuario = servicoUsuario.buscaPorEmail(email);

    	return new ResponseEntity<>(servicoReceita.buscarConsultasRealizadas(usuario.getId(), tempo), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/consultas/{beginData}/{endData}", method = RequestMethod.GET)
    public ResponseEntity<List<Consulta>> buscarConsultas(
    		@PathVariable("beginData") String beginData,
    		@PathVariable("endData") String endData,
    		@RequestHeader("Authorization") String token){
    	
    	String email = pegarUsernameDoHead.pegarUsername(token);
    	Usuario usuarioLogado = servicoUsuario.buscaPorEmail(email);
    	
    	List<Consulta> consultas = servicoReceita.buscarConsultasRealizadas(usuarioLogado.getId(), beginData, endData);
    	
    	
    	return new ResponseEntity<>(consultas, HttpStatus.OK);
    }
    
    
    
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "/consultas/nome/{nome}/{tempo}", method = RequestMethod.GET)
    public ResponseEntity<List<Consulta>> buscarConsultasPorNome(
    		@PathVariable("nome") String nome,
    		@PathVariable("tempo") Long tempo,
    		@RequestHeader("Authorization") String token){
    	
    	System.out.println("buscarConsultasPorNome");
    	System.out.println("buscarConsultasPorNome " + nome);
    	Usuario usuario = servicoUsuario.buscaPorNome(nome);

    	System.out.println("Nome " + nome + " id: " + usuario.getEmail());
    	List<Consulta> consultas = servicoReceita.buscarConsultasRealizadas(usuario.getId(), tempo);
    	System.out.println("Consultas " + consultas.size());
    	
    	return new ResponseEntity<>(consultas, HttpStatus.OK);
    }
    
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "/consultas/nome/{nome}/{beginData}/{endData}", method = RequestMethod.GET)
    public ResponseEntity<List<Consulta>> buscarConsultasPorNome(
    		@PathVariable("nome") String nome,
    		@PathVariable("beginData") String beginData,
    		@PathVariable("endData") String endData,
    		@RequestHeader("Authorization") String token){
    	
    	Usuario usuario = servicoUsuario.buscaPorNome(nome);
    	
    	List<Consulta> consultas = servicoReceita.buscarConsultasRealizadas(usuario.getId(), beginData, endData);
    	
    	
    	return new ResponseEntity<>(consultas, HttpStatus.OK);
    }
    
    
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Receita> adiciona(@RequestBody Receita receita){
    	receita = servicoReceita.save(receita);
        return new ResponseEntity<>(receita, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = "application/json")
    public ResponseEntity<Receita> altera(@RequestBody Receita receita){
        receita = servicoReceita.save(receita);
        return new ResponseEntity<>(receita, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = "application/json")    
    public ResponseEntity<Boolean> remove(@PathVariable("id") String id){
    	if (!Strings.isNullOrEmpty(id)) {
    		servicoReceita.remove(id);
    		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    	}
    	return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Receita>> todos(){
        List<Receita> todos = new ArrayList<>();
        todos.addAll(servicoReceita.buscaTodos());
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
    
    
    @RequestMapping(value = "/token", method = RequestMethod.GET, consumes = "application/json")
    public ResponseEntity<Token> buscarToken(@RequestHeader("Authorization") String token){
    	String email = pegarUsernameDoHead.pegarUsername(token);
    	Usuario usuario = servicoUsuario.buscaPorEmail(email);
    	
    	Token tokenDeAcessoAPI = servicoDeToken.buscarTokenByIdUsuario(usuario.getId());
    	
        return new ResponseEntity<>(tokenDeAcessoAPI, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/token", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Token> gerarNovoToken(@RequestHeader("Authorization") String token){
    	String email = pegarUsernameDoHead.pegarUsername(token);
    	Usuario usuario = servicoUsuario.buscaPorEmail(email);
    	
    	Token tokenDeAcessoAPI = servicoDeToken.gerarNovoToken(usuario);
    	
        return new ResponseEntity<>(tokenDeAcessoAPI, HttpStatus.OK);
    }
    
	
}
