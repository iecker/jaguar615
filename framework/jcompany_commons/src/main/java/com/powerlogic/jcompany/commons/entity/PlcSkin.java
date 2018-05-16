/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.entity;

import java.io.Serializable;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Value Object específico para personalização de pele. 
 */

@SPlcEntity
@SuppressWarnings("serial")
public class PlcSkin implements Serializable {

    private static final long serialVersionUID = 1L;

    private String peleDinamica;

    public PlcSkin() {
    	
    }

    public String getPele() {
    	return this.peleDinamica;
    }

    public void setPele(String pele) {
		this.peleDinamica = pele;
    }

	@Override
	public String toString() {
		return getPele();
	}
}