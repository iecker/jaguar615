package com.empresa.rhrich.entity;

/**
 * Estado Civil do Funcionário
 */
public enum EstadoCivil {
    
	C ("Casado"),
	S ("Solteiro");

	private String label;
    
    private EstadoCivil(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
