package com.redcompany.receita.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Plano {
	
	@Id
	private String id;
	
	private String idUsuario;
	
	private PlanoConsulta planoCNPJ;
	
	private PlanoConsulta planoCPF;
	
	private PlanoConsulta planoCEP;

	public Plano(String id, String idUsuario, PlanoConsulta planoCNPJ, PlanoConsulta planoCPF,
			PlanoConsulta planoCEP) {
		super();
		this.id = id;
		this.idUsuario = idUsuario;
		
		setPlanoCNPJ(planoCNPJ);
		setPlanoCPF(planoCPF);
		setPlanoCEP(planoCEP);
	}
	
	public void atualizarDia() {
		planoCNPJ.setDiasRestantes(planoCNPJ.getDiasRestantes()-1);
		if(planoCNPJ.getDiasRestantes() <= 0) {
			planoCNPJ.setDiasRestantes(planoCNPJ.getDias());
			planoCNPJ.setQuantidadeRestante(planoCNPJ.getQuantidade());
		}
		
		planoCPF.setDiasRestantes(planoCPF.getDiasRestantes()-1);
		if(planoCPF.getDiasRestantes() <= 0) {
			planoCPF.setDiasRestantes(planoCPF.getDias());
			planoCPF.setQuantidadeRestante(planoCPF.getQuantidade());
		}
		
		planoCEP.setDiasRestantes(planoCEP.getDiasRestantes()-1);
		if(planoCEP.getDiasRestantes() <= 0) {
			planoCEP.setDiasRestantes(planoCEP.getDias());
			planoCEP.setQuantidadeRestante(planoCEP.getQuantidade());
		}
	}
	
	public void consumirConsultaCNPJ() {
		planoCNPJ.setQuantidadeRestante(planoCNPJ.getQuantidadeRestante() - 1);
	}
	
	public boolean possuiCreditosCNPJ() {
		return planoCNPJ.getQuantidadeRestante() > 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public PlanoConsulta getPlanoCNPJ() {
		return planoCNPJ;
	}

	public void setPlanoCNPJ(PlanoConsulta planoCNPJ) {
		if(planoCNPJ == null) {
			planoCNPJ = new PlanoConsulta(null, 40, 1);
		}
		planoCNPJ.setTipoConsulta(PlanoConsulta.TipoPlano.CNPJ);
		this.planoCNPJ = planoCNPJ;
	}

	public PlanoConsulta getPlanoCPF() {
		return planoCPF;
	}

	public void setPlanoCPF(PlanoConsulta planoCPF) {
		if(planoCPF == null) {
			planoCPF = new PlanoConsulta(null, 999999999, 100);
		}
		planoCPF.setTipoConsulta(PlanoConsulta.TipoPlano.CPF);		
		this.planoCPF = planoCPF;
	}

	public PlanoConsulta getPlanoCEP() {
		return planoCEP;
	}

	public void setPlanoCEP(PlanoConsulta planoCEP) {
		if(planoCEP == null) {
			planoCEP = new PlanoConsulta(null, 999999999, 100);
		}
		planoCEP.setTipoConsulta(PlanoConsulta.TipoPlano.CEP);
		this.planoCEP = planoCEP;
	}
	
}
