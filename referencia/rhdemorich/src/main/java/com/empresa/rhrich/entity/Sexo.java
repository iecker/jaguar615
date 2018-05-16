package com.empresa.rhrich.entity;

/**
 * Sexo do Funcionário.
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
