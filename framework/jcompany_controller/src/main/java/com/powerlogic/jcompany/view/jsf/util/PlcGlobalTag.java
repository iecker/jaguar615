/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.view.jsf.util;

import javax.el.ValueExpression;

/**
 * Classe auxiliar que contem as propriedades comuns a maioria das tags.
 * auxilia no registro de valor das propriedades no setProperties de cada tag.  
 *
 */
public class PlcGlobalTag {
	/*
	 * Propriedades comuns a todas as tags
	 */
	private String propriedade;
	private String classeCSS;
	private String ajuda;
	private String ajudaChave;
	private ValueExpression titulo;
	private String tituloChave;
	private String somenteLeitura;
	private String chavePrimaria;
	private String obrigatorio;
	private String tamanho;
	private String tamanhoMaximo;
	private String bundle;
	private ValueExpression exibeSe;
	
	// Instancia a classe passando todos os paramentros
	public PlcGlobalTag( String propriedade, String classeCSS, String ajuda, 
			 			 String ajudaChave, ValueExpression titulo, String tituloChave, 
			 			 String somenteLeitura, String chavePrimaria, String obrigatorio, 
			 			 String tamanho, String tamanhoMaximo, String bundle, ValueExpression exibeSe){
		
		this.propriedade = propriedade;
		this.classeCSS=classeCSS;
		this.ajuda = ajuda;
		this.ajudaChave = ajudaChave;
		this.titulo = titulo;
		this.tituloChave = tituloChave;
		this.somenteLeitura = somenteLeitura;
		this.chavePrimaria = chavePrimaria;
		this.obrigatorio = obrigatorio;

		this.tamanho = tamanho;
		this.tamanhoMaximo = tamanhoMaximo;
		this.bundle	= bundle;
		this.exibeSe = exibeSe;
		
	}
	/*
	 * Setters das propriedades
	 */
	public String getAjuda() {
		return ajuda;
	}
	public String getAjudaChave() {
		return ajudaChave;
	}
	public String getChavePrimaria() {
		return chavePrimaria;
	}
	public String getClasseCSS() {
		return classeCSS;
	}
	public String getObrigatorio() {
		return obrigatorio;
	}
	public String getPropriedade() {
		return propriedade;
	}
	public String getSomenteLeitura() {
		return somenteLeitura;
	}
	public String getTamanho() {
		return tamanho;
	}
	public String getTamanhoMaximo() {
		return tamanhoMaximo;
	}
	public ValueExpression getTitulo() {
		return titulo;
	}
	public String getTituloChave() {
		return tituloChave;
	}
	public String getBundle() {
		return bundle;
	}
	public void setBundle(String bundle) {
		this.bundle = bundle;
	}
	public ValueExpression getExibeSe() {
		return exibeSe;
	}

}
