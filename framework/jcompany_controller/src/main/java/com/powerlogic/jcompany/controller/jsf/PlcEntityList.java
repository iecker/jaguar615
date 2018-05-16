/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;
import java.util.List;

import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;

/**
 * Value Object que armazena uma lista de entidades ({@link PlcEntityInstance}). 
 * 
 * É instanciado pelo factory method {@link PlcBaseMB#newObjectList()}.
 *
 */
public class PlcEntityList implements Serializable {
	
	private static final long serialVersionUID = -4850264872168563154L;
	
	/**
	 * Controle para Navegador, indicando qual a página selecionada. 
	 */
	protected String selectedPage;
	
	/**
	 * Número dinâmico de registros
	 */
    protected String dynRegNumber;
    
	/**
	 * Lista de itens resultado da seleção ou da lógica tabular.
	 */
	private List<Object> itensPlc;

	public PlcEntityList() {
		super();
	}

	public List<Object> getItensPlc() {
		return itensPlc;
	}

	public void setItensPlc(List<Object> itensPlc) {
		this.itensPlc = itensPlc;
	}
	
	
	public String getSelectedPage() {
		return selectedPage;
	}

	public void setSelectedPage(String paginaSelecionada) {
		this.selectedPage = paginaSelecionada;
	}
	
	public String getDynRegNumber() {
		return dynRegNumber;
	}

	public void setDynRegNumber(String numRecDin) {
		this.dynRegNumber = numRecDin;
	}	

}