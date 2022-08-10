package com.redcompany.receita.infra.security;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Gunter
 *	Antes de fazer a requisicao, pega o token do cookie para poder ser usado
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenPreProcessorFilter implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
			//TODO:throws IOException, ServletException {
		String refreshToken = "";
		HttpServletRequest req  = ((HttpServletRequest) request);//.getServletRequest();
		
		if("/oauth/token".equalsIgnoreCase(req.getRequestURI())
			&& "refresh_token".equalsIgnoreCase(req.getParameter("grant_type"))
			&& req.getCookies() != null) {
			
			for(Cookie cookie : req.getCookies()) {				
				if(cookie.getName().equals("refreshToken")) {
					refreshToken = cookie.getValue();
				}
			}
			req = new MyServletRequestWrapper(req, refreshToken);
			
			//req.addHeader("Authorization", "Bearer" + accessToken);
		}else if(
					"/WM3/principal.html".equalsIgnoreCase(req.getRequestURI())
				|| (
						(req.getRequestURI().contains(".html") || (req.getRequestURI().contains(".jsp")) )
							&& !"/login.html".equalsIgnoreCase(req.getRequestURI())) 
				|| (
					   (req.getRequestURI().contains("/api/usuario"))
					|| (req.getRequestURI().contains("/api/noticia"))
					|| (req.getRequestURI().contains("/arquivos/"))
					|| (req.getRequestURI().contains("/chat/"))
					|| (req.getRequestURI().contains("/categoria/"))
					|| (req.getRequestURI().contains("/templates/"))
					|| (req.getRequestURI().contains("/pages/"))
					|| (req.getRequestURI().contains("/resources/"))
					|| (req.getRequestURI().contains("/services"))
					|| (req.getRequestURI().contains("/controllers"))
					|| (req.getRequestURI().contains("/main.js"))
					|| (req.getRequestURI().contains("/notifications"))
					|| (req.getRequestURI().contains("/api/receita"))
					|| (req.getRequestURI().contains("/token/revoke"))
				)
				//excecoes, urls que nao precisao de autorizacao
				&& ( (!req.getRequestURI().contains("/assets/")) )
				&& ( (!req.getRequestURI().contains("/seguranca/")) )
				&& ( (!req.getRequestURI().contains("/api/receita/cnpj/")) )
				&& ( (!req.getRequestURI().contains("/api/receita/cep/")) )
				&& ( (!req.getRequestURI().contains("/api/receita/cpf/")) )
				&& ( (!req.getRequestURI().contains("/alteracaoDeSenha.html")) )
				){
			String accessToken = null;
			if(req.getCookies() != null) {
				for(Cookie cookie : req.getCookies()) {
					if(cookie.getName().equals("accessToken")) {
						accessToken = cookie.getValue();
					}
					if(cookie.getName().equals("refreshToken")) {
						refreshToken = cookie.getValue();
					}
				}
			}
			if(accessToken != null) {
				req = new MyServletRequestWrapper(req, refreshToken, accessToken);
			}			
		}
		
		try {
			chain.doFilter(req, response);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
	}
	
	
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		return accessTokenConverter;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	
	@Override
	public void destroy() {
		
	}
	
	/**
	 *como o getParameter map nao pode ser mudado, entao esta classe é criada para altera-lo
	 */
	static class MyServletRequestWrapper extends HttpServletRequestWrapper {
		private String refreshToken;
		private String accessToken;
		
		private Map<String, String> headerMap = new HashMap<String, String>();
		
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken, String accessToken) {
			super(request);
			this.refreshToken = refreshToken;
			this.accessToken = accessToken;
			addHeader("Authorization", "Bearer " + accessToken);
		}

		@Override
		public Map<String, String[]> getParameterMap() {			
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] {refreshToken});
			map.put("Authorization", new String[] {"Bearer " + accessToken});
			map.setLocked(true);
			return map;
		}
		
		//////////HEADERS///
		
		public void addHeader(String name, String value) {
            headerMap.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = super.getHeader(name);
            if (headerMap.containsKey(name)) {
                headerValue = headerMap.get(name);
            }
            return headerValue;
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());
            for (String name : headerMap.keySet()) {
                names.add(name);
            }
            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));
            if (headerMap.containsKey(name)) {
                values.add(headerMap.get(name));
            }
            return Collections.enumeration(values);
        }
	}
}
