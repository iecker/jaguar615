/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompanyqa.factory;

import static org.easymock.classextension.EasyMock.createMock;

/**
 * Classe de apoio aos testes com jUnit. Cria Mocks para algumas classes.
 * @author Pedro Henrique
 *
 */
public class PlcMockFactory {
	
	/**
	 * Retorna uma instância da classe que será testada.
	 */
	public static <T extends Object> T  getInstanceClasseTeste(Class<T> classe){
		try{
			return classe.newInstance();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}

	}
	/**
	 * Cria um mock para a classe informada
	 */
	public static <T>T getMock(Class<T> classObjeto){
		return createMock(classObjeto);
	}
		

	
}
