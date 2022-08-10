package com.redcompany.receita.infra.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.redcompany.receita.infra.property.WM3ApiProperty;

/**
 * 
 * @author Gunter
 *	Coloca Token no Cookie e retira do body
 */
@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{

	@Autowired
	private WM3ApiProperty wm3ApiProperty;
	
	/**
	 *  quando o metodo para de request for interceptado for postAccessToken
	 *  para a classe OAuth2AccessToken, retorna true para podermos 
	 *  executar o metodo beforeBodyWrite(...)
	 *   
	 * */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		
		HttpServletRequest req  = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp  = ((ServletServerHttpResponse) response).getServletResponse();
		
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		String refreshToken = body.getRefreshToken().getValue();
		String accessToken = body.getValue();
		
		adicionarRefreshTokenNoCookie(refreshToken, req, resp);
		adicionarAccessTokenNoCookie(accessToken, req, resp);
		removerRefreshTokenDoBody(token);		
		
		return body;
	}

	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
	}

	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(wm3ApiProperty.getSeguranca().isEnableHttps());
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		refreshTokenCookie.setMaxAge(2592000);//30 dias para expiracao
		resp.addCookie(refreshTokenCookie);
	}
	
	private void adicionarAccessTokenNoCookie(String accessToken, HttpServletRequest req, HttpServletResponse resp) {
		Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
		accessTokenCookie.setHttpOnly(true);
		accessTokenCookie.setSecure(false);
		System.out.println("ContextPath: " + req.getContextPath() + "  - TOKEN");
		accessTokenCookie.setPath("/");//req.getContextPath()
		accessTokenCookie.setMaxAge(3600 * 24);//1 dia para expiracao
		resp.addCookie(accessTokenCookie);
	}

}
