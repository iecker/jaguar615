package com.powerlogic.rhavancado.entity;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum EstadoCivil {
    
	SOLT("{estadoCivil.SOLT}"),
	CASA("{estadoCivil.CASA}"),
	VIUV("{estadoCivil.VIUV}"),
	DISQ("{estadoCivil.DISQ}"),
	DIVO("{estadoCivil.DIVO}");

	
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
