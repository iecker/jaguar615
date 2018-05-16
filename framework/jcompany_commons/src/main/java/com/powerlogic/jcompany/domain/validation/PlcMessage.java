/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.domain.validation;

import org.apache.commons.lang.StringEscapeUtils;


/**
 * Classe que encapsula mensagens de validação de entrada de dados, das
 * várias camadas, para apresentação unificada.
 * @since jCompany 3.2
 */
public class PlcMessage {

	public enum Cor {
		msgAzulPlc,
		msgVermelhoPlc,
		msgAmareloPlc,
		msgVerdePlc;
		
		public static String getTipo(String cor){
			if(cor.equals(Cor.msgVermelhoPlc.toString())){
				return "erro";
			}
			else if(cor.equals(Cor.msgAzulPlc.toString())){
				return "sucesso";
			}
			else if(cor.equals(Cor.msgAmareloPlc.toString())){
				return "advertencia";
			}
			else if(cor.equals(Cor.msgVerdePlc.toString())){
				return "livre";
			}
			else
				return cor;
		}
	}
	
	public enum ApresentarEm {
		TOPO,
		CAMPO;
	}
	
	private Cor cor = Cor.msgAzulPlc;
	
	private ApresentarEm apresentarEm = ApresentarEm.TOPO;

	/**
	 * Mensagem, já montada no idioma correto, sem nenhum token a ser traduzido
	 */
	private String mensagem;
	
	/**
	 * Propriedade. Se não informado considera validação em nível de classe
	 */
	private String propriedade;

	/**
	 * Se for um erro inesperado, mensagem pode conter o stack trace
	 */
	private String stackTrace;
	
	private String codigoExcecao;
	
	public String getCodigoExcecao() {
		return codigoExcecao;
	}

	public void setCodigoExcecao(String codigoExcecao) {
		this.codigoExcecao = codigoExcecao;
	}

	public String toString() {
		return getMensagem();
	}
	
	public PlcMessage(String mensagem,Cor cor) {
		this.mensagem=mensagem;
		this.cor=cor;
	}
	

	public PlcMessage(String mensagem,String propriedade) {
		this.mensagem=mensagem;
		this.propriedade=propriedade;
	}
	
	public PlcMessage(String mensagem,String propriedade,Cor cor) {
		this.mensagem=mensagem;
		this.cor=cor;
		this.propriedade=propriedade;
	}
	
	public PlcMessage(String mensagem,String propriedade,Cor cor,ApresentarEm apresentarEm,String stackTrace) {
		this.mensagem=mensagem;
		this.apresentarEm=apresentarEm;
		this.cor=cor;
		this.propriedade=propriedade;
		this.stackTrace=stackTrace;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getPropriedade() {
		return propriedade;
	}

	public void setPropriedade(String propriedade) {
		this.propriedade = propriedade;
	}

	public ApresentarEm getApresentarEm() {
		return apresentarEm;
	}

	public void setApresentarEm(ApresentarEm apresentarEm) {
		this.apresentarEm = apresentarEm;
	}

	public Cor getCor() {
		return cor;
	}

	public void setCor(Cor cor) {
		this.cor = cor;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof PlcMessage
				&& ((PlcMessage)obj).cor==this.cor
				&& ((PlcMessage)obj).apresentarEm==this.apresentarEm
				&& equals(((PlcMessage)obj).mensagem,this.mensagem)
				&& equals(((PlcMessage)obj).propriedade,this.propriedade)
				&& equals(((PlcMessage)obj).stackTrace,this.stackTrace);
	}
	
	/**
	 * Método auxiliar para verificação de igualdade.
	 */
	private boolean equals(Object o1, Object o2) {
		return (o1==null && o2==null) || (o1!=null && o2!=null && o1.equals(o2));
	}
	
	public String getScriptMessage() {
		return "\""+ StringEscapeUtils.escapeJavaScript(getMensagem()) +"\"";
	}
	

}

