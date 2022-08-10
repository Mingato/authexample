package com.redcompany.receita.domain.servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.redcompany.receita.domain.Meta;
import com.redcompany.receita.domain.Plano;
import com.redcompany.receita.domain.PlanoConsulta;
import com.redcompany.receita.domain.Result;
import com.redcompany.receita.domain.S3File;
import com.redcompany.receita.domain.Usuario;
import com.redcompany.receita.domain.repositorio.RepositorioDePlanos;
import com.redcompany.receita.domain.repositorio.RepositorioDeUsuario;
import com.redcompany.receita.infra.aws.AWSClient;
import com.redcompany.receita.infra.exception.UsuarioNaoExistenteException;



@Component
public class ServicoUsuario {

	@Autowired
    private RepositorioDeUsuario repositorioDeUsuario;

	@Autowired
    private RepositorioDePlanos repositorioDePlanos;

    public Usuario buscarPorId(String idUsuario){
        Optional<Usuario> usuario = repositorioDeUsuario.findById(idUsuario);
        if(!usuario.isPresent()){
            throw new UsuarioNaoExistenteException(idUsuario);
        }
        return usuario.get();
    }

    public List<Usuario> buscaTodos(){
        List<Usuario> usuario = repositorioDeUsuario.findAll();
        return usuario;
    }
    
    
    @SuppressWarnings("deprecation")
	public List<Usuario> buscaCompleta(Meta meta) {
    	List<Usuario> usuarios = new ArrayList<Usuario>();
    	int total = repositorioDeUsuario.findAll().size();
    	
    	meta.setPages(((int)(total/meta.getPerpage())) + 1);
    	
    	if(meta.getPerpage() == -1){
    		meta.setPerpage(10);
    	}
    	
    	if(meta.getPage() > 0){
    		meta.setPage(meta.getPage() - 1);
    	}
    	    	
    	if(meta.getSort() != null){
    		if(meta.getSort().equals("desc")){
    			usuarios = repositorioDeUsuario.findByNomeLikeIgnoreCaseOrderByNomeDesc(meta.getGeneralSearch(), new PageRequest(meta.getPage(), meta.getPerpage()));
    		}else{
    			usuarios = repositorioDeUsuario.findByNomeLikeIgnoreCaseOrderByNomeAsc(meta.getGeneralSearch(), new PageRequest(meta.getPage(), meta.getPerpage()));
    		}
    	}else{
    		usuarios = repositorioDeUsuario.findByNomeLikeIgnoreCaseOrderByNomeAsc(meta.getGeneralSearch(), new PageRequest(meta.getPage(), meta.getPerpage()));
    	}
    	
    	return usuarios;    	    		
	}

    public Usuario buscaPorEmail(String email){
        Usuario usuario = repositorioDeUsuario.findByEmail(email);
        String password = "";
        if(usuario == null){
            throw new UsuarioNaoExistenteException(email);
        }
        passwordEncoder().matches(password, usuario.getSenha());
        usuario.setSenha(password);
        return usuario;
    }
    
    public Usuario buscaPorNome(String nome){
        List<Usuario> usuarios = repositorioDeUsuario.findByNomeLikeIgnoreCaseOrderByNomeAsc(nome);
        
        if(usuarios.size() <= 0){
            throw new UsuarioNaoExistenteException(nome);
        }
        
        Usuario usuario = usuarios.get(0);
        
        usuario.setSenha("");
        
        return usuario;
    }

    public boolean existeUsuarioEmail(String email){
        Usuario usuario = repositorioDeUsuario.findByEmail(email);
        return (usuario != null);
    }

    public boolean existeUsuarioId(String id){
        return repositorioDeUsuario.findById(id) != null;
    }

    public Usuario save(Usuario usuario){
    	Usuario novo = repositorioDeUsuario.save(usuario);
    	
    	getPlanoPorIdUsuario(novo.getId());
        
    	return novo;
    }

    public void remove(String id){
        repositorioDeUsuario.deleteById(id);
    }

    public void atualizaImagem(String idUsuario, S3File imagem) {
        Usuario usuario = buscarPorId(idUsuario);
        
        //usuario.setImagem(imagem);
        AWSClient.uploadS3(imagem);        
        repositorioDeUsuario.deleteById(usuario.getId());
        repositorioDeUsuario.save(usuario);
    }

    public Meta strToMeta(String str) {
		Meta meta = new Meta();
		str += "&";
		//EXAMPLE: pagination%5Bpage%5D=1&pagination%5Bpages%5D=1&pagination%5Bperpage%5D=3&pagination%5Btotal%5D=1&query%5BgeneralSearch%5D=d&sort%5Bfield%5D=nome&sort%5Bsort%5D=asc
		
		if(str.contains("pagination")){
			String page = getValue(str, str.lastIndexOf("pagination%5Bpage%5D=") + 21);
			String perpage = getValue(str, str.lastIndexOf("pagination%5Bperpage%5D=") + 24);
					
			meta.setPage(Integer.valueOf(page));
			meta.setPerpage(Integer.valueOf(perpage));
		}
		
		if(str.contains("sort")){
			String field = getValue(str, str.lastIndexOf("sort%5Bfield%5D=") + 16);
			String sort = getValue(str, str.lastIndexOf("sort%5Bsort%5D=") + 15);
			
			meta.setField(field);
			meta.setSort(sort);
		}

		meta.setGeneralSearch("");		
		if(str.contains("generalSearch")){
			String generalSearch = getValue(str, str.lastIndexOf("query%5BgeneralSearch%5D=") + 25);		
			
			meta.setGeneralSearch(generalSearch.replace("+", " "));
		}

		return meta;
	}
	
	private String getValue(String str, int init){
		String s = "";
		
		for(int i = init; i < str.length(); i++){
			if(str.charAt(i) != '&'){
				s += str.charAt(i);
			}else{
				return s;
			}			
		}
		
		return s;
	}
	
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////// PLANOS ////////////////////////////////////////////////////////////////////////
	
	public Plano getPlanoPorIdUsuario(String idUsuario) {
		buscarPorId(idUsuario);
		
		List<Plano> resultado = repositorioDePlanos.findByIdUsuario(idUsuario);
		
		if(resultado.size() <= 0) {
			//throw new QtdConsultasNaoExistenteException(idUsuario);
			resultado.add(repositorioDePlanos.save(new Plano(null, idUsuario, new PlanoConsulta(null, 30, 1), null, null)));
		}
			
		return resultado.get(0);
	}

	public Plano alterartQuantidadeDeConsultas(Plano plano) {
		Plano plano2 = getPlanoPorIdUsuario(plano.getIdUsuario());
		
		plano.setId(plano2.getId());
		
		repositorioDePlanos.save(plano);
		
		return plano;
	}
	
	public void renovarCreditosDoPLanoPorUsuario(String idUsuario) {
		List<Plano> planos = repositorioDePlanos.findByIdUsuario(idUsuario);
		
		for(Plano plano: planos) {
			plano.atualizarDia();
			
			repositorioDePlanos.save(plano);
		}
		
	}
	
	public void consumirCreditoDoPlano(String idUsuario, Result result) {
		System.out.println(" - - - VERIFICANDO CREDITOS - - - ");
		Plano plano = getPlanoPorIdUsuario(idUsuario);
		boolean consumiu = false;
		
				
		if(result.getReceita() != null) {
			if(result.getReceita().isBuscouDaReceita()) {
				consumiu = true;
				System.out.println("receita buscou");
			}
		}
		
		if(result.getSintegra() != null) {
			if(result.getSintegra().isBuscouDaReceita()) {
				consumiu = true;
				System.out.println("sintegra buscou");
			}
		}
		
		if(result.getSimples() != null) {
			if(result.getSimples().isBuscouDaReceita()) {
				consumiu = true;
				System.out.println("simples buscou");
			}
		}
		
		if(result.getSuframa() != null) {
			if(result.getSuframa().isBuscouDaReceita()) {
				consumiu = true;
				System.out.println("suframa buscou");
			}
		}
		
		
		
		if(consumiu) {
			System.out.println("Consumindo creditos");
			plano.consumirConsultaCNPJ();
			repositorioDePlanos.save(plano);
		}else {
			System.out.println("Não Consumiu creditos");
		}
	}
	
	public boolean verificarSePodeRealizarConsulta(String idUsuario) {
		Usuario usuario = buscarPorId(idUsuario);
		Plano plano = getPlanoPorIdUsuario(idUsuario);
		
		if(plano.possuiCreditosCNPJ() || usuario.getPerfil().equalsIgnoreCase("ADMINISTRADOR")) {
			//throw new NaoPossuiCreditosException();
			return true;
		}
		
		return false;
	}
	
	public void renovarCreditosDosPLanos() {
		List<Plano> planos = repositorioDePlanos.findAll();
		
		for(Plano plano: planos) {
			plano.atualizarDia();
			
			repositorioDePlanos.save(plano);
		}
		
	}
	
}
