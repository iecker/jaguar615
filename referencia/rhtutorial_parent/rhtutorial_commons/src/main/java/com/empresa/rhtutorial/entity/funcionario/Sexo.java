package com.empresa.rhtutorial.entity.funcionario;

/**
 * Sexo do FuncionÃ¡rio.
 */
public enum Sexo {
    
	M ("{sexo.M}"),
	F ("{sexo.F}");

	private String label;
    
    private Sexo(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
