/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.component.core.nav.CoreCommandButton;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.view.jsf.component.PlcButton;
import com.powerlogic.jcompany.view.jsf.component.PlcDownloadButton;
import com.powerlogic.jcompany.view.jsf.renderer.PlcButtonRenderer;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentPropertiesUtil;
import com.powerlogic.jcompany.view.jsf.util.PlcComponentUtil;

/**
 * Especialização da tag base PlcBotaoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao  Renderiza um botão para baixar o arquivo anexado! 
 * @Exemplo!
 * @Tag botaoDownload!
 */
public class PlcDownloadButtonTag extends PlcButtonTag {
	
	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);
	
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcComponentUtil componentUtil;
	
	@Inject @QPlcDefault 
	protected PlcComponentPropertiesUtil componentPropertiesUtil;
	
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType()
	{
		return PlcDownloadButton.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType()
	{
		return PlcButtonRenderer.RENDERER_TYPE;
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

		

		if (StringUtils.isEmpty(super.getAcao())) 
			setAcao("downloadFile");
		
		String bundle = componentPropertiesUtil.getPropertyBundle(bean);
		if (StringUtils.isEmpty(getLabel())){
			String label = componentUtil.createLocalizedMessage(bean, "jcompany.evt.download", new Object[]{});
			setLabel("jcompany.evt.download");
			bean.setProperty(CoreCommandButton.TEXT_KEY, label);
		}	
		
		setBotaoArrayID("DOWNLOAD");
		bean.setProperty(PlcButton.VALIDA_FORM, Boolean.FALSE);
		
		super.setProperties(bean);
	}

}
