/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 

package com.powerlogic.jcompany.controller.helper;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import com.powerlogic.jcompany.controller.util.PlcServletRequestUtil;

public class PlcServletRequestUtilTest {

	String				param;

	String				valorParamOriginal;

	String				valorParamEncodeUTF8;

	String				valorParamEncodeISO_8859_1;

	String				queryStringOriginal;

	String				queryStringEncodeUTF8;

	String				queryStringEncodeISO_8859_1;

	HttpServletRequest	containerISO_8859_1_com_URL_UTF8;

	HttpServletRequest	containerUTF8_com_URL_UTF8;

	HttpServletRequest	containerUTF8_com_URL_UTF8_NaoExistente;

	HttpServletRequest	containerUTF8_com_URL_UTF8_Null;

	HttpServletRequest	containerUTF8_com_URL_UTF8_SemQueryString;

	@Before
	public void configuraClasseTeste() throws Exception {

	}

	@Before
	public void configureContainers() throws Exception {

		containerISO_8859_1_com_URL_UTF8 = createMock(HttpServletRequest.class);
		expect(containerISO_8859_1_com_URL_UTF8.getParameter(param)).andReturn(URLDecoder.decode(valorParamEncodeUTF8, "ISO-8859-1"));
		expect(containerISO_8859_1_com_URL_UTF8.getQueryString()).andReturn(queryStringEncodeUTF8);

		containerUTF8_com_URL_UTF8 = createMock(HttpServletRequest.class);
		expect(containerUTF8_com_URL_UTF8.getParameter(param)).andReturn(URLDecoder.decode(valorParamEncodeUTF8, "UTF-8"));
		expect(containerUTF8_com_URL_UTF8.getQueryString()).andReturn(queryStringEncodeUTF8);

		containerUTF8_com_URL_UTF8_NaoExistente = createMock(HttpServletRequest.class);
		expect(containerUTF8_com_URL_UTF8_NaoExistente.getParameter("parametroNaoExistente")).andReturn(null);
		expect(containerUTF8_com_URL_UTF8_NaoExistente.getQueryString()).andReturn(queryStringEncodeUTF8);

		containerUTF8_com_URL_UTF8_Null = createMock(HttpServletRequest.class);
		expect(containerUTF8_com_URL_UTF8_Null.getParameter(null)).andReturn(null);
		expect(containerUTF8_com_URL_UTF8_Null.getQueryString()).andReturn(null);

		containerUTF8_com_URL_UTF8_SemQueryString = createMock(HttpServletRequest.class);
		expect(containerUTF8_com_URL_UTF8_SemQueryString.getParameter(param)).andReturn(URLDecoder.decode(valorParamEncodeUTF8, "UTF-8"));
		expect(containerUTF8_com_URL_UTF8_SemQueryString.getQueryString()).andReturn(null);
	}

	@Before
	public void configureQueryStrings() throws Exception {

		String queryStringTemp = "evento=y&codigoCursos=2&" + param + "=";
		queryStringOriginal = queryStringTemp + valorParamOriginal;
		queryStringEncodeUTF8 = queryStringTemp + URLEncoder.encode(valorParamOriginal, "UTF-8");
		queryStringEncodeISO_8859_1 = queryStringTemp + URLEncoder.encode(queryStringOriginal, "ISO-8859-1");
	}

	@Before
	public void configureParameterValues() throws Exception {

		param = "nomeCursos";
		valorParamOriginal = "Administração ÉéáàÀçãê";
		valorParamEncodeUTF8 = URLEncoder.encode(valorParamOriginal, "UTF-8");
		valorParamEncodeISO_8859_1 = URLEncoder.encode(valorParamOriginal, "ISO-8859-1");
	}

	@Test
	public void testGetParameterValidateCompositeURLEncondingURLs() {

		// Devem ser iguais as combinacoes iguais de URLEnconding com URLs
		replay(containerUTF8_com_URL_UTF8);
		assertEquals(containerUTF8_com_URL_UTF8.getParameter(param), valorParamOriginal);

		// Não devem ser iguais as combinacoes alternadas de URLEnconding com URLs
		replay(containerISO_8859_1_com_URL_UTF8);
		assertFalse(containerISO_8859_1_com_URL_UTF8.getParameter(param).equals(valorParamOriginal));
	}

	@Test
	public void testGetParameterContainerISO_8859_1_with_URL_UTF8() {

		replay(containerISO_8859_1_com_URL_UTF8);
		String valor = new PlcServletRequestUtil().getParameter(containerISO_8859_1_com_URL_UTF8, param, "UTF-8");
		assertEquals(valor, valorParamOriginal);
	}

	@Test
	public void testGetParameterContainerUTF8_With_URL_UTF8() {

		replay(containerUTF8_com_URL_UTF8);
		String valor = new PlcServletRequestUtil().getParameter(containerUTF8_com_URL_UTF8, param, "UTF-8");
		assertEquals(valor, valorParamOriginal);
	}

	@Test
	public void testGetParameterWithNotFoundParameter() {

		replay(containerUTF8_com_URL_UTF8_NaoExistente);
		String valor = new PlcServletRequestUtil().getParameter(containerUTF8_com_URL_UTF8_NaoExistente, "parametroNaoExistente", "UTF-8");
		assertNull(valor);
	}

	@Test(expected = NullPointerException.class)
	public void testGetParameterNull() {

		replay(containerUTF8_com_URL_UTF8_Null);
		String valor = new PlcServletRequestUtil().getParameter(containerUTF8_com_URL_UTF8_Null, null, "UTF-8");
	}

	@Test
	public void testGetParameterByPostNotFoundInQueryString() {

		replay(containerUTF8_com_URL_UTF8_SemQueryString);
		String valor = new PlcServletRequestUtil().getParameter(containerUTF8_com_URL_UTF8_SemQueryString, param, "UTF-8");
		assertEquals(valor, valorParamOriginal);
	}

}
