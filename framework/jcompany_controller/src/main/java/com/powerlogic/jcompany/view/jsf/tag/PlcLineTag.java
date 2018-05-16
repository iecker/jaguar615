/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.taglib.html.HtmlRowLayoutTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcLineAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcLine;
import com.powerlogic.jcompany.view.jsf.renderer.PlcLineRenderer;

/**
 * Especialização da tag base HtmlRowLayoutTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza corpo entre linhas HTML.!
 * @Exemplo <plcf:linha>conteudo</plcf:linha>!
 * @Tag linha!
 */
public class PlcLineTag extends HtmlRowLayoutTag{

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcLine.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcLineRenderer.RENDERER_TYPE;
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {
		

		// Trinidad
		super.setProperties(bean);
		
		PlcLineAdapter.getInstance().adapter(bean);
		

	}

	public void setExibeSe(ValueExpression exibeSe) {
		this.setRendered(exibeSe);
	}
}

