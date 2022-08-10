package com.redcompany.receita.domain.servico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.redcompany.receita.domain.Cep;
import com.redcompany.receita.domain.Consulta;
import com.redcompany.receita.domain.Cpf;
import com.redcompany.receita.domain.Fornecedor;
import com.redcompany.receita.domain.Receita;
import com.redcompany.receita.domain.Result;
import com.redcompany.receita.domain.SaldoSintegra;
import com.redcompany.receita.domain.Simples;
import com.redcompany.receita.domain.Sintegra;
import com.redcompany.receita.domain.Suframa;
import com.redcompany.receita.domain.TipoConsulta;
import com.redcompany.receita.domain.repositorio.RepositorioDeConsulta;
import com.redcompany.receita.domain.repositorio.RepositorioDeCpf;
import com.redcompany.receita.domain.repositorio.RepositorioDeReceita;
import com.redcompany.receita.domain.repositorio.RepositorioDeSaldoSintegra;
import com.redcompany.receita.domain.repositorio.RepositorioDeSimples;
import com.redcompany.receita.domain.repositorio.RepositorioDeSintegra;
import com.redcompany.receita.domain.repositorio.RepositorioDeSuframa;
import com.redcompany.receita.infra.exception.NaoPossuiCreditosException;
import com.redcompany.receita.infra.exception.ReceitaNaoExistenteException;


@Component
public class ServicoReceita {

	@Autowired
    private RepositorioDeReceita repositorioDeReceita;
	
	@Autowired
    private RepositorioDeSintegra repositorioDeSintegra;
	
	@Autowired
    private RepositorioDeSimples repositorioDeSimples;
	
	@Autowired
    private ServerConnection server;
	
	@Autowired
    private ServicoUsuario servicoUsuario;
	
	
	@Autowired
    private RepositorioDeSuframa repositorioDeSuframa;
	
	@Autowired
	private RepositorioDeConsulta repositorioDeConsulta;

	@Autowired
	private RepositorioDeSaldoSintegra repositorioDeSaldoSintegra;
	
	@Autowired
	private RepositorioDeCpf repositorioDeCpf;
	
	private Gson gson = new Gson(); // Or use new GsonBuilder().create();
	
    public Receita buscarPorId(String idReceita){
        Optional<Receita> receita = repositorioDeReceita.findById(idReceita);
        if(receita == null){
            throw new ReceitaNaoExistenteException(idReceita);
        }
        return receita.get();
    }
    
    public Result buscaCompletaPorCNPJ(String cnpj, String idUsuario, String source, String consulta_max_age){
    	Result result = new Result();
        String tp_consulta = "receita,sintegra,simples,suframa";
        
        //TODO: antes das consultas pesquisar em qual fornecedor fazer a consulta
        
    	if(isCNPJ(cnpj)) {
	    	System.out.println(" --- . . . PEGANDO RECEITS BUSCA COMPLETA . . . --- ");
	    	
	    	
	    	result.setReceita(buscarReceita(cnpj, idUsuario, tp_consulta, source, consulta_max_age));
	    	if(result.getReceita() != null) {
	    		result.setStatus("OK");
	    		
	    		
	    		if(result.getReceita().getStatus() != null) {
			    	if(!result.getReceita().getStatus().equals("ERROR")) {
			    		result.setSintegra(buscarSintegra(cnpj, idUsuario, tp_consulta, source, consulta_max_age));
			    		
			        	result.setSimples(buscarSimples(cnpj, idUsuario, tp_consulta, source, consulta_max_age));
			        	result.setSuframa(buscarSuframa(cnpj, pegarUf(result), idUsuario, tp_consulta, source, consulta_max_age));
			    	}
		    	}else {
		    		result.setSimples(buscarSimples(cnpj, idUsuario, tp_consulta, source, consulta_max_age));
		    		result.setSuframa(buscarSuframa(cnpj, pegarUf(result), idUsuario, tp_consulta, source, consulta_max_age));
		    	}
	    	}else {
	    		result.setStatus("ERROR");
	    		result.setMessage("Não foi possível realizar a consulta do cnpj");
	    		System.out.println("ERROR: Não foi possível realizar a consulta do cnpj!");
	    	}
    	}else {
    		result.setStatus("ERROR");
    		result.setMessage("CNPJ invalido");
    		System.out.println("ERROR: CNPJ Inválido!");
    	}
    	
    	servicoUsuario.consumirCreditoDoPlano(idUsuario, result);
    	
    	System.out.println("=============== olhaa11 " + result.getReceita().toString());
        return result;
    }
    
    public Result buscaPorCNPJ(String cnpj, String idUsuario, String tp_consulta, String source, String consulta_max_age) {
    	Result result = new Result();
    	boolean realizouConsulta = false;
    	
    	//TODO: antes das consultas pesquisar em qual fornecedor fazer a consulta
    	if(isCNPJ(cnpj)) {
	    	System.out.println(" --- . . . buscaPorCNPJ especificando o tipo consulta. . . --- ");
	    	
	    	if(tp_consulta.toLowerCase().contains(TipoConsulta.RECEITA.getTipo())) {	    		
	    		
	    		result.setReceita(buscarReceita(cnpj, idUsuario, tp_consulta, source, consulta_max_age));
	    		System.out.println("SINTEGRA " + result.getReceita().isBuscouDaReceita());
				realizouConsulta = true;
	    	}
	    	
	    	if(tp_consulta.toLowerCase().contains(TipoConsulta.SINTEGRA.getTipo()) && result.getReceita() == null) {
	    		System.out.println("SINTEGRA");
	    		result.setSintegra(buscarSintegra(cnpj, idUsuario, tp_consulta, source, consulta_max_age));
	    		realizouConsulta = true;
	       	}else if(tp_consulta.toLowerCase().contains(TipoConsulta.SINTEGRA.getTipo()) && result.getReceita() != null) {
	       		System.out.println("SINTEGRA");
	       		result.setSintegra(buscarSintegra(cnpj, idUsuario, tp_consulta, source, consulta_max_age));
	    		realizouConsulta = true;
	       	}
	    	
	    	if(tp_consulta.toLowerCase().contains(TipoConsulta.SIMPLES.getTipo())) {
	    		System.out.println("SIMPLES");
	    		result.setSimples(buscarSimples(cnpj, idUsuario, tp_consulta, source, consulta_max_age));
	    		realizouConsulta = true;
	    	}
			
	    	
			if(tp_consulta.toLowerCase().contains(TipoConsulta.SUFRAMA.getTipo())) {
				System.out.println("SUFRAMA");
	    		result.setSuframa(buscarSuframa(cnpj, "AM", idUsuario, tp_consulta, source, consulta_max_age));
	    		realizouConsulta = true;
	    	}
			
			if(realizouConsulta) {
				servicoUsuario.consumirCreditoDoPlano(idUsuario, result);
			}
    	}else {
    		result.setStatus("ERROR");
    		result.setMessage("CNPJ invalido");
    	}
    	
    	System.out.println("=============== olhaa " + result.getReceita().toString());
    	
		return result;
	}
	
    
    private Receita buscarReceita(String cnpj, String idUsuario, String tpConsulta, String source, String consulta_max_age) {
    	Receita receita = null;
    	//List<Receita> receitaDoBanco;
    	Optional<Receita> receitaDoBanco;
    	boolean atualizar = true;
    	
    	//registra a consulta do usuario
    	repositorioDeConsulta.save(new Consulta(null, idUsuario, System.currentTimeMillis(), TipoConsulta.RECEITA, tpConsulta, cnpj, "receita", new Fornecedor(ServerConnection.Fornecedores.CNPJA.getFornecedor(), "", "", ""), source));
    	System.out.println(" --- Buscando de Receita ---");
    	//verificar se existe no banco local, de acordo com uma data minima
    	receitaDoBanco = repositorioDeReceita.findByCnpj(cnpj);
    	if(receitaDoBanco.isPresent()) {
    		receita = receitaDoBanco.get();
    		System.out.println("- data consulta: " + receita.getDataConsulta());
    		if(receita.getDataConsulta() > (System.currentTimeMillis() - (33L * 24L * 60L * 60L * 1000L))) {
    			System.out.println("Existe no banco local Receita com data valida");
    			receita.setCode("0");
	    		receita.setBuscouDaReceita(false);
	    		atualizar = VerificarOTempoDeCache(consulta_max_age, receita.getDataConsulta());
    		}
    	}
    	atualizar = true;
    	if(atualizar){//caso nao exista a consulta no banco de dados ou pode atualizar
    		if(!servicoUsuario.verificarSePodeRealizarConsulta(idUsuario)) {
    			throw new NaoPossuiCreditosException();
    		}
    		
    		try {
    			System.out.println("buscando do site receita");
				JSONObject obj = server.buscarReceita(cnpj, null, "", ServerConnection.Fornecedores.CNPJA, ServerConnection.TipoConsulta.RECEITA, consulta_max_age);
				System.out.println("buscou do site receita " + obj);
				if(obj != null) {
					System.out.println("buscou do site receita obj not null");
					if(obj.getString("status").equalsIgnoreCase("OK")) {
						System.out.println("buscou do site receita status OK");
						try{ System.out.println("PEGOU DE RECEITAWS . . . " + obj.getString("message"));}catch(Exception e) {System.out.println("PEGOU DE RECEITAWS . . . ");}
							System.out.println("buscou receita: " + obj);
							receita = convertJsonToReceita(obj);
					    	receita.setMessage("Consulta realizada");
					    	receita.setBuscouDaReceita(true);
							repositorioDeReceita.save(receita);
							receita.setCode("0");
							System.out.println("buscou do site receita passou no try");
					}else if(Integer.valueOf(obj.getString("code")) > 0){
						return erroReceita(obj.getString("message"), obj.getString("status"), obj.getString("code"));
					}
				}else {
					
					return erroReceita("Não encontrou receita", "INFO", "8");
    			}
    		}catch(Exception e) {
    			e.printStackTrace();
				return erroReceita("Erro ao buscar CNPJ receita: " + e.getMessage(), "ERROR", "");
    		}
    	}
    	
		return receita;
	}

    private Sintegra buscarSintegra(String cnpj, String idUsuario, String tpConsulta, String source, String consulta_max_age) {
    	Sintegra sintegra = null;    	
    	//List<Sintegra> sintegraDoBanco;
    	Optional<Sintegra> sintegraDoBanco;
    	boolean atualizar = true;
    	
    	//registra a consulta do usuario
    	repositorioDeConsulta.save(new Consulta(null, idUsuario, System.currentTimeMillis(), TipoConsulta.SINTEGRA, tpConsulta, cnpj, "receita", new Fornecedor(ServerConnection.Fornecedores.CNPJA.getFornecedor(), "", "", ""), source));
    	System.out.println(" --- Buscando de Sintegra ---");
    	//verificar se existe no banco local, de acordo com uma data minima
    	sintegraDoBanco = repositorioDeSintegra.findByCnpj(cnpj);
    	if(sintegraDoBanco.isPresent()) {
    		sintegra = sintegraDoBanco.get();
    		System.out.println("- data consulta: " + sintegra.getDataConsulta());
    		if(sintegra.getDataConsulta() > (System.currentTimeMillis() - (33L * 24L * 60L * 60L * 1000L))) {
	    		sintegra.setCode("0");
	    		sintegra.setBuscouDaReceita(false);
	    		System.out.println("Existe no banco local Sintegra");
	    		atualizar = VerificarOTempoDeCache(consulta_max_age, sintegra.getDataConsulta());
    		}
    	}
    	
    	if(atualizar){//caso nao exista a consulta no banco de dados ou pode atualizar
    		if(!servicoUsuario.verificarSePodeRealizarConsulta(idUsuario)) {
    			throw new NaoPossuiCreditosException();
    		}
    		
    		try {
    			System.out.println("buscando do site sintegra ST");
    			JSONObject obj = server.buscarReceita(cnpj, null,"", ServerConnection.Fornecedores.CNPJA, ServerConnection.TipoConsulta.SINTEGRA, consulta_max_age);
    			if(obj != null) {
	    			if(obj.getString("code").equals("0")) {
	    	    		System.out.println("PEGOU DE SINTEGRAWS Sintegra. . . " + obj.getString("message"));
	    	    		sintegra = convertJsonToSintegra(obj);
	    	    		sintegra.setDataConsulta(new Date().getTime());
	    	    		sintegra.setBuscouDaReceita(true);
	    	    		repositorioDeSintegra.save(sintegra);
	    	    		sintegra.setCode("0");
	    	        	
	    	    	}else if(Integer.valueOf(obj.getString("code")) > 0){
						return erroSintegra(obj.getString("message"), obj.getString("status"), obj.getString("code"));
	    	    	}
    			}else {
    				return erroSintegra("CNPJ não encontrado no sintegra", "INFO", "1");
    			}
    		}catch(Exception e) {
    			e.printStackTrace();    		
				return erroSintegra("Erro ao buscar CNPJ sintegra: " + e.getMessage(), "ERROR", "8");
    		}
	    	
    	}
    	
		return sintegra;
	}
    
	private Simples buscarSimples(String cnpj, String idUsuario, String tpConsulta, String source, String consulta_max_age) {
    	Simples simples = null;
    	//List<Simples> simplesDoBanco;
    	Optional<Simples> simplesDoBanco;
    	boolean atualizar = true;
    	
    	//registra a consulta do usuario
    	repositorioDeConsulta.save(new Consulta(null, idUsuario, System.currentTimeMillis(), TipoConsulta.SIMPLES, tpConsulta, cnpj, "receita", new Fornecedor(ServerConnection.Fornecedores.CNPJA.getFornecedor(), "", "", ""), source));
    	System.out.println(" --- Buscando de Simples ---");
    	//verificar se existe no banco local, de acordo com uma data minima
    	simplesDoBanco = repositorioDeSimples.findByCnpj(cnpj);
    	if(simplesDoBanco.isPresent()) {
    		simples = simplesDoBanco.get();
    		System.out.println("- data consulta simples: " + simples.getDataConsulta());
    		if(simples.getDataConsulta() > (System.currentTimeMillis() - (33L * 24L * 60L * 60L * 1000L))) {
	    	
	    		simples.setCode("0");
	    		simples.setBuscouDaReceita(false);
	    		System.out.println("Existe no banco local Simples");
	    		atualizar = VerificarOTempoDeCache(consulta_max_age, simples.getDataConsulta());
	    	}
    	}
    	
    	if(atualizar){//caso nao exista a consulta no banco de dados ou pode atualizar
    		if(!servicoUsuario.verificarSePodeRealizarConsulta(idUsuario)) {
    			throw new NaoPossuiCreditosException();
    		}
    		
	    	try {
	    		System.out.println("buscando do site sintegra SN");
	    		JSONObject obj = server.buscarReceita(cnpj, null, "", ServerConnection.Fornecedores.CNPJA, ServerConnection.TipoConsulta.SIMPLES, consulta_max_age);
	    		if(obj != null) {
			    	if(obj.getString("code").equals("0")) {
			    		System.out.println("PEGOU DE SINTEGRAWS Simples. . . " + obj.getString("message"));
			    		simples = convertJsonToSimples(obj);
			    		simples.setDataConsulta(new Date().getTime());
			    		simples.setBuscouDaReceita(true);
			        	repositorioDeSimples.save(simples);
			        	simples.setCode("0");
			    	}else if(Integer.valueOf(obj.getString("code")) > 0){
						return erroSimples(obj.getString("message"), obj.getString("status"), obj.getString("code"));
			    	}
	    		}else {
    				return erroSimples("Não encontrou simples", "INFO", "1");
    			}
	    	}catch(Exception e) {
				return erroSimples("Erro ao buscar CNPJ simples: " + e.getMessage(), "ERROR", "8");
    		}
    	}
    	
		return simples;
	}
    
    private Suframa buscarSuframa(String cnpj, String uf, String idUsuario, String tpConsulta, String source, String consulta_max_age) {
    	Suframa suframa = new Suframa();
    	Optional<Suframa> suframaDoBanco;
    	boolean atualizar = true;
    	
    	suframa.setBuscouDaReceita(false);
    	System.out.println(" --- Buscar de suframa ---");
    	if(uf != null) {
    		if(uf.equals("")) {        		
    			System.out.println("UF não informado pela receita!");
        		return erroSuframa("UF não informado pela receita", "WARNING", "10");
        	}
    		
	    	if(uf.equalsIgnoreCase("AM") ||
	    		uf.equalsIgnoreCase("AC") ||
	 		   uf.equalsIgnoreCase("RR") ||
	 		   uf.equalsIgnoreCase("RO") ||
	 		   uf.equalsIgnoreCase("AP")) {
		    	
	    		//registra a consulta do usuario
	    		repositorioDeConsulta.save(new Consulta(null, idUsuario, System.currentTimeMillis(), TipoConsulta.SUFRAMA, tpConsulta, cnpj, "receita", new Fornecedor(ServerConnection.Fornecedores.CNPJA.getFornecedor(), "", "", ""), source));
		    	System.out.println("Buscando de  Suframa . . . ");
		    	//verificar se existe no banco local, de acordo com uma data minima
		    	suframaDoBanco = repositorioDeSuframa.findByCnpj(cnpj);
		    	if(suframaDoBanco.isPresent()) {
		    		suframa = suframaDoBanco.get();
		    		System.out.println("- data consulta suframa: " + suframa.getDataConsulta());
		    		if(suframa.getDataConsulta() > (System.currentTimeMillis() - (33L * 24L * 60L * 60L * 1000L))) {
			    		System.out.println("Existe no banco local Suframa");
			    		suframa.setCode("0");
			    		suframa.setBuscouDaReceita(false);
			    		atualizar = VerificarOTempoDeCache(consulta_max_age, suframa.getDataConsulta());
		    		}
		    	}
		    	
		    	if(atualizar){//caso nao exista a consulta no banco de dados ou pode atualizar
		    		if(!servicoUsuario.verificarSePodeRealizarConsulta(idUsuario)) {
		    			throw new NaoPossuiCreditosException();
		    		}
		    		
		    		try {
		    			System.out.println("buscando do site sintegra SF");
			    		JSONObject obj = server.buscarReceita(cnpj, null, "", ServerConnection.Fornecedores.SINTEGRAWS, ServerConnection.TipoConsulta.SUFRAMA, consulta_max_age);
			    		if(obj != null) {
				    		System.out.println("PEGOU DE SINTEGRAWS Suframa. . . " + obj.getString("message"));
				    		
				        	if(obj.getString("code").equalsIgnoreCase("0")) {
				        		suframa = convertJsonToSuframa(obj);
				        		suframa.setDataConsulta(new Date().getTime());
				        		suframa.setBuscouDaReceita(true);
				            	
				        		repositorioDeSuframa.save(suframa);
				        		suframa.setCode("0");
				        	}else if(Integer.valueOf(obj.getString("code")) > 0){
								return erroSuframa(obj.getString("message"), obj.getString("status"), obj.getString("code"));
								
				        	}
			    		}else {//caso não encontrou em suframa grava para q não realize a consulta no site novamente
			    			suframa = erroSuframa("Não encontrou suframa", "INFO", "1");
			    			suframa.setDataConsulta(new Date().getTime());
			            	suframa.setCnpj(cnpj);
			        		repositorioDeSuframa.save(suframa);
							return suframa;
		    			}
		    		}catch(Exception e) {
		    			e.printStackTrace();
						return erroSuframa("Erro ao buscar CNPJ suframa: " + e.getMessage(), "ERROR", "8");
		    		}
		    	}
	    	}else {
	    		System.out.println("UF diferente de AM, RR, RO, AP, AC");
	    		return erroSuframa("UF diferente de AM, RR, RO, AP, AC", "INFO", "10");
	    	}
    	}else {
    		System.out.println("UF não informado pela receita!");
    		return erroSuframa("UF não informado pela receita", "WARNING", "11");
    	}
	    return suframa;
    }
    
    
    
   
    private boolean VerificarOTempoDeCache(String consulta_max_age, Long dataConsulta) {
		
    	if(consulta_max_age == null) {
    		return false;
    	}
    	
    	if(consulta_max_age.equals("")) {
    		return false;
    	}
    	
    	try {
	    	if((Integer.valueOf(consulta_max_age) * 24 * 60 * 60 * 1000) < (System.currentTimeMillis() - dataConsulta)) {
	    		return true;
	    	}
    	
    	}catch (Exception e) {
			
		}
    	
		return false;
	}

	private Receita erroReceita(String message, String status, String code) {
    	Receita receita = new Receita();
		receita.setStatus(status);
		receita.setMessage(message);
		receita.setCode(code);
		System.out.println(message);
		
		return receita;
    }
    
    private Sintegra erroSintegra(String message, String status, String code) {
    	Sintegra sintegra = new Sintegra();
    	sintegra.setStatus(status);
    	sintegra.setMessage(message);
    	sintegra.setCode(code);
		System.out.println(message);
		
		return sintegra;
    }
    
    private Simples erroSimples(String message, String status, String code) {
    	Simples simples = new Simples();
    	simples.setStatus(status);
    	simples.setMessage(message);
    	simples.setCode(code);
    	System.out.println(message);
    	
		return simples;
    }
    
    private Suframa erroSuframa(String message, String status, String code) {
    	Suframa suframa = new Suframa();
    	suframa.setStatus(status);
    	suframa.setMessage(message);
    	suframa.setCode(code);
    	System.out.println(message);
    	
		return suframa;
    }
        
		
	
	//para resolver o caso queira fazer uma consulta em suframa
	private String pegarUf(Result result) {
		String uf = null;
		
		if(result.getReceita() != null) {
			uf = result.getReceita().getUf();
			if(uf == "") {
				uf = null;
			}
		}
		if(result.getSuframa() != null && uf == null) {
			uf = result.getSuframa().getUf();
		}
		
		return uf;
	}
	
	private Receita convertJsonToReceita(JSONObject json) {		
		System.out.println("CONVERTENDO JSON to Receita . . . ");
		
		Receita receita = gson.fromJson(String.valueOf(json), Receita.class);		
		receita.setCnpj(tirarCaracteresEspeciais(receita.getCnpj()));
		receita.setDataConsulta(System.currentTimeMillis());
		return receita;
	}
	
	private String tirarCaracteresEspeciais(String cnpj) {
		cnpj = cnpj.replaceAll("/", "");
		cnpj = cnpj.replaceAll("-", "");
		cnpj = cnpj.replace('.', ' ');
		cnpj = cnpj.replaceAll(" ", "");
		//System.out.println("CNPJ Formatado: " + cnpj);
		return cnpj;
	}

	private Sintegra convertJsonToSintegra(JSONObject json) {
		Sintegra sintegra = gson.fromJson(String.valueOf(json), Sintegra.class);
		sintegra.setDataConsulta(System.currentTimeMillis());
		return sintegra;
	}

	private Simples convertJsonToSimples(JSONObject json) {
		System.out.println("CONVERTENDO JSON to Simples . . . ");
		Simples simples = gson.fromJson(String.valueOf(json), Simples.class);;
		simples.setDataConsulta(System.currentTimeMillis());
		return simples;
	}
		
	private Suframa convertJsonToSuframa(JSONObject json) {
		System.out.println("CONVERTENDO JSON to Suframa . . . \n" + json);
		if(json.toString().equals("")) {
			return null;
		}
		Suframa suframa = gson.fromJson(String.valueOf(json), Suframa.class);
		suframa.setDataConsulta(System.currentTimeMillis());
		return suframa;
	}
	
	public Result setVerificarConsultasRealizadas(Result result, String tp_consulta) {
		if(result.getStatus() == null) {
			result.setStatus("");
		}

		if(result.getStatus().equals("ERROR")) {
			return result;
		}
		try {
			if(tp_consulta == null) {
				result.setMessage("");
			
				if(result.getReceita().getCode().equals("")) {
					result.getReceita().setCode("4");
				}

				if(result.getSintegra() == null) {
					result.setSintegra(new Sintegra());
				}
				if(result.getSintegra().getCode().equals("")) {
					result.getSintegra().setCode("4");
				}
				
				if(result.getSimples() == null) {
					result.setSimples(new Simples());
				}
				if(result.getSimples().getCode().equals("")) {
					result.getSimples().setCode("4");
				}
				
				if(result.getSuframa() == null) {
					result.setSuframa(new Suframa());
				}
				if(result.getSuframa().getCode().equals("")) {
					result.getSuframa().setCode("0");
				}
				
				if( Integer.valueOf(result.getReceita().getCode()) > 0 ||
					Integer.valueOf(result.getSintegra().getCode()) > 0 ||
					Integer.valueOf(result.getSimples().getCode()) > 0 ||
					Integer.valueOf(result.getSuframa().getCode()) > 0) {
					
					result.setStatus("WARNING");
				}
				
				if(result.getMessage().equals("")) result.setMessage("Consulta Realizada com sucesso");
				
			}else {
				result.setMessage("");
				
				if(result.getReceita() != null)
				if(Integer.valueOf(result.getReceita().getCode()) > 0 && tp_consulta.toLowerCase().contains(TipoConsulta.RECEITA.getTipo())) {
					result.setStatus("WARNING");
				}
				
				if(result.getSintegra() != null)
				if(Integer.valueOf(result.getSintegra().getCode()) > 0 && tp_consulta.toLowerCase().contains(TipoConsulta.SINTEGRA.getTipo())) {
					result.setStatus("WARNING");
				}
				
				if(result.getSimples() != null)
				if(Integer.valueOf(result.getSimples().getCode()) > 0 && tp_consulta.toLowerCase().contains(TipoConsulta.SIMPLES.getTipo())) {
					result.setStatus("WARNING");
				}
				
				if(result.getSuframa() != null)
				if(Integer.valueOf(result.getSuframa().getCode()) > 0 && tp_consulta.toLowerCase().contains(TipoConsulta.SUFRAMA.getTipo())) {
					result.setStatus("WARNING");
				}
				
				if(result.getMessage().equals("")) result.setMessage("Consulta Realizada com sucesso");
			}
		}catch(Exception e) {
			System.out.println("ERRO setVerificarConsultasRealizadas: " + e.getCause());
			e.printStackTrace();
			return result;
		}
		
		return result;
		
	}
   

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////CEP///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Cep buscarCep(String cep) {
    	
    	JSONObject json = server.sendRequest("https://viacep.com.br/ws/" + cep + "/json", "", "GET", "", 1000*5);
    	
    	if(json != null) {
    		Cep result = convertJsonToCep(json);
    		if(result.getCep() != null) {
	    		result.setCode("0");
	    		result.setStatus("OK");
	    		result.setMessage("Consulta realizada!");
    		}else {
	    		result.setCode("1");
	    		result.setStatus("OK");
	    		result.setMessage("Cep não encontrado!");
    		}
    		return result;
    	}
    	
    	return erroCep("Cep não encontrado", "ERROR", "1");
    }
    
    private Cep convertJsonToCep(JSONObject json) {
		System.out.println("CONVERTENDO JSON to cep . . . ");
		Cep cep = gson.fromJson(String.valueOf(json), Cep.class);;
		
		return cep;
	}
    
    private Cep erroCep(String message, String status, String code) {
    	Cep cep = new Cep();
    	cep.setStatus(status);
    	cep.setMessage(message);
    	cep.setCode(code);
    	System.out.println(message);
    	
		return cep;
    }
    
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////CPF///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Cpf buscarPorCpf(String cpf, String dataNascimento, String idUsuario, String source, String consulta_max_age) {
    	Cpf cpfResult = null;
    	List<Cpf> cpfDoBanco;
    	
    	//registra a consulta do usuario
    	repositorioDeConsulta.save(new Consulta(null, idUsuario, System.currentTimeMillis(), TipoConsulta.CPF, "CPF",cpf,"receita",null, source));//TODO:adicionar fornecedor
    	System.out.println(" --- Buscando de Sintegra ---");
    	//verificar se existe no banco local, de acordo com uma data minima
    	cpfDoBanco = repositorioDeCpf.findByCpfAndDataConsultaLessThan(cpf, System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000));
    	
    	if(cpfDoBanco.size() > 0) {
    		cpfResult = cpfDoBanco.get(0);
    		if(!cpfResult.getData_nascimento().equals(dataNascimento)) {
    			return erroCpf("Data incorreta", "OK", "9"); 
    		}
    			
    		System.out.println("Existe no banco local cpf");
    	}else {//caso nao exista a consulta no banco de dados ou pode atualizar
    		try {
    			System.out.println("buscando do site sintegra ST");
    			JSONObject obj = server.buscarReceita(cpf, dataNascimento, "", ServerConnection.Fornecedores.SINTEGRAWS, ServerConnection.TipoConsulta.CPF, consulta_max_age);
		    	if(obj != null) {
			    	if(obj.getString("status").equals("OK")) {
			    		System.out.println("PEGOU DE SINTEGRAWS cpf. . . " + obj.getString("message"));
			    		cpfResult = convertJsonToCpf(obj);
			    		cpfResult.setDataConsulta(new Date().getTime());
			    		
			    		repositorioDeCpf.save(cpfResult);
			    	}else if(Integer.valueOf(obj.getString("code")) > 0){
						return erroCpf(obj.getString("message"), obj.getString("status"), obj.getString("code"));
			    	}
		    	}else {
		    		return erroCpf("Cpf não encontrado no sintegra", "INFO", "1");
		    	}
    		}catch(Exception e) {
    			e.printStackTrace();
    			return erroCpf("Erro ao buscar Cpf sintegra", "ERROR", "8");
    		}
    	}
    	
		return cpfResult;
    }
    
    private Cpf convertJsonToCpf(JSONObject json) {
		System.out.println("CONVERTENDO JSON to cpf . . . ");
		Cpf cpf = gson.fromJson(String.valueOf(json), Cpf.class);;
		
		return cpf;

    }
    
    private Cpf erroCpf(String message, String status, String code) {
    	Cpf cpf = new Cpf();
    	cpf.setStatus(status);
    	cpf.setMessage(message);
    	cpf.setCode(code);
    	System.out.println(message);
    	
		return cpf;
    }
    
    
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////UTILS///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 
    
    /*private String tratarErrorsDeRequestSintegra(JSONObject json) {
		String message = "ERROR";
		
		switch (json.getString("code")) {
		case "2":
			message = "CNPJ inválido.";
			break;
		case "3"://Token inválido.
			message = "Não foi possivel realizar a consulta";
			break;
		case "4"://Usuário não contratou nenhum pacote de créditos.
			message = "Não foi possivel realizar a consulta";
			break;
		case "5"://Os créditos contratados acabaram.
			message = "Não foi possivel realizar a consulta";
			break;
		case "6":
			message = "Plugin não existe."; 
			break;
		case "7"://Site do simples nacional esta com instabilidade.
			message = "Site de consulta esta com instabilidade.";
			break;
		case "8"://Ocorreu um erro interno, por favor contatar o nosso suporte.
			message = "Ocorreu um erro interno, por favor contatar o nosso suporte.";
			break;

		default:
			break;
		}
		
		return message;
	}*/
	
	public List<Receita> buscaTodos(){
        List<Receita> receita = repositorioDeReceita.findAll();
        return receita;
    }

    public Receita buscaPorEmail(String email){
    	Receita receita = repositorioDeReceita.findByEmail(email);
        if(receita == null){
            throw new ReceitaNaoExistenteException(email);
        }
        return receita;
    }

    public Receita save(Receita receita){
        return repositorioDeReceita.save(receita);
    }

    public void remove(String id){
    	repositorioDeReceita.deleteById(id);
    }
    
    public void consultarSaldo() {
    	try {
		    ServerConnection serverConection = new ServerConnection();
		    System.out.println("Gravando consulta de saldoSintegra");
		    JSONObject obj = serverConection.buscarCredito();
		    System.out.println(obj);
		    
		    SaldoSintegra saldoSintegra = new SaldoSintegra();
		    saldoSintegra.setData(System.currentTimeMillis());
		    saldoSintegra.setSaldo(obj.getString("qtd_consultas_disponiveis"));
		    
		    if(saldoSintegra != null)
		    	repositorioDeSaldoSintegra.save(saldoSintegra);
		    	    
		    System.out.println("consulta de saldoSintegra gravado");
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public List<Consulta> buscarConsultasRealizadas(String idUsuario, Long tempo) {
		return repositorioDeConsulta.findByIdUsuarioAndDataConsultaGreaterThanOrderByDataConsulta(idUsuario, tempo);		
	}
    
    public List<Consulta> buscarConsultasRealizadas(String idUsuario, String beginData, String endData) {
        System.out.println("Buscando consultas");
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    		System.out.println("data0: " + beginData);
        	System.out.println("data1: " + endData);
			Date date0 = sdf.parse(beginData);
			Date date1 = sdf.parse(endData);
			System.out.println("Data0 " + date0.getTime());
			System.out.println("Data1 " + (date1.getTime() + 86399999));
			//return repositorioDeConsulta.findByIdUsuarioAndDataConsultaGreaterThanOrderByDataConsulta(idUsuario, (1563566585956L - (1000 * 60 * 60 * 24 * 365)));
			return repositorioDeConsulta.
					findByIdUsuarioAndDataConsultaBetweenOrderByDataConsulta(
									idUsuario, 
									date0.getTime(),//(1563566585956L - (1000 * 60 * 60 * 24 * 365)),
									(date1.getTime() + 86399999));//1563566012834L
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	System.out.println("Deu ruim ");
		return null;
	}
    
    public boolean isCNPJ(String CNPJ) {
    	// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
	    if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
	        CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
	        CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
	        CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
	        CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
	       (CNPJ.length() != 14)) {
	    	return(false);
	    }
    	 
	    char dig13, dig14;
	    int sm, i, r, num, peso;
    	 
    	// "try" - protege o código para eventuais erros de conversao de tipo (int)
	    try {
	    	// Calculo do 1o. Digito Verificador
	    	sm = 0;
	    	peso = 2;
	    	for (i=11; i>=0; i--) {
	    		// converte o i-ésimo caractere do CNPJ em um número:
		    	// por exemplo, transforma o caractere '0' no inteiro 0
		    	// (48 eh a posição de '0' na tabela ASCII)
    	        num = (int)(CNPJ.charAt(i) - 48);
    	        sm = sm + (num * peso);
    	        peso = peso + 1;
    	        if (peso == 10)
    	           peso = 2;
    	     }
    	 
    	     r = sm % 11;
    	     if ((r == 0) || (r == 1))
    	    	 dig13 = '0';
    	     else 
    	    	 dig13 = (char)((11-r) + 48);
    	 
    	     // Calculo do 2o. Digito Verificador
    	     sm = 0;
    	     peso = 2;
    	     for (i=12; i>=0; i--) {
    	    	 num = (int)(CNPJ.charAt(i)- 48);
    	    	 sm = sm + (num * peso);
    	    	 peso = peso + 1;
    	    	 if (peso == 10)
    	    		 peso = 2;
    	     }
    	 
    	     r = sm % 11;
    	     if ((r == 0) || (r == 1))
    	        dig14 = '0';
    	     else 
    	    	 dig14 = (char)((11-r) + 48);
    	 
    	     // Verifica se os dígitos calculados conferem com os dígitos informados.
    	     if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
    	    	 return(true);
    	     else 
    	    	 return(false);
	    } catch (InputMismatchException erro) {
	        return(false);
	    }
    }    
    
    public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    
}