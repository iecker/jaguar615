/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.taglib.html.HtmlCellFormatTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcCellAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcCell;
import com.powerlogic.jcompany.view.jsf.renderer.PlcCellRenderer;

/**
 * Especialização da tag base HtmlCellFormatTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza corpo entre Colunas HTML!
 * @Exemplo <plcf:celula styleClass="cabecalho">   <plcf:titulo tituloChave="label.nome"/>		</plcf:celula> !
 * @Tag !
 */
public class PlcCellTag extends HtmlCellFormatTag {

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcCell.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcCellRenderer.RENDERER_TYPE;
	}

	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

		super.setProperties(bean);

		PlcCellAdapter.getInstance().adapter(bean);
	}

}
