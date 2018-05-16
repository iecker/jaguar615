/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.empresa.rhtutorial2.controller;


import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.empresa.rhtutorial2.commons.AppBaseContextVO;
import com.empresa.rhtutorial2.commons.AppUserProfileVO;
import com.empresa.rhtutorial2.entity.PreferenciaUsuario;
import com.empresa.rhtutorial2.entity.PreferenciaUsuarioEntity;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.controller.util.PlcBaseUserProfileUtil;

/**
 * rhtutorial2 . Implementar aqui lógicas de perfil do usuário da aplicação (user profiling)
 */
@SPlcUtil
@QPlcDefault
public class AppUserProfileUtil extends PlcBaseUserProfileUtil {
	
	@Inject @QPlcDefault
	protected IPlcFacade iPlcFacade;
	
	/**
	 * jCompany 6.0: Lógicas de Inicialização de Perfil de Usuario
	 * Recebe a requisição, o perfil do usuário preenchido no ancestral e 
	 * a interface para chamada da persistência.
	 *
	 * Preencher o objeto de Perfil com informações específicas, especializando-o
	 * se necessário.
	 */
	@Override
	public PlcBaseUserProfileVO registrySpecificProfile(HttpServletRequest request,
			 HttpServletResponse response, PlcBaseUserProfileVO plcPerfilVO) throws Exception {
	
		AppUserProfileVO appUsuarioPerfilVO = (AppUserProfileVO) plcPerfilVO;

		String urlRedirect = montaPreferenciaNoPerfilUsuario (appUsuarioPerfilVO);

		request.setAttribute("prefUsuarioRedirect", urlRedirect);

		return plcPerfilVO;
	}
	
	private String montaPreferenciaNoPerfilUsuario (AppUserProfileVO appUsuarioPerfilVO) {
		//Informa argumento no próprio objeto
		PreferenciaUsuario preferenciaArg = new PreferenciaUsuarioEntity();	
		preferenciaArg.setLogin(appUsuarioPerfilVO.getLogin());

		//Recupera preferencia do usuário reutilizando método do Jaguar
		//Obs.: O objeto context passa metadados importantes entre camadas
		List<PreferenciaUsuarioEntity> l = (List<PreferenciaUsuarioEntity>)iPlcFacade.findList (new AppBaseContextVO(), preferenciaArg, null, 0, 1);
		//Devolve URLs para redirecionamento
		if (l==null || l.size()==0)
			return "/f/n/preferenciausuario";
		else {
			PreferenciaUsuarioEntity preferenciaUsuario = l.get(0);
			appUsuarioPerfilVO.setPreferenciaUsuario(preferenciaUsuario);
			return preferenciaUsuario.getFormularioInicial();
		}
	}
}
