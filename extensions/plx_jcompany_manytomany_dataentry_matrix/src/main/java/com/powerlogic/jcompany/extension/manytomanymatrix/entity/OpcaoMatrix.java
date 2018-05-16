package com.powerlogic.jcompany.extension.manytomanymatrix.entity;

/**
 * Enum de domínio discreto utilizado como opção de seleção na tela.
 * 
 * @author Mauren Ginaldo Souza
 */
public enum OpcaoMatrix {
    
	TODOS /* opcaoMatrix.TODOS=Todos */,
	MARCADOS /* opcaoMatrix.MARCADOS=Somente Marcados */,
	DESMARCADOS /* opcaoMatrix.DESMARCADOS=Somente Desmarcados */;

	
    /**
     * @return Retorna o codigo.
     */
	public String getCodigo() {
		return this.toString();
	}
	
}
