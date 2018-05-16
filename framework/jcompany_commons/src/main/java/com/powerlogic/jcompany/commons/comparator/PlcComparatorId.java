/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons.comparator;

import java.util.Comparator;

import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

/**
 * jCompany 2.5.3. Comparator. Classe que implementa Comparator para ordenar
 * descendentes de PlcEntityInstance por id. <br>
 * Permite informar a ordem que será aplicada: ascendente (default) ou
 * descendente. <br>
 * Trata valor nulo.
 * 
 * @author Paulo Alvim
 * @author Roberto Badaró
 */
public class PlcComparatorId implements Comparator {



    /**
     * Define se a organização será ascendente (1) ou descendente (-1).
     */
    private int ordem = 1;


    /**
     * @since jCompan 3.0
     *
     */
    public PlcComparatorId() {

    }


    /**
     * @since jCompan 3.0
     *
     */
    public PlcComparatorId(boolean orderDesc) {

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

    	PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);    	
    	
    	PlcEntityInstance obj1Instance = metamodelUtil.createEntityInstance(obj1);
    	PlcEntityInstance obj2Instance = metamodelUtil.createEntityInstance(obj2);
    	
        Long id1 = obj1Instance.getId();
        Long id2 = obj2Instance.getId();

        int retorno = -1;



        if (id1 != null && id2 != null) {
        	if (id1.compareTo(id2)>0){
        		retorno = 1;
        	}else{
        		retorno = -1;
        	}
        	
        } else if (id1 == null && id2 != null) {

            retorno = 1;

        } else if (id1 != null && id2 == null) {

            retorno = -1;

        }



        return (retorno * ordem);

    }

}



