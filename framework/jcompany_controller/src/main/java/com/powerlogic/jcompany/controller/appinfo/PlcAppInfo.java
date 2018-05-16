/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.appinfo;

import java.util.List;

/**
 * Informa as configurações da aplicação: sigla, nome, actions e roles. Guarda
 * as informações da aplicação para auto-configuração do jSecurity (módulo
 * administrador), chamado via web-service.
 * 
 * @author Roberto Badaró
 */
public class PlcAppInfo {

	private String applicationAcronym;
	private String applicationName;
	private List<PlcAppMBInfo> actions;
	private List<String> roles;
	private boolean configured = true;

	/* Recuperação de Filters para o jSecurity */
	private List<String> filterDefs;

	/**
	 * 
	 * @param siglaAplicacao
	 * @param nomeAplicacao
	 * @param actions
	 * @param roles
	 */
	public PlcAppInfo(String siglaAplicacao, String nomeAplicacao, List<PlcAppMBInfo> actions, List<String> roles, List<String> filters, List<String> filterDefs, List<String> defsParam) {
		this.applicationAcronym = siglaAplicacao;
		this.applicationName = nomeAplicacao;
		this.actions = actions;
		this.roles = roles;
		this.filterDefs = filterDefs;
	}

	public String getApplicationAcronym() {
		return applicationAcronym;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public List<PlcAppMBInfo> getActions() {
		return actions;
	}

	public List<String> getRoles() {
		return roles;
	}

	public boolean isConfigured() {
		return configured;
	}

	public void setConfigured(boolean configured) {
		this.configured = configured;
	}

	public List<String> getFilterDefs() {
		return filterDefs;
	}

}
