/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.inject.Inject;

import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;

/**
 * Especialização da tag base PlcTituloTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza uma coluna de título para caixas de exclusão.!
 * @Exemplo <plcf:tituloExclusao/>!
 * @Tag tituloExclusao!
 */
public class PlcTitleExclusionTag extends PlcTitleTag{
	
	private String caminhoImagem;
	
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

		super.setProperties(bean);
		
		PlcConfigApplication controleAplicacao;
		try {
			
			controleAplicacao = configUtil.getConfigApplication().application();
			this.caminhoImagem = "//fcls//midia//ico_lixeira.gif";
			
		} catch (PlcException e1) {
			e1.printStackTrace();
		}
		
		String contextPath = contextUtil.getRequest().getContextPath();
		bean.setProperty(PlcTitle.CAMINHO_IMAGEM_KEY, contextPath+this.caminhoImagem);

	}
	
	public void setCaminhoImagem(String caminhoImagem) {
		this.caminhoImagem = caminhoImagem;
	}
}
