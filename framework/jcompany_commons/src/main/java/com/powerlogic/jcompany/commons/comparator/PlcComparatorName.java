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
 * descendentes de PlcEntityInstance por nome. <br>
 * Permite informar a ordem que será aplicada: ascendente (default) ou
 * descendente. <br>
 * Trata valor nulo.
 * 
 * @author Paulo Alvim
 * @author Roberto Badaró
 */
public class PlcComparatorName implements Comparator {

	@Inject
	protected transient Logger log;
	
    /**
     * @since jCompan 3.0
     * Define se a organização será ascendente (1) ou descendente (-1).
     */
    private int ordem = 1;

    /**
     * @since jCompan 3.0
     * Constrói comparador ordenando ascendentemente.
     */
    public PlcComparatorName() {
    }

    /**
     * @since jCompan 3.0
     * Construtor para definir explicitamente qual ordenação será utilizada.
     * 
     * @param orderDesc
     *            <code>TRUE</code> Ordem descendente, <code>FALSE</code>
     *            ascendente (default).
     */
    public PlcComparatorName(boolean orderDesc) {
        if (orderDesc) {
            ordem = -1;
        }
    }

    /**
     * @since jCompan 3.0
     *
     */
    public boolean equals(Object parameter1) {
        return false;
    }

    /**
     * @since jCompan 3.0
     *
     */
    public int compare(Object obj1, Object obj2) {
    	try {
					
	        String nome1 = (String) PropertyUtils.getProperty(obj1,"nome");
	        String nome2 = (String) PropertyUtils.getProperty(obj2,"nome");
	        int retorno = -1;
	
	        if (nome1 != null && nome2 != null) {
	        	if (nome1.compareTo(nome2)>0){
	        		retorno = 1;
	        	}else{
	        		retorno = -1;
	        	}
	        } else if (nome1 == null && nome2 != null) {
	            retorno = 1;
	        } else if (nome1 != null && nome2 == null) {
	            retorno = -1;
	        }
	
	        return (retorno * ordem);
	        
    	} catch (Exception e) {
			log.error( "Erro ao tentar ordenar. Objeto "+obj1.getClass().getName()+" nao possui propriedade nome "+e);
			e.printStackTrace();
			return 0;
		}
    }
}