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
import org.apache.myfaces.trinidadinternal.taglib.core.output.CoreOutputLabelTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcShowAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;
import com.powerlogic.jcompany.view.jsf.renderer.PlcTitleRenderer;

/**
  * Especialização da tag base CoreOutputLabelTag para permitir IoC e DI nos componentes JSF/Trinidad.
  * 
  * @Descricao Renderiza um dado (texto) na tela!
  * @Exemplo <plcf:exibe value="#{plcLogicaItens.itensPlc.nome}"/>!
  * @Tag exibe!
 */
public class PlcShowTag extends CoreOutputLabelTag{
	
	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	 * propriedades mapeadas
	 */
	protected String chavePrimaria;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	protected String classeCSS;

	protected String enumI18n;
	
	protected String riaUsa;
	
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcTitle.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcTitleRenderer.RENDERER_TYPE;
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

		

		super.setProperties(bean);
		
		PlcShowAdapter.getInstance().adapter(bean, chavePrimaria, bundle, enumI18n, riaUsa, classeCSS);

	}
	
	public void setValor(ValueExpression valor) {
		this.setValue(valor);
	}
	public void setChavePrimaria(String chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}


	public void setExibeSe(ValueExpression exibeSe) {
		this.setRendered(exibeSe);
	}

	public void setObjetoIndexado(String objetoIndexado) {
//		Método está aqui para compatibilidade anterior.
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public void setEnumI18n(String enumI18n) {
		this.enumI18n = enumI18n;
	}

	public String getRiaUsa() {
		return riaUsa;
	}

	public void setRiaUsa(String riaUsa) {
		this.riaUsa = riaUsa;
	}

	public String getClasseCSS() {
		return classeCSS;
	}

	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}
}
