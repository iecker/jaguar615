/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */
package com.empresa.rhtutorial2.commons;

import javax.enterprise.inject.Specializes;

import com.empresa.rhtutorial2.entity.PreferenciaUsuarioEntity;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;

/**
 * rhtutorial2. Implementar aqui atributos de personalização específicos do
 * usuario.
 */
@QPlcDefault
@Specializes
public class AppUserProfileVO extends PlcBaseUserProfileVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PreferenciaUsuarioEntity preferenciaUsuario;

	public PreferenciaUsuarioEntity getPreferenciaUsuario() {
		return preferenciaUsuario;
	}

	public void setPreferenciaUsuario (PreferenciaUsuarioEntity preferenciaUsuario) {
		this.preferenciaUsuario = preferenciaUsuario;
	}

}
