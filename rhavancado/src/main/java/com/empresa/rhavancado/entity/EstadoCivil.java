package com.empresa.rhavancado.entity;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum EstadoCivil {
   
	S("{estadoCivil.S}"),
	C("{estadoCivil.C}"),
	D("{estadoCivil.D}"),
	V("{estadoCivil.V}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private EstadoCivil(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
