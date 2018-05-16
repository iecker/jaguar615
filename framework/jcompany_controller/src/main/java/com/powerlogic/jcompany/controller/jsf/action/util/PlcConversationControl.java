/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.action.util;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.PlcConstants;

/**
 * @since jCompany 5.
 * Classe que encapsula propriedades de formulário que vivem durante
 * toda a conversação, para controle de colaborações do jCompany.
 */
@Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
@ConversationScoped
public class PlcConversationControl implements Serializable {

	private static final long serialVersionUID = 6696563450867358374L;

	/**
	 * Modo de operação
	 * @see PlcConstants.MODOS
	 */
	private String modoPlc = PlcConstants.MODOS.MODO_INCLUSAO;

	/**
	 * Utilizado para exibição de alerta de alteracao.
	 */
	private String alertaAlteracaoPlc = null;
	
	/**
	 * Utilizado para exibição de alerta de exclusao detalhe.
	 */
	private String alertaExclusaoDetalhePlc = null;
	
	public String getAlertaAlteracaoPlc() {
		return alertaAlteracaoPlc;
	}

	public void setAlertaAlteracaoPlc(String alertaAlteracaoPlc) {
		this.alertaAlteracaoPlc = alertaAlteracaoPlc;
	}

	public String getAlertaExclusaoDetalhePlc() {
		return alertaExclusaoDetalhePlc;
	}

	public void setAlertaExclusaoDetalhePlc(String alertaExclusaoDetalhePlc) {
		this.alertaExclusaoDetalhePlc = alertaExclusaoDetalhePlc;
	}

	/**
	 * Parametro de conversacao para identificar tab-folder agil corrente.
	 */
	private String ordenacaoPlc="";
	
	/**
	 * Parametro de conversacao para identificar detalhes por demanda nao recuperados.
	 */
	Map<String, Class> detalhesPorDemanda = null;
	

	/**
	 * Parametro de conversacao para validaForm em botão pesquisa quando usado em Tabular.
	 */
	private boolean pesquisarValida = true;
	
	public String getModoPlc() {
		return modoPlc;
	}

	public void setModoPlc(String modoPlc) {
		this.modoPlc = modoPlc;
	}

	public String getOrdenacaoPlc() {
		return ordenacaoPlc;
	}

	public void setOrdenacaoPlc(String ordenacaoPlc) {
		this.ordenacaoPlc = ordenacaoPlc;
	}

	public Map<String, Class> getDetalhesPorDemanda() {
		return detalhesPorDemanda;
	}

	public void setDetalhesPorDemanda(Map<String, Class> detalhesPorDemanda) {
		this.detalhesPorDemanda = detalhesPorDemanda;
	}
	
	public boolean getPesquisarValida() {
		return pesquisarValida;
	}

	public void setPesquisarValida(boolean pesquisarValida) {
		this.pesquisarValida = pesquisarValida;
	}

	
}
