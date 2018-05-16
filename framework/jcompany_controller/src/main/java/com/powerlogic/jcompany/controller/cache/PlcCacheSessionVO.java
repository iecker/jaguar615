/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.cache;

import java.io.Serializable;
import java.util.Map;

/**
 * Encapsula parametrizações (preferencias) do usuário, de escopo de sessão. 
 * IMPORTANTE: Toda informação abaixo pressupoe-se que o usuário pode personalizar, do contrário deveria estar em escopo de aplicação
 */
public class PlcCacheSessionVO implements Serializable {

	private static final long serialVersionUID = -7260025275280017970L;
	private String pele;
	private String layout;
	private String formAcaoExibeTexto;
	private String indLayoutReduzido = "N";
	private String indLayoutExibeMenu = "s";
	private String formAlertaAlteracao="S";
	private String formAlertaExclusaoDetalhe="N";
	private String pesquisaRestful="N";
	private Map<String,String> segurancaVerticalAnonimo = null;
	private boolean explorerStatus= true;
	private boolean explorerAtivo = false;
	
	private boolean feedbackUsa = false;
	private String feedbackEndereco = "";
	
	/**
	 * @return Returns the explorerStatus.
	 */
	public boolean isExplorerStatus() {
		return explorerStatus;
	}

	/**
	 * @param explorerAtivo The explorerStatus to set.
	 */
	public void setExplorerStatus(boolean explorerStatus) {
		this.explorerStatus = explorerStatus;
	}

	/**
	 * @return Returns the explorerAtivo.
	 */
	public boolean isExplorerAtivo() {
		return explorerAtivo;
	}

	/**
	 * @param explorerAtivo The explorerAtivo to set.
	 */
	public void setExplorerAtivo(boolean explorerAtivo) {
		this.explorerAtivo = explorerAtivo;
	}

	/**
	 * jCompany 3.0 Formata valores de personalização diversos de form para armazenar em um só cookie
	 */
	public String getFormToCookie() {
		String acaoEstilo = null;
		if(acaoEstilo == null) {
			return "NULL"+"_NULL_"+getFormAcaoExibeTexto()+ "_NULL";
		}
		return acaoEstilo.substring(acaoEstilo.lastIndexOf("/"))+"_NULL_"+getFormAcaoExibeTexto()+ "_NULL_"+getFormAlertaAlteracao()+"_NULL_"+pesquisaRestful;
	}
	
	/**
	 * jCompany 3.0 Formata valores de personalização diversos de form para armazenar em um só cookie
	 */
	public void setCookieToForm(String cookieForm) {
		if (cookieForm != null) {
			int pos = cookieForm.indexOf("_");
			int pos2 = cookieForm.indexOf("_",pos+1);
			if (pos>-1)
				setFormAcaoExibeTexto(cookieForm.substring(pos+1,pos2));
			int pos3 = cookieForm.indexOf("_",pos2+1);
			int pos4 = cookieForm.indexOf("_",pos3+1);
			if (pos3>-1)
				setFormAlertaAlteracao(cookieForm.substring(pos3+2,pos4));
			int pos5 = cookieForm.indexOf("_",pos4+1);
		}
	}

	public PlcCacheSessionVO(){}

	public String getPele(){
		return pele;
	}

	public String getLayout(){
		return layout;
	}

	public String getIndLayoutReduzido(){
		return indLayoutReduzido;
	}

	public void setPele(String newVal){
		pele=newVal;
	}

	public void setLayout(String newVal){
		layout=newVal;
	}

	public void setIndLayoutReduzido(String newVal){
		indLayoutReduzido=newVal;
	}

	public void setSegurancaVerticalAnonimo(Map<String,String> segurancaVerticalAnonimo){
		segurancaVerticalAnonimo=segurancaVerticalAnonimo;
	}

	public Map<String,String> getSegurancaVerticalAnonimo(){
		return segurancaVerticalAnonimo;
	}

	/**
	 * @return String
	 */
	public String getIndLayoutExibeMenu() {
		return indLayoutExibeMenu;
	}

	/**
	 * @param string
	 */
	public void setIndLayoutExibeMenu(String string) {
		indLayoutExibeMenu = string;
	}


	/**
	 * @return Returns the formAcaoExibeTexto.
	 */
	public String getFormAcaoExibeTexto() {
		return formAcaoExibeTexto;
	}

	/**
	 * @param formAcoesExibeTexto The formAcaoExibeTexto to set.
	 */
	public void setFormAcaoExibeTexto(String formAcoesExibeTexto) {
		this.formAcaoExibeTexto = formAcoesExibeTexto;
	}


	/**
	 * @return Returns the formAlertaAlteracao.
	 */
	public String getFormAlertaAlteracao() {
		return formAlertaAlteracao;
	}

	/**
	 * @param formAlertaAlteracao The formAlertaAlteracao to set.
	 */
	public void setFormAlertaAlteracao(String formAlertaAlteracao) {
		this.formAlertaAlteracao = formAlertaAlteracao;
	}

	/**
	 * @return Returns the formAlertaExclusaoDetalhe.
	 */
	public String getFormAlertaExclusaoDetalhe() {
		return formAlertaExclusaoDetalhe;
	}

	/**
	 * @param formAlertaExclusaoDetalhe The formAlertaExclusaoDetalhe to set.
	 */
	public void setFormAlertaExclusaoDetalhe(String formAlertaExclusaoDetalhe) {
		this.formAlertaExclusaoDetalhe = formAlertaExclusaoDetalhe;
	}

	public String getPesquisaRestful() {
		return pesquisaRestful;
	}

	public void setPesquisaRestful(String pesquisaRestful) {
		this.pesquisaRestful = pesquisaRestful;
	}
	
	public boolean isFeedbackUsa() {
		return feedbackUsa;
	}

	public void setFeedbackUsa(boolean feedbackUsa) {
		this.feedbackUsa = feedbackUsa;
	}

	public String getFeedbackEndereco() {
		return feedbackEndereco;
	}

	public void setFeedbackEndereco(String feedbackEndereco) {
		this.feedbackEndereco = feedbackEndereco;
	}

}