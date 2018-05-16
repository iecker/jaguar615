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
import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreSelectOneRadioTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcRadioAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcRadio;
import com.powerlogic.jcompany.view.jsf.renderer.PlcRadioRenderer;

/**
 * Especialização da tag base CoreSelectOneRadioTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza uma área com campos de marcação, escolha de itens, para entrada de dados.!
 * @Exemplo <plcf:radio id="sexo" value="#{plcLogicaItens.argumentos.sexo.valor}" dominio="Sexo" />!
 * @Tag radio!
 */
public class PlcRadioTag extends CoreSelectOneRadioTag{

	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	protected ValueExpression dominio;
	private String obrigatorio;
	protected String obrigatorioDestaque;	
	private String somenteLeitura;
	private String classeCSS;
	private String ajudaChave;
	private String ajuda;
	protected ValueExpression exibeSe;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	private String chavePrimaria;
	private String colunas;
	private ValueExpression titulo;
	private String tituloChave;
	protected String riaUsa;
	
	
	
	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcRadio.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcRadioRenderer.RENDERER_TYPE;
	}
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean){

		// Trinidade
		super.setProperties(bean);
		
		PlcRadioAdapter.getInstance().adapter(bean, dominio, obrigatorio, obrigatorioDestaque, somenteLeitura, classeCSS, ajudaChave,
				ajuda, exibeSe, bundle, chavePrimaria, colunas, titulo, tituloChave, riaUsa);
		
	}
	

	public ValueExpression getDominio() {
		return dominio;
	}

	public void setDominio(ValueExpression dominio) {
		this.dominio = dominio;
	}

	public String getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(String obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public String getChavePrimaria() {
		return chavePrimaria;
	}

	public void setChavePrimaria(String chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}

	public String getClasseCSS() {
		return classeCSS;
	}

	public void setClasseCSS(String classeCSS) {
		this.classeCSS = classeCSS;
	}

	public String getColunas() {
		return colunas;
	}

	public void setColunas(String colunas) {
		this.colunas = colunas;
	}

	public void setExibeSe(ValueExpression exibeSe) {
		this.exibeSe = exibeSe;
	}

	public void setObjetoIndexado(String objetoIndexado) {
//		Método está aqui para compatibilidade anterior.
	}

	public String getSomenteLeitura() {
		return somenteLeitura;
	}

	public void setSomenteLeitura(String somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
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

	public String getAjudaChave() {
		return ajudaChave;
	}

	public void setAjudaChave(String ajudaChave) {
		this.ajudaChave = ajudaChave;
	}

	public String getAjuda() {
		return ajuda;
	}

	public void setAjuda(String ajuda) {
		this.ajuda = ajuda;
	}

	public void setObrigatorioDestaque(String obrigatorioDestaque) {
		this.obrigatorioDestaque = obrigatorioDestaque;
	}

	public String getBundle() {
		return bundle;
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
