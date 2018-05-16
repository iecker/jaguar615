/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.rest.conversors;

import javax.enterprise.context.RequestScoped;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;

/**
 * Classe utilitária que faz a transformação de Objetos e Entidades para o formato HTML.
 * @author Bruno Carneiro
 * @since Jaguar 6.0.0 Final
 *
 */
@SPlcUtil
@QPlcDefault
@RequestScoped
public class PlcHtmlRestRendererUtil extends PlcBaseRestRendererUtil  {

	@Override
	public String getOpenItemString() {
		return "<li>";
	}

	@Override
	public String getCloseItemString() {
		return "</li>";
	}

	@Override
	public String getOpenCollectionString() {
		return "<ul>";
	}

	@Override
	public String getCloseCollectionString() {
		return "</ul>";
	}

	@Override
	public String getOpenObjectString() {
		return "";
	}

	@Override
	public String getCloseObjectString() {
		return "";
	}

	@Override
	public String getNullString() {
		return " nenhum";
	}

	@Override
	public String getLineBreakString() {
		return "<br/>";
	}

	@Override
	public String getQuotesString() {
		return "";
	}

	@Override
	public String getOpenPropertyString() {
		return ": ";
	}

	@Override
	public String getClosePropertyString() {
		return ", ";
	}

	@Override
	public String getOpenDocumentString() {
		return "<html>";
	}

	@Override
	public String getCloseDocumentString() {
		return "</html>";
	}

}
