/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.view.jsf.util;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

/**
 * Classe criada com functions para auxiliar em manipulações de 
 * dados via EL para Facelets  
 *
 */
@SPlcUtil
@QPlcDefault
public class PlcExecuteUtil {

	@Inject
	private transient Logger log;
	
	public PlcExecuteUtil() {
		
	}
	
	/**
	 * Executa um Método específico de um objeto 
	 * @param obj O objeto que será executado o método
	 * @param method Nome do método
	 * @param args Argumentos relacionados do método
	 * @param tiposArgs Tipos do argumentos, caso seja necessário especificar
	 * @throws ClassNotFoundException 
	 *  
	 */
	public static Object execute (Object obj, String method, Object [] args, String [] tiposArgs) throws ClassNotFoundException{
	
		PlcReflectionUtil reflectionUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcReflectionUtil.class, QPlcDefaultLiteral.INSTANCE); 
		
		Object result = null;
		
		if (obj == null || method == null) {
			return null;
		}
		
		Class [] tiposArgsAux = null;
		
		if (args != null && !ArrayUtils.isEmpty(args)){
			
			if (tiposArgs != null && !ArrayUtils.isEmpty(tiposArgs)){

				tiposArgsAux =  new Class [tiposArgs.length]; 

				for (int i = 0; i < tiposArgs.length; i++) {
					String tipo = tiposArgs[i];
					tiposArgsAux [i] = Class.forName(tipo);
				}

			} else {
				
				tiposArgsAux =  new Class [args.length];
				
				for (int i = 0; i < args.length; i++) {
					Object valor = args[i];
					tiposArgsAux [i] = valor.getClass();
				}
			}

			result = reflectionUtil.executeMethod(obj, method, args, tiposArgsAux);
			
		} else {
			result = reflectionUtil.executeMethod(obj, method);
		}
		
		return result;
	}
	
	/**
	 * Executa um Método específico de um objeto, sem argumento 
	 * @param obj O objeto que será executado o método
	 * @param method Nome do método
	 *  
	 * @throws ClassNotFoundException 
	 */
	public static Object execute (Object obj, String method) throws ClassNotFoundException{
		return execute(obj, method, new Object [] {}, new String [] {});
	}
	
	/**
	 * Executa um Método específico de um objeto, sem argumento 
	 * @param obj O objeto que será executado o método
	 * @param method Nome do método
	 *  
	 * @throws ClassNotFoundException 
	 */
	public static Object execute (Object obj, String method, Object param) throws ClassNotFoundException{
		return execute(obj, method, new Object [] { param }, new String [] {});
	}
	

	/**
	 * Executa um Método específico de um objeto, sem argumento 
	 * @param obj O objeto que será executado o método
	 * @param method Nome do método
	 *  
	 * @throws ClassNotFoundException 
	 */
	public static Object execute (Object obj, String method, Object param1, Object param2) throws ClassNotFoundException{
		return execute(obj, method, new Object [] { param1, param2 }, new String [] {});
	}
	
	/**
	 * Executa um Método específico de um objeto, sem argumento 
	 * @param obj O objeto que será executado o método
	 * @param method Nome do método
	 *  
	 * @throws ClassNotFoundException 
	 */
	public static Object execute (Object obj, String method, Object param1, Object param2, Object param3) throws ClassNotFoundException{
		return execute(obj, method, new Object [] { param1, param2, param3 }, new String [] {});
	}

	/**
	 * 
	 * @param obj
	 * @param method
	 * @param tipoParam
	 * @param param
	 * @return
	 * @throws ClassNotFoundException
	 * 
	 */
	public static Object execute (Object obj, String method, Object param, String tipoParam) throws ClassNotFoundException{
		return execute(obj, method, new Object [] { param }, new String [] { tipoParam });
	}
	
	public static PlcEntityInstance createEntityInstance(Object entidade) {
		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		return metamodelUtil.createEntityInstance(entidade);
	}
	
}
