/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.util.security;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.PlcStringUtil;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;

/**
 * jCompany 3.0 Auxiliar para registro de filtros verticiais (em dados) de segurança
 * @since jCompany 3.0
*/

@QPlcDefault
public class PlcSecurityUtil  {

	@Inject
	protected transient Logger log;

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcStringUtil stringUtil;
	
 	private PlcSecurityUtil() { 
 		
 	}

 	/**
 	 * Recupera filtro vertical de dados do usuário corrente, para uma classe.
 	 * @param session Sessão HTTP
 	 * @param tipoVO Nome da classe do VO com pacote completo
 	 * @return String vazio se não há filtros ou o filtro para a classe, se existir
 	 * 
 	 */
 	public String getUserFilterToClass(HttpSession session,String tipoVO)  {

 	    log.debug( "########## Entrou em recuperaFiltroUsuarioParaClasse");

 	    PlcBaseUserProfileVO perfilUsuarioVO = (PlcBaseUserProfileVO) session.getAttribute(PlcConstants.USER_INFO_KEY);


 	    if (perfilUsuarioVO == null)
 	        return "";
 	    else if (perfilUsuarioVO.getPlcVerticalSecurity() == null)
 	        return "";
 	    else
 	        return (String) perfilUsuarioVO.getPlcVerticalSecurity().get(tipoVO);
 	}
 	
 	/**
	 * JCompany 3.0. Complementa os VOs com registros de filtros de segurança verticais registrados pelo
	 * objeto de profiling
	 */
	public String securityFilter(String path, String indMultiEmpresa, HttpServletRequest request,String tipoVO)
	 {

		String filtro = "";

		log.debug( "################ Entrou em recupera filtro de seguranca para usuario corrente");

		try {

			PlcBaseUserProfileVO perfilUsuarioVO = (PlcBaseUserProfileVO) request.getSession().getAttribute(PlcConstants.USER_INFO_KEY);

			if (perfilUsuarioVO != null &&
				perfilUsuarioVO.getPlcVerticalSecurity() != null &&
				perfilUsuarioVO.getPlcVerticalSecurity().containsKey(tipoVO)) {

				filtro = (String)perfilUsuarioVO.getPlcVerticalSecurity().get(tipoVO);

				String excecao = (String) perfilUsuarioVO.getPlcVerticalSecurity().get(PlcConstants.PROFILE.FILTRO_EXCECOES);

				String acaoExcecao = "";
				boolean eExcecao = false;

				List l = stringUtil.splitSubstringList(excecao);

				if (l != null) {


					Iterator i = l.iterator();

					while (i.hasNext()) {

						acaoExcecao = (String) i.next();

						if (acaoExcecao.equals(path))
						    eExcecao = true;
					}

					if (log.isDebugEnabled())
						log.debug( "eExcecao:" + eExcecao);

					if (eExcecao)
						filtro = "";

				}

				// Testa Multi-Empresa quando usa login padrão J2EE - VO de usuário
				// agregado no VO de cache da sessao
				if (((PlcBaseUserProfileVO) request.getSession().getAttribute(PlcConstants.USER_INFO_KEY)).getCompany() != null &&
						indMultiEmpresa.substring(0,1).equals("S")) {

					if (!filtro.equals("")) filtro += " and ";

					filtro += "idEmpresa="+perfilUsuarioVO.getCompany();

				}


			} else {
				PlcCacheSessionVO plcS = (PlcCacheSessionVO) request.getSession().getAttribute(PlcConstants.SESSION_CACHE_KEY);
				if (plcS.getSegurancaVerticalAnonimo().size() > 0) 
					filtro = (String) plcS.getSegurancaVerticalAnonimo().get(tipoVO);
			}

			// Testa multi-empresa quando não usa login padrão J2EE - VO de usuario direto na sessao
			if (perfilUsuarioVO != null &&
					indMultiEmpresa.substring(0,1).equals("S")) {

				if (!filtro.equals("")) filtro += " and ";

				filtro += "idEmpresa="+perfilUsuarioVO.getCompany();

				if (log.isDebugEnabled())
					log.debug( "%%%%%%%%%%%% Colocou filtro empresa = "+filtro);

			}

			log.debug( "########## Entrou ver se altera filtro para pesquisar pendentes ou inativos");
			// Se for consulta de Pendentes ou Inativos, altera o filtro
			if (request.getAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL) != null &&
				((String)request.getAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL)).equals("pesquisaInativos")) {
				 log.debug( "########## Entrou para alterar filtro para inativos");
				 filtro = StringUtils.replace(filtro,"sitHistoricoPlc='A'","sitHistoricoPlc='I'");
			} else if (request.getAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL) != null &&
			((String)request.getAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL)).equals("pesquisaPendentes")) {
				log.debug( "########## Entrou para alterar filtro para pendentes");
				 filtro = StringUtils.replace(filtro,"sitHistoricoPlc='A'","sitHistoricoPlc='P'");
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
		   throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_COMPLEMENT_VO_INDIVIDUAL, new Object[] {tipoVO,e},e,log);
		}

		if (log.isDebugEnabled()) log.debug( "Saiu com filtro:" + filtro);

		if (filtro == null) filtro = "";

		return filtro;

	}

	
	/**
	 * Recebe uma String com roles separadas por vírgula e testa
	 * se usuário se encontra em alguma delas
	 * @return True se usuário pertence a alguma role do parametro
	 */
	public boolean isUserInRole(HttpServletRequest request,String roles) {

		List l = stringUtil.splitSubstringList(roles);
		Iterator i = l.iterator();
		
		while (i.hasNext()) {
			String role = (String) i.next();
			if (request.isUserInRole(role))
				return true;
		}
		return false;
	}
	
}
