/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.persistence;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompanyqa.PlcBaseTestCase;

/**
 * Classe base para testes de classes DAO
 * @since jCompany 3.0
 */
public class PlcBasePersistenceDAOTestCase extends PlcBaseTestCase {
	
	private PlcPersistenceDAOUtilTestCase persistenciaUtil = new PlcPersistenceDAOUtilTestCase();

	/**
	 * @return Returns the persistenciaUtil.
	 */
	public PlcPersistenceDAOUtilTestCase getPersistenciaUtil() {
		return persistenciaUtil;
	}

	/**
	 * @param persistenciaUtil The persistenciaUtil to set.
	 */
	public void setPersistenciaUtil(PlcPersistenceDAOUtilTestCase persistenciaUtil) {
		this.persistenciaUtil = persistenciaUtil;
	}

	/**
	 * Permite que se gere um context default incluido em Thread Local, 
	 * com usuário para lógicas de testes de persistencia
	 * que usam acesso aos dados.
	 * @since jCompany 3.04
	 */
	public void setContextDefault() {
		PlcBaseContextVO baseContextVO = new PlcBaseContextVO();
		PlcBaseUserProfileVO perfilVO = new PlcBaseUserProfileVO();
		perfilVO.setLogin("usuario_teste_unidade");
		baseContextVO.setUserProfile(perfilVO);		
	}
	
}
