/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa;

import junit.framework.TestCase;
import junitx.util.PrivateAccessor;

import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.persistence.PlcBaseDAO;

/**
 * jCompany 2.7 Ancestral para Testes de Unidade Todas as classes de teste de
 * unidade devem estender essa classe ou suas subclasses.
 * @since jCompany 2.7.3
 */
public class PlcBaseTestCase extends TestCase {

	
	/**
	 * jCompany 2.7. Registra DAO Mock para uso em teste
	 * @since jCompany 2.7.3
	 */
	
	public void registraMockDAO(PlcBaseDAO mockDAO) {
		//PlcPersistenciaLocator dao = PlcPersistenciaLocator.getInstance();
		//dao.registra(mockDAO.getClass(), mockDAO);
	}
    
	/**
	 * jCompany 2.7. Registra BO Mock para uso em teste
	 * @since jCompany 2.7.3
	 */
	
	public void registraMockBO(PlcBaseRepository mockBO) {
//		PlcModeloLocator bo = PlcModeloLocator.getInstance();
//		bo.registra(mockBO.getClass(), (PlcBaseBC)mockBO);
	}
    
	/**
	 * @since jCompany 2.7.3
	 */
	
	public void registraMockDAO(Class classeChave, PlcBaseDAO mockDAO) {
//		PlcPersistenciaLocator dao = PlcPersistenciaLocator.getInstance();
//		dao.registra(classeChave, mockDAO);
	}
	

	/**
	 * jCompany 2.7. Registra BO Mock para uso em teste
	 * @since jCompany 2.7.3
	 */
	
	public void registraMockBO(Class classeChave, PlcBaseRepository mockBO) {
//		PlcModeloLocator bo = PlcModeloLocator.getInstance();
//		bo.registra(classeChave, (PlcBaseBC)mockBO);
	}
	

	/**
	 * jCompany 2.7, Chama métodos privados
	 * 
	 * @since jCompany 2.7.3
	 * @param obj
	 *            instância da classe sendo testada
	 * @param nomeMetodo
	 *            nome do método privado
	 * @param classesDosArgumentos
	 *            Conjuntos de Classes de cada argumentos, na ordem
	 * @param valoresDosArgumentos
	 *            Conjuntos de valores de cada argumento, na ordem
	 * @return objeto de retorno, se houver.
	 */
	protected Object chamaMetodoPrivado(Object obj, String nomeMetodo,
			Class[] classesDosArgumentos, Object[] valoresDosArgumentos)
			throws Throwable {

		return PrivateAccessor.invoke(obj, nomeMetodo, classesDosArgumentos,
				valoresDosArgumentos);

	}

}
