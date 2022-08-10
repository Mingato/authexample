package com.redcompany.receita.infra.specification;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * User: Tiago Magalhães
 * Date: 8/8/15
 * Time: 5:18 PM
 * <p/>
 * Responsabilidade: Garantir que o usuario logado nao veja ele mesmo na lista de contato
 */
public class OUsuarioConectadoNaoApareceNaListaDeContatoPropria {
    private String usuario;

    public OUsuarioConectadoNaoApareceNaListaDeContatoPropria(String idUsuario){
        this.usuario = idUsuario;
    }

    public boolean verificar(String idOutroUsuario) {
        return !usuario.equals(idOutroUsuario);
    }
}
