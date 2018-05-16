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
 *  Classe específica para para personalização de layout. 
 */

@SPlcEntity
@SuppressWarnings("serial")
public class PlcLayout implements Serializable {

    private static final long serialVersionUID = 1L;

    private String layoutDinamico;

    public PlcLayout() {
    	
    }
    
    public String getLayout() {
    	return this.layoutDinamico;
    }

    public void setLayout(String layout) {
    	this.layoutDinamico = layout;
    }

	public String getLayoutDinamico() {
		return layoutDinamico;
	}

	public void setLayoutDinamico(String layoutDinamico) {
		this.layoutDinamico = layoutDinamico;
	}

	public void registraPlcPreferencia(String layoutDinamico) {
		this.layoutDinamico = layoutDinamico;
	}

    
	@Override
	public String toString() {
		return getLayout();
	}
	
}
