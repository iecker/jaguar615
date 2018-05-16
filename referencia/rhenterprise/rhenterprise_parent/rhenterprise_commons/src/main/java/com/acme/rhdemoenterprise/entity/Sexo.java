package com.acme.rhdemoenterprise.entity;

/**
 * Enum de domÃ­nio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum Sexo {
    
	 
		M("{sexo.M}"),
		F("{sexo.F}");

		
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
