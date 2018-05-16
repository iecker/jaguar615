/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.component;

import java.io.Serializable;

import javax.faces.component.EditableValueHolder;

/**
 * Estabelece um contrato de algumas propriedades e métodos para os componentes especializados do jsf 
 * @author Pedro Henrique
 */
public interface IPlcComponent extends Serializable {
	
	// Registra o componente referente ao flag desprezar em uma lógica que tem detalhe ou tabular. Caso seja outra lógica o componente desprezar será null.
	public abstract void setPropertyComponenteDesprezar(EditableValueHolder colecaoFlagDesprezar);

	/*
	 * Registra a chave I18n para a propriedade detalhe em uma lógica que tem detalhe. Caso seja outra lógica o valor será null
	 * Obs. padrão:  NOME_MESTRE.def.universal.mestre.NOME_DETALHE.titulo
	 */
	public abstract void setPropertyChaveI18nDetalhe(String chaveI18nDetalhe);

	public abstract String getPropertyChaveI18nDetalhe();
	
	public abstract void addLabel();
}
