/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package br.com.plc.rhdemo.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.plc.rhdemo.commons.AppUsuarioPerfilVO;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.controller.util.PlcBaseUserProfileUtil;

/**
 * rhdemo . Implementar aqui lÃ³gicas de perfil do usuÃ¡rio
 * da aplicaÃ§Ã£o (user profiling)
 */
@SPlcUtil
@QPlcDefault
public class AppUsuarioPerfilUtil extends PlcBaseUserProfileUtil{
	
	/**
	 * jCompany 6.0: LÃ³gicas de InicializaÃ§Ã£o de Perfil de Usuario
	 * Recebe a requisiÃ§Ã£oo, o perfil do usuÃ¡rio preenchido no ancestral e 
	 * a interface para chamada da persistÃªncia.
	 *
	 * Preencher o objeto de Perfil com informaÃ§Ã£oes especÃ­ficas, especializando-o
	 * se necessÃ¡rio.
	 */
	@Override
	public PlcBaseUserProfileVO registrySpecificProfile(HttpServletRequest request, HttpServletResponse response, PlcBaseUserProfileVO plcPerfilVO) throws Exception {
	
		// Importante transformar o valor de String para Object
		Map<String,Object> m = new HashMap<String,Object>();

		AppUsuarioPerfilVO appUsuarioPerfilVO = (AppUsuarioPerfilVO) plcPerfilVO;

		// Deve estar ao final da montagem do Map
		appUsuarioPerfilVO.getPlcVerticalSecurity().putAll(m);
		// se usuÃ¡rio for Administrador Geral, adicionar essa role no perfil
		if(request.isUserInRole("Administrador Geral Plc")) {
			List grupos = new ArrayList(1);
			grupos.add("Administrador Geral");
			plcPerfilVO.setGroups(grupos);
		}

		// Retorna objeto modificado
		return plcPerfilVO;

		}
}
