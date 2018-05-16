/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.extension.manytomanymatrix.controller;

import javax.enterprise.event.Observes;

import org.apache.commons.lang.ArrayUtils;

import com.powerlogic.jcompany.commons.metamodel.bootstrap.PlcMetamodelUtilBootstrapBefore;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;

/**
 * Observer que registra um novo Caso de Uso
 * 
 * @author Mauren Ginaldo Souza, Rogério Baldini
 * 
 */
public class PlcRegistraCasoDeUsoObserver {

	public void registraCasoDeUso(@Observes PlcMetamodelUtilBootstrapBefore antes)  {

		PlcConfigUtil.SUFIXOS_URL = (String[])ArrayUtils.add(PlcConfigUtil.SUFIXOS_URL, "mmm");
		PlcConfigUtil.NOMES_SUFIXOS_URL = (String[])ArrayUtils.add(PlcConfigUtil.NOMES_SUFIXOS_URL , "MATRIX");

		
	}
	
	
}
