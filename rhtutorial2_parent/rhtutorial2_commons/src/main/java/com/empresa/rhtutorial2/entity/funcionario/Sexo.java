package com.empresa.rhtutorial2.entity.funcionario;

/**
 * Enum de domnio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum Sexo {
    
	F("{sexo.F}"),
	M("{sexo.M}");

	
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
