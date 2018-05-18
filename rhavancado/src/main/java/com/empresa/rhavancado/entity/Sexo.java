package com.empresa.rhavancado.entity;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum Sexo {
    
	MASC("{sexo.1}"),
	FEMI("{sexo.2}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private Sexo(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
