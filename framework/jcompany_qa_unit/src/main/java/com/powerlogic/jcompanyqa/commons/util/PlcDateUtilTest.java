/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.commons.util;

import java.util.Date;

import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompanyqa.PlcBaseTestCase;

/**
 * jCompany. Teste PlcDateUtil<p>
 * @since jCompany 2.7.3
 */
public class PlcDateUtilTest extends PlcBaseTestCase{

	//Utilizar CDI?
	//@Inject @QPlcDefault
	PlcDateUtil plcDateUtil;

	public void setUp() throws Exception {
		plcDateUtil = new PlcDateUtil();
	}


	/**
	 * @since jCompany 2.7.3
	 */
	public void testDateNumDays() {
		Date ini = new Date(2003, 11, 1);
		Date fim = new Date(2003, 11, 2);
		assertEquals("Testa soma de um dia", plcDateUtil.addDays(ini, 1), fim);

		ini = new Date(2003, 0, 1);
		fim = new Date(2002, 11, 31);
		assertEquals("Testa subtracao de um dia", plcDateUtil.addDays(ini, -1), fim);

		ini = new Date(2004, 11, 22);
		fim = new Date(2005, 0, 28);
		assertEquals("Testa subtracao de um dia", plcDateUtil.addDays(ini, 37), fim);

	}

	/**
	 * Testa
	 *  - Diferença de um dia deve retornar 1<br>
	 *  - Diferença no mesmo dia deve retornar 0<br>
	 *  - Diferença de um dia 23:59seg para próximo 00:01seg deve retornar 1<br>
	 * @since jCompany 2.7.3
	 *
	 */
	public void testDaysBetweenDates() {
		Date ini = new Date(2003, 12, 1);
		Date fim = new Date(2003, 12, 2);
		assertEquals("Testa diferença de um dia", plcDateUtil.daysBetweenDates(ini, fim), (float) 1, (float) 0);

		assertEquals("Testa dias idênticos", plcDateUtil.daysBetweenDates(new Date(), new Date()), (float) 0, (float) 0);

		Date iniHora24 = new Date(2003, 12, 1, 23, 59, 59);
		Date fimHora24 = new Date(2003, 12, 2, 0, 0, 0);
		assertEquals("Testa diferença de um segundo entre dois dias", plcDateUtil.daysBetweenDates(ini, fim), (float) 1,
				(float) 0);
	}

}
