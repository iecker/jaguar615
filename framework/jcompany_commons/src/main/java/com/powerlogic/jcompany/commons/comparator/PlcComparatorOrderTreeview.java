/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.comparator;

import java.util.Comparator;

import com.powerlogic.jcompany.commons.PlcObjetoNegocioHVO;

/**
 * jCompany 2.5.3. Classe que implementa Comparator para ordenar descendentes de
 * PlcObjetoNegocioVO por nome
 */
public class PlcComparatorOrderTreeview implements Comparator
{

    
    /**
     * @since jCompany 3.0
     *
     */
	public boolean equals( Object parameter1 )
    {
        /** @todo: implement this function */
        return false; 
    }

    /**
     * @since jCompany 3.0
     *
     */
	public int compare(Object obj1, Object obj2) { 

		PlcObjetoNegocioHVO vo1 = (PlcObjetoNegocioHVO) obj1; 

		PlcObjetoNegocioHVO vo2 = (PlcObjetoNegocioHVO) obj2; 

		return vo1.getOrdemPlc().compareTo(vo2.getOrdemPlc()); 

    }
}

