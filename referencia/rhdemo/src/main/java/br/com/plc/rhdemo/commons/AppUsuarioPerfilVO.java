/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.commons;

import javax.enterprise.inject.Specializes;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;

/**
* rhdemo. Implementar aqui atributos de personalizaÃ§Ã£o
* especÃ­ficos do usuario.
*/
@QPlcDefault
@Specializes
public class AppUsuarioPerfilVO extends PlcBaseUserProfileVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
