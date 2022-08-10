package com.redcompany.receita.domain.servico;

import java.util.ArrayList;

import org.json.JSONObject;

import com.redcompany.receita.domain.Atividade;
import com.redcompany.receita.domain.Cnae_principal;
import com.redcompany.receita.domain.Qsa;
import com.redcompany.receita.domain.Receita;
import com.redcompany.receita.domain.Simples;
import com.redcompany.receita.domain.Sintegra;
import com.redcompany.receita.infra.exception.PaserErrorException;

public class JsonParser {

	
	
	public static Receita CNPJAToReceita(JSONObject obj) {
		System.out.println("CNPJAToReceita");
		Receita receita = new Receita();
		
		if(obj != null) {
			ArrayList<Atividade> atividade_principal = new ArrayList<Atividade>(); 
			ArrayList<Atividade> atividades_secundarias = new ArrayList<Atividade>();
			ArrayList<Qsa> qsa = new ArrayList<Qsa>();
			
			atividade_principal.add(
					new Atividade(
							obj.getJSONObject("primary_activity").getString("description"),
							obj.getJSONObject("primary_activity").getString("code")
					));
			
			for(Object atividade: obj.getJSONArray("secondary_activities")) {
				atividade_principal.add(
						new Atividade(
								((JSONObject)atividade).getString("description"),
								((JSONObject)atividade).getString("code")
						));
			}
			
			for(Object atividade: obj.getJSONArray("membership")) {
				
				try {
					JSONObject legal_rep = ((JSONObject)atividade).getJSONObject("legal_rep");
					
					qsa.add(
						new Qsa(
								((JSONObject)atividade).getString("name"),
								((JSONObject)atividade).getJSONObject("role").getString("description"),
								((JSONObject)atividade).getString("home_country"),
								legal_rep.getString("name"),
								legal_rep.getJSONObject("role").getString("description")
						));
				} catch(Exception e) {
					try {
						if(((JSONObject)atividade).has("home_country")) {
							qsa.add(
								new Qsa(
										((JSONObject)atividade).getString("name"),
										((JSONObject)atividade).getJSONObject("role").getString("description"),
										((JSONObject)atividade).get("home_country").toString(),
										"",""
								));
						}else {
							qsa.add(
								new Qsa(
										((JSONObject)atividade).getString("name"),
										((JSONObject)atividade).getJSONObject("role").getString("description"),
										"",
										"",""
								));
						}
					}catch (Exception ex) {
						throw new PaserErrorException(ex.getMessage());
					}
				}
				
			}
			
			try {
				receita = new Receita(
					obj.getString("tax_id"),
					obj.getString("type"),
					obj.getString("founded"),
					obj.getString("name"),
					obj.get("alias").toString(),
					atividade_principal,
					atividades_secundarias,
					obj.getJSONObject("legal_nature").getString("description"),
					obj.getJSONObject("address").getString("street"),
					obj.getJSONObject("address").getString("number"),
					obj.getJSONObject("address").get("details").toString(),
					obj.getJSONObject("address").getString("zip"),
					obj.getJSONObject("address").getString("neighborhood"),
					obj.getJSONObject("address").getString("city"),
					obj.getJSONObject("address").getString("state"),
					obj.get("email").toString(),
					obj.get("phone").toString(),
					"",//efr
					obj.getJSONObject("registration").getString("status"),
					obj.getJSONObject("registration").getString("status_date"),
					String.valueOf(obj.getJSONObject("registration").get("status_reason")),
					String.valueOf(obj.getJSONObject("registration").get("special_status")),
					String.valueOf(obj.getJSONObject("registration").get("special_status_date")),
					String.valueOf(obj.get("capital")),
					qsa,
					"",
					obj.getJSONObject("files").getString("registration")
				);
			}catch (Exception e) {
				throw new PaserErrorException(e.getMessage());
			}
			System.out.println("CNPJAToReceita finish");
		}
		
		receita.setStatus("OK");
		return receita;
	}
	
	public static Simples CNPJAToSimples(JSONObject obj) {
		System.out.println("CNPJAToSimples");
		Simples simples = new Simples();
		
		if(obj != null) {
			
			try {
				simples = new Simples(
					obj.getString("tax_id"),
					obj.getString("tax_id"),
					obj.getString("name"),
					String.valueOf(obj.getJSONObject("simples_nacional").get("simples_optant")),
					String.valueOf(obj.getJSONObject("simples_nacional").get("simei_optant")),
					String.valueOf(obj.getJSONObject("simples_nacional").get("simples_optant")),
					String.valueOf(obj.getJSONObject("simples_nacional").get("simei_optant")),
					
					String.valueOf(obj.getJSONObject("simples_nacional").get("simples_included")),
					String.valueOf(obj.getJSONObject("simples_nacional").get("simples_excluded")),
					String.valueOf(obj.getJSONObject("simples_nacional").get("simei_optant"))
				);
			}catch (Exception e) {
				throw new PaserErrorException(e.getMessage());
			}
			System.out.println("CNPJAToSimples finish");
		}
		
		simples.setStatus("OK");
		return simples;
	}

	public static Sintegra CNPJAToSintegra(JSONObject obj) {
		System.out.println("CNPJAToSintegra");
		Sintegra sintegra = new Sintegra();
		
		if(obj != null) {
			
			try {
				sintegra = new Sintegra(
						obj.getString("tax_id"),
						obj.getString("name"),
						obj.get("alias").toString(),
						obj.getJSONObject("sintegra").get("home_state_registration").toString(),
						"",
						obj.getJSONObject("registration").getString("status_date"),
						"situacao_cnpj",
						obj.getJSONObject("registration").getString("status"),
						"Não informado",
						"Não informado",
						"Não informado",
						"Não informado",
						new Cnae_principal(
								obj.getJSONObject("primary_activity").getString("description"),
								obj.getJSONObject("primary_activity").getString("code")
						),
						"",
						obj.getJSONObject("address").getString("state"),
						obj.getJSONObject("address").getString("zip"),
						obj.getJSONObject("address").getString("city"),
						obj.getJSONObject("address").getString("neighborhood"),
						obj.getJSONObject("address").getString("street"),
						obj.getJSONObject("address").get("details").toString(),
						obj.getJSONObject("address").getString("number")
				);
			}catch (Exception e) {
				throw new PaserErrorException(e.getMessage());
			}
			
			System.out.println("CNPJAToSintegra finish");
		}
		sintegra.setStatus("OK");
		return sintegra;
	}
	
}
