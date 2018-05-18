/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information in your installation directory. 
*  Contact Powerlogic for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br  */  
package com.powerlogic.consumer.commons;

import javax.enterprise.inject.Specializes;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;

/**
* consumer. Implementar aqui atributos de personalização
* específicos do usuario. PlcUserProfile
*/
@QPlcDefault
@Specializes 
public class AppUserProfileVO extends PlcBaseUserProfileVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
