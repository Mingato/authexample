package com.redcompany.receita.infra.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.redcompany.receita.infra.property.WM3ApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter{

	@Autowired 
	private WM3ApiProperty wm3ApiProperty; 
	
	private String originPermitida = "";//TODO:wm3ApiProperty.getOriginPermitida();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));//TODO:originPermitida);
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		resp.setHeader("X-Frame-Options", "SAMEORIGIN");
		
		if("OPTIONS".equals(req.getMethod()) && 
				originPermitida.equals(req.getHeader("Origin"))) {
			resp.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
			resp.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
			resp.setHeader("Access-Control-Max-Age", "3600");
			
			
			resp.setStatus(HttpServletResponse.SC_OK);
		}else {
			chain.doFilter(request, response);
		}
		
	}
	

}
