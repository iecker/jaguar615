/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.modelo.repository;

import javax.enterprise.context.ApplicationScoped;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.model.PlcBaseCreateRepository;

@ApplicationScoped
@QPlcSpecific(name="com.powerlogic.jcompany.persistence.jpa.entity.basic.UserEntity")
public class UsuarioCreateRepository extends PlcBaseCreateRepository {


}
