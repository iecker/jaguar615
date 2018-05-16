/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.commons.util;

import java.math.BigDecimal;
import java.util.Date;

import javax.inject.Inject;

import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcExceptionHandlerUtil;
import com.powerlogic.jcompanyqa.PlcBaseTestCase;
import com.powerlogic.jcompanyqa.commons.mock.PlcBaseVOMock;

/**
 * Teste para PlcentityCommonsUtil
 * @since jCompany 2.7.3
 */
public class PlcVOUtilTest extends PlcBaseTestCase {

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject 
	protected PlcEntityCommonsUtil entityCommonsUtil;

	/**
	 * jCompany 3.0 DP Composite. Devolve o serviço POJO de tratamento de exceções
	 * @since jCompany 2.7.3
	 */
	protected PlcExceptionHandlerUtil getExceptionHandlerUtil() {
		try {
			return (PlcExceptionHandlerUtil) PlcCDIUtil.getInstance().getInstanceByType(PlcExceptionHandlerUtil.class, QPlcDefaultLiteral.INSTANCE);
		} catch (Exception e) {
			System.out.println("Erro nao tratavel no tratamento de excecao. Erro: "+e);
			return null;
		}
	}
	
	PlcBaseVOMock baseVOFilho = new PlcBaseVOMock();

	Date dataHoje = new Date();

	/**
	 * - Testa se VO é preenchido corremente do Array
	 * @since jCompany 2.7.3
	 *
	 */
	public void testFillVOWithObjectArray() {

		try {

			// Se preenche Ok
			baseVOFilho = (PlcBaseVOMock) entityCommonsUtil.fillEntityByObjectArray(baseVOFilho, new String[] { "umDate", "umLong",
					"umDouble", "umBigDecimal", "umString", "umVO" }, new Object[] { dataHoje, new Long(1), new Double(2),
					new BigDecimal(1.2), "alfa", new PlcBaseVOMock("teste") });

			PlcBaseVOMock outroVO = new PlcBaseVOMock(new Long(1), "alfa", dataHoje, new BigDecimal(1.2), new Double(2),
					new PlcBaseVOMock("teste"));

			assertEquals(true, baseVOFilho.equals(outroVO));

			// VOs vazios
			baseVOFilho = new PlcBaseVOMock();
			outroVO = new PlcBaseVOMock();

			baseVOFilho = (PlcBaseVOMock) entityCommonsUtil.fillEntityByObjectArray(baseVOFilho, new String[] {}, new Object[] {});

			assertEquals(true, baseVOFilho.equals(outroVO));

		} catch (Exception e) {
			fail("Não deveria dar exceção '" + e + "' em método 'testPreencheVOComArrayObject'"
					+ getExceptionHandlerUtil().stackTraceToString(e, false));
		}

	}

	/**
	 * - Testa exceções do método
	 * @since jCompany 2.7.3
	 */
	public void testFillVOWithObjectArrayException() {

		try {

			try {
				// Testa disparo de exceção
				baseVOFilho = (PlcBaseVOMock) entityCommonsUtil.fillEntityByObjectArray(baseVOFilho, new String[] { "umDate",
						"umLong", "umDouble", "umBigDecimal", "umString", "umVO" }, new Object[] {});

				fail("Deveria dar exceção em método 'testPreencheVOComArrayObjectExcessao'");

			} catch (PlcException plcExc) {
			}

			try {
				// Testa disparo de exceção
				baseVOFilho = (PlcBaseVOMock) entityCommonsUtil.fillEntityByObjectArray(baseVOFilho, null, null);

				fail("Deveria dar exceção em método 'testPreencheVOComArrayObjectExcessao'");

			} catch (PlcException plcExc) {
			}

		} catch (Exception e) {
			fail("Não deveria dar exceção '" + e + "' em método 'testPreencheVOComArrayObject'"
					+ getExceptionHandlerUtil().stackTraceToString(e, false));
		}

	}

}
