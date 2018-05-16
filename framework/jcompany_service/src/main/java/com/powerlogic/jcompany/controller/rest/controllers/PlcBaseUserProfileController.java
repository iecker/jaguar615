package com.powerlogic.jcompany.controller.rest.controllers;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.util.PlcBaseUserProfileUtil;

/**
 * Controle que recupera os recursos negados para o usuário corrente.
 * /service/security
 */
@SPlcController
@QPlcControllerName("userprofile")
public class PlcBaseUserProfileController<AppBaseUserProfileVO, Long> extends PlcBaseController<AppBaseUserProfileVO, Long> {

	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;

	 
	
	@Override
	@SuppressWarnings("unchecked")
	public void retrieveCollection() {

		PlcBaseUserProfileUtil plcProfile = PlcCDIUtil.getInstance().getInstanceByType(PlcBaseUserProfileUtil.class, QPlcDefaultLiteral.INSTANCE);

		plcProfile.registryUserProfile(request, response);			
		AppBaseUserProfileVO usuario =  (AppBaseUserProfileVO) request.getSession().getAttribute(PlcConstants.USER_INFO_KEY);
		setEntity(usuario);
		
	}


}