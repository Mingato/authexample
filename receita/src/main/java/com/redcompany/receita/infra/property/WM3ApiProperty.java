package com.redcompany.receita.infra.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * classde para a configuracao da aplicacao de acordo com o perfil, 
 * para usar adicionar na main @EnableConfigurationProperties(WM3ApiProperty.class)
 * e no arquivo application-prod.properties adicionar wm3.seguranca.enable-https=true ou 
 * no arquivo application.properties adicionar wm3.origin-permitida= 
 *
 */
@ConfigurationProperties("wm3")
public class WM3ApiProperty {

	private String originPermitida = "http://localhost:8080";
	
	public final Seguranca seguranca = new Seguranca();
	
	
	public String getOriginPermitida() {
		return originPermitida;
	}


	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}



	public Seguranca getSeguranca() {
		return seguranca;
	}
	
	public static class Seguranca{
		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}
		
	}
	
	
}
