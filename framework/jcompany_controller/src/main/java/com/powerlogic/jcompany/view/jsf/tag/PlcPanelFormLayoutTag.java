/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.taglib.core.layout.CorePanelFormLayoutTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.component.PlcPanelFormLayout;
import com.powerlogic.jcompany.view.jsf.renderer.PlcPanelFormLayoutRenderer;

/**
 * Especialização da tag base HtmlCellFormatTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza corpo entre Colunas HTML!
 * @Exemplo <plcf:celula styleClass="cabecalho">   <plcf:titulo tituloChave="label.nome"/>		</plcf:celula> !
 * @Tag !
 */
public class PlcPanelFormLayoutTag extends CorePanelFormLayoutTag {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);
	
	protected String aoSair;

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcPanelFormLayout.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcPanelFormLayoutRenderer.RENDERER_TYPE;
	}

	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

	//

		super.setProperties(bean);
		bean.setProperty(PlcPanelFormLayout.AOSAIR_KEY, aoSair);

//		PlcCelulaAdapter.getInstance().adapter(bean);

	/*	try{
			
		} catch (PlcException e) {
			e.printStackTrace();
		}	
*/

	}
	
	public void setAoSair(String aoSair) {
		this.aoSair = aoSair;
	}

}
