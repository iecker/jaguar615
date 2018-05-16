/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.domain.type;

import java.io.Serializable;

/**
 * Dominio discreto para valores tipo 'S' ou 'N'. 
 * Melhor que usar boolean porque permite alteração simples para novos valores, e permite três estados (null, N ou S)
 * @since jCompany 3.1.1
 */
public enum PlcYesNo implements Serializable {
    
	S /* plcYesNo.S=Sim */,
	N /* pcYesNo.N=Não */;

    /**
     * @return Retorna o codigo.
     */
	public String getCodigo() {
		return this.toString();
	}
	
}
