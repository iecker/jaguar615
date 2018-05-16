/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.action.util;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.controller.injectionpoint.HttpParam;

@Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_REQUISICAO)
@RequestScoped
public class PlcRequestControl {

	/**
	 * Parametro de request padrão para identificar detalhe corrente.
	 * Este campo atuamente recebe o nome do campo concatenado com '#' para ancoras, para foco em logica de tab-folder, que o seta manualmente. 
	 */
	@Inject @HttpParam("detCorrPlc")
	protected String detCorrPlc;
	
	@Inject @HttpParam("indExcDetPlc")
	protected String indExcDetPlc;
	
	/**
	 * Parametro de conversacao para identificar o fwPlc.
	 */
	@Inject @HttpParam("fwPlc")
	protected String fwPlc="";
	
	/**
	  * Flag para comutação entre modo de visualização ou edicao para documentos com layout de tab-folder dinamico
	  */
	@Inject @HttpParam(PlcConstants.EXIBE_ED_DOC_PLC)
	protected String exibeEditarDocumentoPlc;
	
	public String getDetCorrPlc() {
		return detCorrPlc;
	}

	public void setDetCorrPlc(String detCorrPlc) {
		this.detCorrPlc = detCorrPlc;
	}
	
	public String getIndExcDetPlc() {
		return indExcDetPlc;
	}

	public void setIndExcDetPlc(String indExcDetPlc) {
		this.indExcDetPlc = indExcDetPlc;
	}
	
	public String getExibeEditarDocumentoPlc() {
		return exibeEditarDocumentoPlc;
	}

	public void setExibeEditarDocumentoPlc(String exibeVisualizarDocumentoPlc) {
		this.exibeEditarDocumentoPlc = exibeVisualizarDocumentoPlc;
	}

	public String getFwPlc() {
		return fwPlc;
	}

	public void setFwPlc(String fwPlc) {
		this.fwPlc = fwPlc;
	}

	
}
