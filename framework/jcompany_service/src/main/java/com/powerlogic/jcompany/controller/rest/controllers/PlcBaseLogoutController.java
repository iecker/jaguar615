package com.powerlogic.jcompany.controller.rest.controllers;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;

/**
 * Serviço Rest que retorna, dado um usuario e senha, se ele será autenticado.
 * @author Bruno Carneiro
 *
 * @param <E>
 * @param <I>
 */

@SPlcController
@QPlcControllerName("logout")
public class PlcBaseLogoutController <E, I> extends PlcBaseController<E, I> {
	
	@Inject
	private HttpServletRequest request;
	
	/**
	 * Invalida sessão de usuário
	 */
	@Override
	public void retrieveCollection() {
		request.getSession().removeAttribute(PlcConstants.USER_INFO_KEY); 
		request.getSession().invalidate();	
	}
	
}
