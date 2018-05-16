/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

/**
 * Especialização da tag base PlcTextoTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com um linha para mostrar o id do registro atual, no formato texto "string".!
 * @Exemplo <plcf:oid id="cidade_id" autoRecuperacao="S"/>!
 * @Tag oid!
 */
import javax.inject.Inject;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;

import com.powerlogic.jcompany.view.jsf.adapter.PlcOidAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcOid;
import com.powerlogic.jcompany.view.jsf.renderer.PlcOidRenderer;

public class PlcOidTag extends PlcTextTag {
	
	@Inject
	protected transient Logger log;
	
	private String autoRecuperacao = "N";
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcOid.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcOidRenderer.RENDERER_TYPE;
	}
	@Override
	public int doStartTag() throws JspException {
		setId("inibeFoco_" + getId());
		return super.doStartTag();
	}
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {
		if (StringUtils.isBlank(this.tamanho)) {
			setTamanho("8");
		}
		if (StringUtils.isBlank(ajudaChave) && StringUtils.isBlank(ajuda))
			this.ajudaChave = "label.id";
		
		PlcOidAdapter.getInstance().adapter(bean, autoRecuperacao, exibeSe);

		super.setProperties(bean);

		
		
	}
		
		
	public String getAutoRecuperacao() {
		return autoRecuperacao;
	}

	public void setAutoRecuperacao(String autoRecuperacao) {
		this.autoRecuperacao = autoRecuperacao;
	}
}
