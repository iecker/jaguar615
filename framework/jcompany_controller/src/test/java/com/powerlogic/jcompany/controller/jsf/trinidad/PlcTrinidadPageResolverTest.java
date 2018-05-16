/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf.trinidad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

/**
 * Classe teste unitária para o {@link PlcTrinidadPageResolver}
 * 
 * @author george
 *
 */
public class PlcTrinidadPageResolverTest {

	private PlcTrinidadPageResolver pageResolver;
	
	@Before
	public void setUp() throws Exception {
		pageResolver = new PlcTrinidadPageResolver();
	}

	@Test
	public void testNivelAvancado() {
		String viewId = "/n/uftab";
		String actual = pageResolver.getPhysicalURI(viewId);
		String expected = PlcTrinidadPageResolver.FCL_NIVEL_AVANCADO;
		assertEquals(expected, actual);
	}

	@Test
	public void testNivelIntermediario() {
		String viewId = "/l/uftab";
		String actual = pageResolver.getPhysicalURI(viewId);
		String expected = PlcTrinidadPageResolver.FCL_NIVEL_INTERMEDIARIO;
		assertEquals(expected, actual);
	}

	@Test
	public void testNivelApi() {
		String viewId = "/minhapagina.xhtml";
		String actual = pageResolver.getPhysicalURI(viewId);
		String expected = viewId;
		assertEquals(expected, actual);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testArgumentoInvalidoNulo() {
		String viewId = null;
		pageResolver.getPhysicalURI(viewId);
		fail("Deveria jogar uma IllegalArgumentException");
	}

}
