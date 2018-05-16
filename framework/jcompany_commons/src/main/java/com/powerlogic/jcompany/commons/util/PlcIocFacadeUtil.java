/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.commons.util;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;

/**
 * 
 * @author baldini
 *
 */
@SPlcUtil
@QPlcDefault
public class PlcIocFacadeUtil {

	private Logger logger = Logger.getLogger(PlcIocFacadeUtil.class.getCanonicalName());

	@Inject
	@Any
	protected Instance<IPlcFacade> todosFacade;

	@Inject @QPlcDefault
	protected IPlcFacade facadePadrao;
	

	public IPlcFacade getFacade()  {
		return getFacade(IPlcFacade.class);
	}

	/**
	 * Retorna facade que estende de IPlcFacade
	 * @param <T>
	 * @param _interface
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends IPlcFacade> T getFacade(Class<T> _interface)  {

		if (todosFacade == null) {
			return null;
		}

		IPlcFacade facadePadraoPlc = getFacadeDefault();

		if (_interface == IPlcFacade.class) {
			return (T) facadePadraoPlc;
		} else {
			return todosFacade.select(_interface).get();
		}
	}

	/**
	 * Retorna facade específico que não estende de IPlcFacade
	 * @param <T>
	 * @param _interface
	 * @return
	 */
	public <T> T getFacadeSpecific(Class<T> _interface) {
		return PlcCDIUtil.getInstance().getInstanceByType(_interface);
	}

	protected IPlcFacade getFacadeDefault()  {
		return facadePadrao;
	}

}
