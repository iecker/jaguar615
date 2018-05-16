/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.entity;

/**
 * Enum de domï¿½nio discreto gerada automaticamente pelo assistente do jCompany.
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
