/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.tag;

import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreInputHiddenTag;

import com.powerlogic.jcompany.view.jsf.component.PlcHidden;
import com.powerlogic.jcompany.view.jsf.renderer.PlcHiddenRenderer;

/**
 * Especialização da tag base CoreInputHiddenTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo escondido "hidden".!
 * @Exemplo <plcf:escondido id="idAux" />!
 * @Tag escondido!
 */
public class PlcHiddenTag extends CoreInputHiddenTag {

	private String chavePrimaria;
	private String numDet;
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcHidden.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcHiddenRenderer.RENDERER_TYPE;
		
	}
	
	public void setChavePrimaria(String chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}

	public void setNumDet(String numDet) {
		this.numDet = numDet;
	}

	public String getChavePrimaria() {
		return chavePrimaria;
	}

	public String getNumDet() {
		return numDet;
	}

}
