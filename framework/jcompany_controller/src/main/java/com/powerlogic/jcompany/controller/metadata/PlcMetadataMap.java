/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.metadata;

import java.util.HashMap;

public class PlcMetadataMap<K, V> extends HashMap<K, V>{

    @Override
    public V get(Object key) {
    	//FIXME- rever
//    	PlcConfigControleUtil configControleUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcConfigControleUtil.class, QPlcDefaultLiteral.INSTANCE);
//    	return (V) configControleUtil.get(key.toString());
    	return null;
    } 
}
