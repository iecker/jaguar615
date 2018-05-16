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
import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreInputTextTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcTextAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcText;
import com.powerlogic.jcompany.view.jsf.renderer.PlcTextRenderer;

/**
 * 
 * Especialização da tag base CoreInputTextTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo com um linha para entrada de dados no formato texto "string".!
 * @Exemplo <plcf:texto id="nome" value="#{plcEntidade.nome}" ajudaChave="ajuda.nome" />!
 * @Tag texto!
 */
public class PlcTextTag extends CoreInputTextTag {
	
	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	* Propriedades já mapeadas
	*/
	protected String tamanho;		
	protected String tamanhoMaximo;	
	protected String ajudaChave;	
	protected String ajuda;
	protected String classeCSS;		
	protected String obrigatorio;	
	protected String obrigatorioDestaque;	
	protected String somenteLeitura;	
	protected String formato;			
	protected String chavePrimaria;
	protected String tabIndex;
	protected String tituloChave;
	protected ValueExpression titulo;
	protected ValueExpression exibeSe;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	protected String colunas;
	protected String numLinha;
	protected String numDet;
	protected String msgErro;
	protected String riaUsa;

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcText.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcTextRenderer.RENDERER_TYPE;
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

		// Propriedades Trinidad
		super.setProperties(bean);
		
		PlcTextAdapter.getInstance().adapter(bean, tamanho, tamanhoMaximo, ajudaChave, ajuda, classeCSS, obrigatorio, 
				obrigatorioDestaque, somenteLeitura,formato, chavePrimaria, tabIndex, tituloChave, titulo, exibeSe, bundle, colunas,
				numLinha, numDet, msgErro, riaUsa);

	}


	public void setAjudaChave(String ajudaChave) {
		this.ajudaChave = ajudaChave;
	}

	public void setChavePrimaria(String chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}

	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}

	public void setColunas(String colunas) {
		this.colunas = colunas;
	}

	public void setExibeSe(ValueExpression exibeSe) {
		this.exibeSe = exibeSe;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

	public void setNumDet(String numDet) {
		this.numDet = numDet;
	}

	public void setNumLinha(String numLinha) {
		this.numLinha = numLinha;
	}

	public void setObrigatorio(String obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public void setSomenteLeitura(String somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public void setTamanhoMaximo(String tamanhoMaximo) {
		this.tamanhoMaximo = tamanhoMaximo;
	}

	public void setTitulo(ValueExpression titulo) {
		this.titulo = titulo;
	}

	public void setTituloChave(String tituloChave) {
		this.tituloChave = tituloChave;
	}
	
	public void setTabIndex(String tabIndex) {
		this.tabIndex = tabIndex;
	}

	public void setAjuda(String ajuda) {
		this.ajuda = ajuda;
	}

	public void setObrigatorioDestaque(String obrigatorioDestaque) {
		this.obrigatorioDestaque = obrigatorioDestaque;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public void setRiaUsa(String riaUsa) {
		this.riaUsa = riaUsa;
	}
}
