/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/
package com.empresa.cargosalario.entity;

import javax.persistence.MappedSuperclass;

import com.powerlogic.jcompany.domain.PlcBaseMapEntity;
import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;

@MappedSuperclass
@PlcDbFactory(nome="default_module",autoDetectDialect=true)
public abstract class CssBaseEntity extends PlcBaseMapEntity {

}
