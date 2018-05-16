/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.tag;

import javax.el.ValueExpression;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidadinternal.taglib.core.input.CoreInputTextTag;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.view.jsf.adapter.PlcAggregateAdapter;
import com.powerlogic.jcompany.view.jsf.component.PlcAggregate;
import com.powerlogic.jcompany.view.jsf.renderer.PlcAggregateRenderer;

/**
 * Especialização da tag base CoreInputTextTag para permitir IoC e DI nos componentes JSF/Trinidad.
 * 
* @Descricao Renderiza um campo e botão para seleção popup de classes agregadas à classe principal! 
* A seleção popup pode ser um outro action de seleção da mesma aplicação ou mesmo de outra aplicação (outro WAR). Além disso, um mesmo action utilizado como seleção convencional pode ser reutilizado para seleção popup.!
* @Exemplo <plcf:vinculado id="departamentoPai" value="#{plcEntidade.departamentoPai}" propSelPop="departamentoPai" idExibe="S" idSomenteLeitura="N" autoRecuperacaoClasse="com.powerlogic.teste.entidade.DepartamentoEntity" actionSel="departamentosel" ajudaChave="ajuda.departamentoPai"  onkeydown="selecionaPorTecla(event,this);"/>!	
* @Tag vinculado!
*/
public class PlcAggregateTag extends CoreInputTextTag {
	
	protected static final Logger log = Logger.getLogger(PlcAggregateTag.class.getCanonicalName());
	
	protected ValueExpression propSelPop;
	protected ValueExpression propsSelPop;
	protected String limpaPropsSelPop;
	protected String multiSel;
	protected String multiSelTitulo;
	protected String tituloBotaoSelPop;
	protected String tituloChaveBotaoSelPop;
	protected String tituloBotaoLimpar;
	protected String tituloChaveBotaoLimpar;
	protected String lookupTamanho="20";
	protected String idTamanho = "5";
	protected String classeCSS;
	protected String colunas;
	protected String idExibe = "S";
	protected String idSomenteLeitura;
	protected String obrigatorio;
	protected String obrigatorioDestaque;	
	protected String autoRecuperacaoClasse;
	protected String autoRecuperacaoPropriedade;
	protected String ajaxIdUnico;
	protected String tamanho;			
	protected String tamanhoMaximo = "5";
	protected String chavePrimaria;
	protected String baseActionSel;
	protected ValueExpression actionSel;
	protected String evento;
	protected ValueExpression parametro;
	protected String modal;
	protected String delimitador;
	protected String alt = "500";
	protected String larg = "600";
	protected String posx = "10";
	protected String posy = "10";
	/* atributos do campo lookup */
	protected String ajudaChave;
	protected String ajuda;
	
	protected String tituloChave;
	protected ValueExpression titulo;
	protected ValueExpression exibeSe;
	// Arquivo de Resources. Se não foi informado o padrão é ApplicationResources
	protected String bundle;
	
	protected String exibeBotaoLimpar;
	
	protected ValueExpression riaUsa;
	
	protected String somenteLeitura;	
	
	protected String transacaoUnica;

	/**
	 * Recupera qual é o componente associado a esta tag
	 */	
	@Override
	public String getComponentType() {
		return PlcAggregate.COMPONENT_TYPE;
	}

	/**
	 *  Recupera qual é o renderer associado a esta tag
	 */
	@Override
	public String getRendererType() {
		return PlcAggregateRenderer.RENDERER_TYPE;
	}

	/**
	 *  Registrando valores para propriedades específicas da tag
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void setProperties(FacesBean bean) {

		

		// Propriedades Trinidad
		super.setProperties(bean);
		
		PlcAggregateAdapter.getInstance().adapter(bean, propSelPop, propsSelPop, limpaPropsSelPop, multiSel, multiSelTitulo, 
				tituloBotaoSelPop, tituloChaveBotaoSelPop, tituloBotaoLimpar, tituloChaveBotaoLimpar, lookupTamanho, idTamanho, 
				classeCSS, colunas, idExibe, idSomenteLeitura, obrigatorio, obrigatorioDestaque, autoRecuperacaoClasse, 
				autoRecuperacaoPropriedade, ajaxIdUnico, idTamanho, tamanhoMaximo, chavePrimaria, baseActionSel, actionSel, 
				evento, parametro, alt, larg, posx, posy, ajudaChave, ajuda, tituloChave, titulo, exibeSe, bundle, exibeBotaoLimpar, riaUsa, somenteLeitura, modal, delimitador, transacaoUnica);
		
	}

	public ValueExpression getActionSel() {
		return actionSel;
	}

	public void setActionSel(ValueExpression actionSel) {
		this.actionSel = actionSel;
	}

	public String getAjaxIdUnico() {
		return ajaxIdUnico;
	}

	public void setAjaxIdUnico(String ajaxIdUnico) {
		this.ajaxIdUnico = ajaxIdUnico;
	}

	public String getAjudaChave() {
		return ajudaChave;
	}

	public void setAjudaChave(String ajudaChave) {
		this.ajudaChave = ajudaChave;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getAutoRecuperacaoClasse() {
		return autoRecuperacaoClasse;
	}

	public void setAutoRecuperacaoClasse(String autoRecuperacaoClasse) {
		this.autoRecuperacaoClasse = autoRecuperacaoClasse;
	}

	public String getAutoRecuperacaoPropriedade() {
		return autoRecuperacaoPropriedade;
	}

	public void setAutoRecuperacaoPropriedade(String autoRecuperacaoPropriedade) {
		this.autoRecuperacaoPropriedade = autoRecuperacaoPropriedade;
	}

	public String getBaseActionSel() {
		return baseActionSel;
	}

	public void setBaseActionSel(String baseActionSel) {
		this.baseActionSel = baseActionSel;
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

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public void setExibeSe(ValueExpression exibeSe) {
		this.exibeSe = exibeSe;
	}

	public String getIdExibe() {
		return idExibe;
	}

	public void setIdExibe(String idExibe) {
		this.idExibe = idExibe;
	}

	public String getIdSomenteLeitura() {
		return idSomenteLeitura;
	}

	public void setIdSomenteLeitura(String idSomenteLeitura) {
		this.idSomenteLeitura = idSomenteLeitura;
	}

	public String getLarg() {
		return larg;
	}

	public void setLarg(String larg) {
		this.larg = larg;
	}

	public String getLookupTamanho() {
		return lookupTamanho;
	}

	public void setLookupTamanho(String lookupTamanho) {
		this.lookupTamanho = lookupTamanho;
	}

	public void setObjetoIndexado(String objetoIndexado) {
		//Método está aqui para compatibilidade anterior.
	}

	public String getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(String obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public ValueExpression getParametro() {
		return parametro;
	}

	public void setParametro(ValueExpression parametro) {
		this.parametro = parametro;
	}

	public String getPosx() {
		return posx;
	}

	public void setPosx(String posx) {
		this.posx = posx;
	}

	public String getPosy() {
		return posy;
	}

	public void setPosy(String posy) {
		this.posy = posy;
	}


	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public String getTamanhoMaximo() {
		return tamanhoMaximo;
	}

	public void setTamanhoMaximo(String tamanhoMaximo) {
		this.tamanhoMaximo = tamanhoMaximo;
	}

	public ValueExpression getPropSelPop() {
		return propSelPop;
	}

	public void setPropSelPop(ValueExpression propSelPop) {
		this.propSelPop = propSelPop;
	}

	public ValueExpression getPropsSelPop() {
		return propsSelPop;
	}

	public void setPropsSelPop(ValueExpression propsSelPop) {
		this.propsSelPop = propsSelPop;
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

	public String getAjuda() {
		return ajuda;
	}

	public void setAjuda(String ajuda) {
		this.ajuda = ajuda;
	}

	public void setObrigatorioDestaque(String obrigatorioDestaque) {
		this.obrigatorioDestaque = obrigatorioDestaque;
	}

	public void setChavePrimaria(String chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}

	public String getBundle() {
		return bundle;
	}

	public void setBundle(String bundle) {
		this.bundle = bundle;
	}

	public String getLimpaPropsSelPop() {
		return limpaPropsSelPop;
	}

	public void setLimpaPropsSelPop(String limpaPropsSelPop) {
		this.limpaPropsSelPop = limpaPropsSelPop;
	}

	public String getTituloBotaoSelPop() {
		return tituloBotaoSelPop;
	}

	public void setTituloBotaoSelPop(String tituloBotaoSelPop) {
		this.tituloBotaoSelPop = tituloBotaoSelPop;
	}

	public String getTituloChaveBotaoSelPop() {
		return tituloChaveBotaoSelPop;
	}

	public void setTituloChaveBotaoSelPop(String tituloChaveBotaoSelPop) {
		this.tituloChaveBotaoSelPop = tituloChaveBotaoSelPop;
	}

	public String getTituloBotaoLimpar() {
		return tituloBotaoLimpar;
	}

	public void setTituloBotaoLimpar(String tituloBotaoLimpar) {
		this.tituloBotaoLimpar = tituloBotaoLimpar;
	}

	public String getTituloChaveBotaoLimpar() {
		return tituloChaveBotaoLimpar;
	}

	public void setTituloChaveBotaoLimpar(String tituloChaveBotaoLimpar) {
		this.tituloChaveBotaoLimpar = tituloChaveBotaoLimpar;
	}

	public String getObrigatorioDestaque() {
		return obrigatorioDestaque;
	}

	public String getChavePrimaria() {
		return chavePrimaria;
	}

	public String getExibeBotaoLimpar() {
		return exibeBotaoLimpar;
	}

	public void setExibeBotaoLimpar(String exibeBotaoLimpar) {
		this.exibeBotaoLimpar = exibeBotaoLimpar;
	}

	public String getIdTamanho() {
		return idTamanho;
	}

	public void setIdTamanho(String idTamanho) {
		this.idTamanho = idTamanho;
	}

	public void setMultiSel (String multiSel){
		this.multiSel = multiSel;
	}
	
	public String getMultiSel(){
		return this.multiSel;
	}

	public String getMultiSelTitulo() {
		return multiSelTitulo;
	}

	public void setMultiSelTitulo(String multiSelTitulo) {
		this.multiSelTitulo = multiSelTitulo;
	}

	public void setRiaUsa(ValueExpression riaUsa) {
		this.riaUsa = riaUsa;
	}
	
	public String getSomenteLeitura() {
		return somenteLeitura;
	}

	public void setSomenteLeitura(String somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}

	
	public String getModal() {
		return modal;
	}

	public void setModal(String modal) {
		this.modal = modal;
	}

	public String getDelimitador() {
		return delimitador;
	}
	public void setDelimitador(String delimitador) {
		this.delimitador = delimitador;
	}
	
	public String getTransacaoUnica() {
		return transacaoUnica;
	}

	public void setTransacaoUnica(String transacaoUnica) {
		this.transacaoUnica = transacaoUnica;
	}
}
