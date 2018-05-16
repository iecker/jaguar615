/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.empresa.controller.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.controller.util.PlcBaseUserProfileUtil;

/**
 * Bridge. Implementar aqui lógicas de perfil do usuário da aplicação
 */
@SPlcUtil
@QPlcDefault
abstract public class EmpBaseUserProfileUtil extends PlcBaseUserProfileUtil {

	private static final long serialVersionUID = 1L;

	/**
	 * jCompany 1.0: Lógicas de Inicialização de Perfil de Usuario Recebe a
	 * requisição, o perfil do usuário preenchido no ancestral e a interface
	 * para chamada da persistência.
	 * 
	 * Preencher o objeto de Perfil com informações específicas,
	 * especializando-o se necessário.
	 */
	@Override
	public PlcBaseUserProfileVO registrySpecificProfile(HttpServletRequest request, HttpServletResponse response, PlcBaseUserProfileVO plcPerfilVO) throws Exception {
		log.info("Entrou em registraPerfilUsuario");

		Map<String, String> m = new HashMap<String, String>();

		if (request.isUserInRole("DEMO2")) {
			// m.put(com.empresa.app.vo.tabular.TipoCursoVO.class.getName(),"obj.idioma='N'");
			log.info("Registrou restricoes para usuario rh");
		}

		plcPerfilVO.getPlcVerticalSecurity().putAll(m);

		// se usuário for Administrador Geral, adicionar essa role no perfil
		if (request.isUserInRole("Administrador Geral")) {
			List grupos = new ArrayList(1);
			grupos.add("Administrador Geral");
			plcPerfilVO.setGroups(grupos);
		}

		// Retorna objeto modificado
		return plcPerfilVO;
	}

}
