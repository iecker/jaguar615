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
 * Classe utilitária, que faz a transformação de Objetos e Entidades para o
 * formato JSON.
 * 
 * TODO - Gerar JSONObject no lugar de String.
 */
@SPlcUtil
@QPlcDefault
@RequestScoped
public class PlcJsonRestRenderUtil extends PlcBaseRestRendererUtil {

	@Override
	public String getOpenItemString() {
		return "";
	}

	@Override
	public String getCloseItemString() {
		return ",";
	}

	@Override
	public String getOpenCollectionString() {
		return "[";
	}

	@Override
	public String getCloseCollectionString() {
		return "]";
	}

	@Override
	public String getOpenObjectString() {
		return "{";
	}

	@Override
	public String getCloseObjectString() {
		return "}";
	}

	@Override
	public String getNullString() {
		return "null";
	}

	@Override
	public String getLineBreakString() {
		return "\n";
	}

	@Override
	public String getQuotesString() {
		return "\"";
	}


	@Override
	public String getOpenPropertyString() {
		return ":";
	}

	@Override
	public String getClosePropertyString() {
		return ",";
	}


	@Override
	public String getOpenDocumentString() {
		return "";
	}

	@Override
	public String getCloseDocumentString() {
		return "";
	}

	@Override
	protected void fixLast(StringBuilder out) {
		// Fim do objeto, Limpa a ultima virgula.
		if (out.charAt(out.length() - 1) == ',') {
			out.setLength(out.length() - 1);
		}
	}
	

}
