/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.model;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompanyqa.PlcBaseTestCase;

/**
 * Testes para a camada Modelo
 * @since jCompany 2.7.3
 */

public class PlcModelTest extends PlcBaseTestCase {

	protected PlcBaseContextVO contextMock = new PlcBaseContextVO();

	/**
	 * @since jCompany 2.7.3
	 */
	public void testExclui() {

		Object vo = new Object();

//		vo.setId(new Long(1));

		PlcBaseRepository bo = new PlcBaseRepository();

		try {

			bo.delete(contextMock, vo);

		} catch (Exception e) {
			fail("Erro teste de exclusao. Excecao fatal " + e.getMessage()
					+ " do tipo " + e.getClass());
		}
	}

	/**
	 * @since jCompany 2.7.3
	 */
	public void testAltera() {

		Object vo = new Object();

//		vo.setId(new Long(1));

//		voAnt.setId(new Long(1));

		PlcBaseRepository bo = new PlcBaseRepository();

		try {

			assertEquals(vo, bo.update(contextMock, vo));

		} catch (Exception e) {
			fail("Erro teste de alteracao. Excecao fatal " + e.getMessage()
					+ " do tipo " + e.getClass());
		}
	}

	/**
	 * @since jCompany 2.7.3
	 */
	public void testInclui() {

		Object vo = new Object();

		PlcBaseRepository bo = new PlcBaseRepository();

		try {

			//assertEquals(new Long(1), bo.inclui(vo).getId());

		} catch (Exception e) {
			fail("Erro teste de inclusao. Excecao fatal " + e.getMessage()
					+ " do tipo " + e.getClass());
		}

	}

	/**
	 * @since jCompany 2.7.3
	 */
	public void testRecupera() {

		Object vo = new Object();

		PlcBaseRepository bo = new PlcBaseRepository();

		try {

//			assertEquals(new Long(1), ((Object[]) bo.recupera(vo
//					.getClass(), "1"))[0]).getId());

		} catch (Exception e) {
			fail("Erro teste de recuperacao. Excecao fatal " + e.getMessage()
					+ " do tipo " + e.getClass());
		}
	}
}
