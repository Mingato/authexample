package com.redcompany.receita.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "usuario")
public class Usuario{

    @Id
    private String id;

    private String razaoSocial;

    //@Indexed(unique = true)
    private String nome;

    private String senha;

    private String perfil;

    //@Indexed(unique = true)
    private String email;

    private String telefone;

    //private S3File imagem;

    //==================================================================================================================
    //                                          Construtores
    //==================================================================================================================
    
    public Usuario() {
    }

    public Usuario(String id) {
        this.id = id;
    }
     

    //==================================================================================================================
    //                                          Getters/Setters
    //==================================================================================================================

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /*public S3File getImagem() {
        return imagem;
    }

    public void setImagem(S3File imagem) {
        this.imagem = imagem;
    }*/
    
    public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

    //==================================================================================================================
    //                                         Java Methods
    //==================================================================================================================
	

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        if (!id.equals(usuario.id)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

	

}
