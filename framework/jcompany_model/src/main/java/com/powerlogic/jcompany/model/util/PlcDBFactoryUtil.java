/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.model.util;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcDbFactory;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;

/**
 * 
 * @author Rogerio Baldini
 * @since jCompany 6.1.3
 *
 */
@SPlcUtil
@QPlcDefault
public class PlcDBFactoryUtil  {
 	
 	public String getDBFactoryName(String dbFactoryAtual, Class entidade, PlcBaseContextVO context){
 		
 		String dbFactory = dbFactoryAtual;
 		
 		if (entidade!=null && StringUtils.isNotEmpty(getDbFactoryName(entidade))){
 			dbFactory = getDbFactoryName(entidade);
 		} else if (context!=null && StringUtils.isNotEmpty(context.getDbFactory())) {
 			dbFactory = context.getDbFactory();
 		}
 		
 		return dbFactory;
 	}
 	
	public String getDbFactoryName(Class<?> classe)  {
		
		try {
			PlcDbFactory fabrica = (PlcDbFactory) classe.getAnnotation(PlcDbFactory.class);
			if (fabrica==null)
				fabrica = (PlcDbFactory) classe.getSuperclass().getAnnotation(PlcDbFactory.class); 
			if (fabrica!=null && fabrica.nome()!=null) {
				return fabrica.nome();
			}
		} catch (Exception e) {
			//Não faz nada, pois retornará o default.
		}
		return null;
	} 	
 	
}
