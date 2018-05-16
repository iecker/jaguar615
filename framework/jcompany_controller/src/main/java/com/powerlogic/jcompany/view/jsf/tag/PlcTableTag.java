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
import org.apache.myfaces.trinidadinternal.taglib.html.HtmlTableLayoutTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcTableAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcTable;
import com.powerlogic.jcompany.view.jsf.renderer.PlcTableRenderer;

/**
 * Especialização da tag base HtmlTableLayoutTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza tags HTML de uma tabela que envolve os componentes de um tela.!
 * @Exemplo <plcf:tabela tituloChave="consultafuncionariocon.titulo">!
 * @Tag tabela!
 */
public class PlcTableTag extends HtmlTableLayoutTag{
	
	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	private String classeCSS;
	private String tituloChave;
	private String titulo;
	private String fragmento;
	private String classeTituloCSS;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	protected String aoSair;
	protected String usaFieldset;
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcTable.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcTableRenderer.RENDERER_TYPE;
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {
		
		

		// Trinidad
		super.setProperties(bean);
		
		PlcTableAdapter.getInstance().adapter(bean, classeCSS, tituloChave, titulo, fragmento, classeTituloCSS, bundle, aoSair, usaFieldset);
		
	}


	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}


	public void setExibeSe(ValueExpression exibeSe) {
		this.setRendered(exibeSe);
	}


	public void setFragmento(String fragmento) {
		this.fragmento = fragmento;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setTituloChave(String tituloChave) {
		this.tituloChave = tituloChave;
	}
	
	public void setClasseTituloCSS(String classeTituloCSS) {
		this.classeTituloCSS = classeTituloCSS;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public void setAoSair(String aoSair) {
		this.aoSair = aoSair;
	}

	public String getUsaFieldset() {
		return usaFieldset;
	}
	
	public void setUsaFieldset(String usaFieldset) {
		this.usaFieldset = usaFieldset;
	}
}
