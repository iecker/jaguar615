/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.rest.producers;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcCurrent;

@RequestScoped
public class PlcContextProducer {

	protected PlcBaseContextVO baseContextVO = null;

	@Inject
	@QPlcDefault
	protected PlcCreateContextUtil contextMontaUtil;

	@Produces
	@QPlcCurrent
	@RequestScoped
	public PlcBaseContextVO getBaseContextVOAtual(HttpServletRequest request, @QPlcCurrent PlcConfigAggregationPOJO configAggregationPOJO, @QPlcCurrent PlcConfigCollaborationPOJO collaborationPOJO) {

		if (baseContextVO == null) {

			baseContextVO = contextMontaUtil.createContextParam(null, configAggregationPOJO, collaborationPOJO, contextMontaUtil.createContextParamMinimum(), request);

			if (baseContextVO.getUserProfile() == null) {
				// Usuário está logado e tem perfil na sessão
				baseContextVO.setUserProfile((PlcBaseUserProfileVO) request.getSession().getAttribute(PlcConstants.PROFILE.USER_INFO_KEY));
				// Cria um básico somente no contextparam, contexto ip e nome se
				// por algum motivo não foi
				// realizada logica de profile
				PlcBaseUserProfileVO perfilUsuario = new PlcBaseUserProfileVO();
				if (request.getUserPrincipal() != null) {
					perfilUsuario.setLogin(request.getUserPrincipal().getName());
				}
				perfilUsuario.setIp(request.getRemoteAddr());
				baseContextVO.setUserProfile(perfilUsuario);
			}
		}

		return baseContextVO;
	}
}
