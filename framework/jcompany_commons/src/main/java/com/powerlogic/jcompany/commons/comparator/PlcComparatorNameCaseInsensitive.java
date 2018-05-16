/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.comparator;


import java.util.Comparator;

import javax.inject.Inject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
/**
 * jCompany 2.5.3. Comparator. Classe que implementa Comparator para ordenar
 * descendentes de PlcObjetoNegocioVO por nome case insensitive
 * @author Paulo Alvim
 */
public class PlcComparatorNameCaseInsensitive implements Comparator {

	@Inject
	protected transient Logger log;
	
    /**
     * @since jCompany 3.0
     *
     */
	public boolean equals( Object parameter1 )  {
        return false; 
    }

    /**
     * @since jCompany 3.0
     *
     */
	public int compare(Object obj1, Object obj2) { 

   	try {
		return ((String)PropertyUtils.getProperty(obj1,"nome")).toLowerCase().compareTo(
				((String)PropertyUtils.getProperty(obj2,"nome")).toLowerCase()); 
		
    } catch (Exception e) {
		log.error( "Erro ao tentar ordenar. Objeto "+obj1.getClass().getName()+" nao possui propriedade nome "+e);
		e.printStackTrace();
		return 0;
	}

    }
}

