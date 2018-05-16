/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcBaseContextUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigModule;

/**
 * Auxilar para composição do 'contexto' para envio de informações do cliente web para a camada modelo em POJO. 
 * Clase ancestral para camada controle.
 */

@SPlcUtil
@QPlcDefault
public class PlcBaseContextControllerUtil extends PlcBaseContextUtil {

	@Inject @QPlcDefault
	protected PlcCacheUtil cacheUtil;

	@Inject @QPlcDefault
	protected PlcConfigUtil configUtil;

	/**
	 * 
	 * @return
	 * 
	 */
 	public PlcBaseContextVO createContextParamMinimum()  {
 		
 		PlcBaseContextVO context = PlcCDIUtil.getInstance().getInstanceByType(PlcBaseContextVO.class, QPlcDefaultLiteral.INSTANCE);
		
 		context.setExecutionMode(ObjectUtils.toString(cacheUtil.getObject(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO), "D")); 		
 	    
 	    return context;
 	}
	
 	/**
 	 * 
 	 * @return
 	 * 
 	 */
 	public String[] getModulesAcronym()  {
 		
		String arrayModulos[] = null;
		PlcConfigApplication configAplicacao = configUtil.getConfigApplication().application();
		if (configAplicacao != null){
			PlcConfigModule[] modulos = configAplicacao.modules();
			if (modulos != null){
				arrayModulos = new String[modulos.length];
				for(int i=0; i < modulos.length; i++){
					arrayModulos[i] = modulos[i].acronym();
				}
			}
		}	
		
		return arrayModulos;
   }
 	
 	/**
 	 * 
 	 * @param request
 	 * @param context
 	 * @param evento
 	 * 
 	 */
 	public void createOriginalAction(HttpServletRequest request,PlcBaseContextVO context, String evento)  {
			
		// Registra ação original clicada pelo usuário (nome dos métodos. Ex: aprova, reprova, pesquisa, etc..
		// Refactoring PlcBaseContextMontaUtil para pacote comuns: Obtem parametros necessarios de request, sessao, etc.
		request.setAttribute(PlcConstants.WORKFLOW.IND_ACAO_ORIGINAL,evento);

		// Refactoring PlcBaseContextMontaUtil para pacote comuns: Delega para nova classe do pacote comuns
		createOriginalAction(context, evento);
 	}
	
    /**
     * Monta dados básicos. Para casos onde não haja action mapping e nem formBean.
     *
     * @param context
     * @param request
     * 
     */
    public void createMiscellanea(PlcBaseContextVO context, HttpServletRequest request)  {
       
        log.debug( "############# Entrou em montaMiscelania");

  		
		// Refactoring PlcBaseContextMontaUtil para pacote comuns: Obtem parametros necessarios de request, sessao, etc.
        String siglaAplicacao = configUtil.getConfigApplication().application().definition().acronym();
        
    	context.setExecutionMode(request.getSession().getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO));

    	context.setAppAcronym(siglaAplicacao);
               
    } 	
    
    /**
     * 
     * @param context
     * @param request
     * 
     */
	public void createProfileRequest(PlcBaseContextVO context,HttpServletRequest request)  {

		log.debug( "############# Entrou em montaProfile");
		
 		HttpSession session = request.getSession();
		PlcBaseUserProfileVO voPerfilUsuario = (PlcBaseUserProfileVO) session.getAttribute(PlcConstants.PROFILE.USER_INFO_KEY);
		Principal userPrincipal = request.getUserPrincipal();
		String ipUsuario = request.getRemoteAddr();

		// Refactoring PlcBaseContextMontaUtil para pacote comuns: Delega para nova classe do pacote comuns
		createProfileRequest(context, voPerfilUsuario, userPrincipal, ipUsuario);
			  			
	}
 	
}
