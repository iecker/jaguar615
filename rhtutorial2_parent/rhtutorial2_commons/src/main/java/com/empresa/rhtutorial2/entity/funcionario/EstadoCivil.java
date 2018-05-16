/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.empresa.rhtutorial2.entity.funcionario;

/**
 * Valores discretos para Estado Civil, com descricoes I18n
 */
public enum EstadoCivil {
    
	S /* estadoCivil.S=Solteiro */,
	C /* estadoCivil.C=Casado */,
	D /* estadoCivil.D=Divorciado */,
	V /* estadoCivil.V=Viúvo */;

	
    /**
     * @return Retorna o codigo.
     */
	public String getCodigo() {
		return this.toString();
	}
	
}
