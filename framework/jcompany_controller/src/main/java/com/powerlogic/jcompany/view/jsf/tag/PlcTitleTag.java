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
import com.powerlogic.jcompany.view.jsf.adapter.PlcTitleAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcTitle;
import com.powerlogic.jcompany.view.jsf.renderer.PlcTitleRenderer;

/**
 * Especialização da tag base CoreOutputLabelTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza o texto referente ao título na tela.!
 * @Exemplo <plcf:titulo tituloChave="label.descricao"/>!
 * @Tag titulo!
 */
public class PlcTitleTag extends CoreOutputLabelTag{

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	private ValueExpression titulo;
	private String colunas;
	private String classeCSS;
	private String tituloChave;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	
	private String alias;
	private String propOrdenacao;
	private String ordem;
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
		
		PlcTitleAdapter.getInstance().adapter(bean, titulo, colunas, classeCSS, tituloChave, bundle, alias, propOrdenacao, ordem, riaUsa);
		
	}
	

	public void setAlias(String alias) {
		this.alias = alias;
	}
	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}
	public void setColunas(String colunas) {
		this.colunas = colunas;
	}
	public void setExibeSe(ValueExpression exibeSe) {
		this.setRendered(exibeSe);
	}
	public void setOrdem(String ordem) {
		this.ordem = ordem;
	}
	public void setPropOrdenacao(String propOrdenacao) {
		this.propOrdenacao = propOrdenacao;
	}
	public void setTitulo(ValueExpression titulo) {
		this.titulo = titulo;
	}
	public void setTituloChave(String tituloChave) {
		this.tituloChave = tituloChave;
	}
	public void setBundle(String bundle) {
		this.bundle = bundle;
	}
	public String getRiaUsa() {
		return riaUsa;
	}

	public void setRiaUsa(String riaUsa) {
		this.riaUsa = riaUsa;
	}

}
