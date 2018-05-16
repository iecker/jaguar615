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
import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreSelectOneChoiceTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcDynamicComboAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcDynamicCombo;
import com.powerlogic.jcompany.view.jsf.renderer.PlcDynamicComboRenderer;

/**
 * Especialização da tag base CoreSelectOneChoiceTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
 * @Descricao Renderiza um campo em forma de lista, para escolha de dados. A lista é dinâmica, com base em uma coleção de registros.!
 * @Exemplo <plcf:comboDinamico id="endereco_uf" value="#{plcEntidade.endereco.uf}" dominio="UfEntity" exibeBranco="S"   />!
 * @Tag comboDinamico!
 */
public class PlcDynamicComboTag extends CoreSelectOneChoiceTag{
	
	protected static final Logger logVisao = Logger.getLogger(PlcConstants.LOGGERs.JCOMPANY_VISAO);

	/*
	* Propriedades já mapeadas
	*/
	protected String tamanho;
	protected String ajudaChave;
	protected String ajuda;
	protected String classeCSS;
	protected String obrigatorio;
	protected String obrigatorioDestaque;	
	protected String somenteLeitura;
	protected String chavePrimaria; 
	protected String tituloChave;
	protected ValueExpression titulo;
	protected ValueExpression dominio;
	protected ValueExpression exibeSe;
	protected String exibeBranco;
	protected String botaoAtualiza;
	protected String propRotulo;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	protected String colunas;
	protected String numLinha;
	protected String msgErro;

	/* Se for utilizar a funcionalidade de 'combosAninhados' (atualizar um campo conforme valor do outro), 
	*  informar as propriedades ligadas aos combos que serão atualizados [separados por ','] Ex: cidade ou minhaPropriedade 
	*/
	protected String navegacaoParaCampos;
	
	// Se o combodinamico é uma combo que vai ser atualizado por outro campo, deve-se informar 'S'
	protected String comboAninhado;
	protected String riaUsa;

	/**
	 * Recupera qual é o componente associado a esta tag
	 */
	@Override
	public String getComponentType() {
		return PlcDynamicCombo.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcDynamicComboRenderer.RENDERER_TYPE;
	}
	
	
	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	protected void setProperties(FacesBean bean) {

		// Trinidad
		super.setProperties(bean);
	    
		PlcDynamicComboAdapter.getInstance().adapter(bean, tamanho, ajudaChave, ajuda, classeCSS, obrigatorio, 
				obrigatorioDestaque, somenteLeitura, chavePrimaria, tituloChave, titulo, dominio, exibeSe, exibeBranco, 
				botaoAtualiza, propRotulo, bundle, colunas, numLinha, msgErro, navegacaoParaCampos, comboAninhado, riaUsa);
		

	}	
		
	public String getAjudaChave() {
		return ajudaChave;
	}

	public void setAjudaChave(String ajudaChave) {
		this.ajudaChave = ajudaChave;
	}

	public void setDominio(ValueExpression dominio){
		this.dominio = dominio;
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

	public void setNumLinha(String numLinha) {
		this.numLinha = numLinha;
	}

	public void setObjetoIndexado(String objetoIndexado) {
//		Método está aqui para compatibilidade anterior.
	}

	public void setSomenteLeitura(String somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}


	public void setTitulo(ValueExpression titulo) {
		this.titulo = titulo;
	}

	public void setTituloChave(String tituloChave) {
		this.tituloChave = tituloChave;
	}
	
	
	public String getBotaoAtualiza() {
		return botaoAtualiza;
	}

	public void setBotaoAtualiza(String botaoAtualiza) {
		this.botaoAtualiza = botaoAtualiza;
	}

	public String getNavegacaoParaCampos() {
		return navegacaoParaCampos;
	}

	public void setNavegacaoParaCampos(String navegacaoParaCampos) {
		this.navegacaoParaCampos = navegacaoParaCampos;
	}

	public String getComboAninhado() {
		return comboAninhado;
	}

	public void setComboAninhado(String comboAninhado) {
		this.comboAninhado = comboAninhado;
	}
	
	public String getExibeBranco() {
		return exibeBranco;
	}

	public void setExibeBranco(String exibeBranco) {
		this.exibeBranco = exibeBranco;
	}

	public void setObrigatorioDestaque(String obrigatorioDestaque) {
		this.obrigatorioDestaque = obrigatorioDestaque;
	}

	public String getPropRotulo() {
		return propRotulo;
	}

	public void setPropRotulo(String propRotulo) {
		this.propRotulo = propRotulo;
	}

	public String getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(String obrigatorio) {
		this.obrigatorio = obrigatorio;
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
