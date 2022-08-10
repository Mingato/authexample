package com.redcompany.receita.infra.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired(required=true)
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			.withClient("angular")
			.secret("{noop}@angular")
			.scopes("read", "write")
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(900)
			.refreshTokenValiditySeconds(3600 * 24 * 10)//dez dias
		.and()
			.withClient("mobile")
			.secret("{noop}@mobile")
			.scopes("read")
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(900)//tempo de duracao do token
			.refreshTokenValiditySeconds(3600 * 24 * 10);
	}
	
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain tokenEnhancedChain = new TokenEnhancerChain();// para passar algumas informacoes do usuario no token
		tokenEnhancedChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
		
		endpoints
			.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter())//tipo do token
			.reuseRefreshTokens(false)//um novo refresh token e enviado ao pegar um token para sempre ter um token de refresh token
			.authenticationManager(authenticationManager);		
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		return accessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {//aonde o token fica armazenado
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public TokenEnhancer tokenEnhancer() {		
		return new CustomTokenEnhancer();
	}

}
