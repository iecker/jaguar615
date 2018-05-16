/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package br.com.plc.jcompany_fcls.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.plc.jcompany_fcls.commons.AppUsuarioPerfilVO;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.controller.util.PlcBaseUserProfileUtil;

/**
 * jcompany_fcls . Implementar aqui lÃ³gicas de perfil do usuÃ¡rio
 * da aplicaÃ§Ã£o (user profiling)
 */
@SPlcUtil
@QPlcDefault
public class AppUserProfileUtil extends PlcBaseUserProfileUtil {
	/**
	 * jCompany 5.0: LÃ³gicas de InicializaÃ§Ã£o de Perfil de Usuario
	 * Recebe a requisiÃ§Ã£o, o perfil do usuÃ¡rio preenchido no ancestral e 
	 * a interface para chamada da persistÃªncia.
	 *
	 * Preencher o objeto de Perfil com informaÃ§Ãµes especÃ­ficas, especializando-o
	 * se necessÃ¡rio.
	 */
	@Override
	public PlcBaseUserProfileVO registrySpecificProfile(HttpServletRequest request,
			 HttpServletResponse response, PlcBaseUserProfileVO plcPerfilVO) throws Exception {
		// Importante transformar o valor de String para Object, do exemplo
		// que vem no jCompany
		Map<String,Object> m = new HashMap<String,Object>();
		
		AppUsuarioPerfilVO profile = PlcCDIUtil.getInstance().getInstanceByType(AppUsuarioPerfilVO.class, QPlcDefaultLiteral.INSTANCE);
		
		//IAppFacade facade = (IAppFacade) plcImpl;
		   
		// Nova clÃ¡usula contratual da aplicacao, para recuperar
		// preferencias a partir do login do usuÃ¡rio
		// MeuPerfil meuPerfil =
		// facade.recuperaMeuPerfil(plcPerfilVO.getLogin());
		AppUsuarioPerfilVO appUsuarioPerfilVO = (AppUsuarioPerfilVO) plcPerfilVO;
		// coloca preferencias genÃ©ricas
		// appUsuarioPerfilVO.setMeuPerfil(meuPerfil);

		/*
			   
		if (meuPerfil != null) {
			// Exemplo multi-empresa
			if (meuPerfil.getEmpresa()!=null)
				appUsuarioPerfilVO.setIdEmpresa(meuPerfil.getEmpresa().getId());
			if (meuPerfil.getDivisao()!=null)
				appUsuarioPerfilVO.setIdSubEmpresa(meuPerfil.getDivisao().getId());
			// coloca os filtros
			if (!request.isUserInRole("Administrador")) {
				if (request.isUserInRole("Empresa"))
					m.put("empresa", meuPerfil.getEmpresa().getId());
				if (request.isUserInRole("EmpresaDivisao")) {
				    m.put("empresa", meuPerfil.getEmpresa().getId());
				    m.put("divisao", meuPerfil.getDivisao().getId());
				}
			}
		}

	   */

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

