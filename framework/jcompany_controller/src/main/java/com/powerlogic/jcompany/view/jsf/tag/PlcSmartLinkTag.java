/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.taglib.core.output.CoreOutputLabelTag;

import com.powerlogic.jcompany.view.jsf.adapter.PlcSmartLinkAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcSmartLink;

/**
 * Especialização da tag base CoreOutputLabelTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza números que representam a linha atual em um for de uma coleção!
 * @Exemplo <plcf:contador/>!
 * @Tag contador!
 */
public class PlcSmartLinkTag extends CoreOutputLabelTag{
	
	protected ValueExpression link;		
	protected String alvo;
	protected String ajuda;	
	protected String ajudaChave;	
	protected String tituloChave;
	protected ValueExpression titulo;
	protected ValueExpression exibeSe;
	protected String classeCSS;		
	protected String bundle;
	protected String riaUsa;
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {
		
		super.setProperties(bean);
		
		PlcSmartLinkAdapter.getInstance().adapter(bean, link, alvo, ajuda, ajudaChave,
				tituloChave, titulo, exibeSe, classeCSS, bundle, riaUsa);
				
	}

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcSmartLink.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcSmartLink.RENDERER_TYPE;
	}
	
	public String getAjuda() {
		return ajuda;
	}

	public void setAjuda(String ajuda) {
		this.ajuda = ajuda;
	}

	public String getAjudaChave() {
		return ajudaChave;
	}

	public void setAjudaChave(String ajudaChave) {
		this.ajudaChave = ajudaChave;
	}

	public String getAlvo() {
		return alvo;
	}

	public void setAlvo(String alvo) {
		this.alvo = alvo;
	}

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public String getClasseCSS() {
		return classeCSS;
	}

	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}

	public ValueExpression getLink() {
		return link;
	}

	public void setLink(ValueExpression link) {
		this.link = link;
	}

	public ValueExpression getTitulo() {
		return titulo;
	}

	public void setTitulo(ValueExpression titulo) {
		this.titulo = titulo;
	}

	public String getTituloChave() {
		return tituloChave;
	}

	public void setTituloChave(String tituloChave) {
		this.tituloChave = tituloChave;
	}

	public ValueExpression getExibeSe() {
		return exibeSe;
	}

	public void setExibeSe(ValueExpression exibeSe) {
		this.exibeSe = exibeSe;
	}

}
