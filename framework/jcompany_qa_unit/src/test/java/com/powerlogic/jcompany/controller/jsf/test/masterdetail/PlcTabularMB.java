/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.test.masterdetail;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.controller.jsf.PlcBaseMB;

@PlcConfigAggregation(
		entity = UfEntity.class
	)

@PlcConfigForm(
	
		behavior = @PlcConfigBehavior(rememberDetail = true)
)

@SPlcMB
@PlcUriIoC("uftab")
public class PlcTabularMB extends PlcBaseMB {

}
