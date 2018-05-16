/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.controller.util.PlcViewControllerUtil;

/**
 * Componente para o serviço de Visão.
 * @author Bruno Grossi
 *
 */
@Specializes
public class PlcViewJsfUtil extends PlcViewControllerUtil {

	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;

	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;
	
	@Inject @QPlcDefault
	protected PlcReflectionUtil reflectionUtil;
	
	public static final String DISABLED = "disabled";

	/**
 	 * jCompany 5.0 Versão que injeta o request
 	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, ou de chave de título
 	 */
	public void hide(String nomeObjeto)  {
		HttpServletRequest request = contextUtil.getRequest();
		hide(request,nomeObjeto,null);
	}

	/**
 	 * jCompany 5.2 Versão que injeta o request
	 * @param nomeObjetoAcao Rótulo do botão ou link que não deve ser exibido, utilizando internacionalização (I18N)
 	 */
	public void hideAction(String nomeObjetoAcao)  {
		HttpServletRequest request = contextUtil.getRequest();
		hideAction(request,nomeObjetoAcao);
	}
	
	/**
 	 * jCompany 5.0  Versão que injeta o request
 	 * @param nomeObjeto Nome da propriedade cujo campo respectivo não deve ser exibido, bem como labels e campos auxiliarres correlatos.
 	 */
	public void hideWithLabels( String nomeObjeto)  {

		HttpServletRequest request = contextUtil.getRequest();
		hideWithLabel(request,nomeObjeto,null);
	}
	
	/**
	 * Compõe opções de arquivo anexado 
	 */
	public void handleFileAttach(String nomeColaboracao)  {
		
		HttpServletRequest request = contextUtil.getRequest();
		
		PlcConfigAggregationPOJO pojo = configUtil.getConfigAggregation(nomeColaboracao);
		
		if (pojo!=null){ 
			
			Field [] camposAnotados = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(pojo.entity(), PlcFileAttach.class);
			
			for (Field field : camposAnotados) {
				PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);
				
				if (fileAttach != null && fileAttach.image()){
					request.setAttribute(PlcConstants.ARQUIVO.IND_IMAGEM, "S");
				}
			}
		}
	}

	
	/**
	 * Registra se um componente pode ou não ser alterado, ou seja se tem que ser readonly ou não readonly
	 * @param chaveComponete, chave do componente no mapa de segunrança
	 * @param objetoIndexado Se o Objeto for de uma coleção informe a coleção
	 * @param value, flag de readonly ou não.
	 * 
	 * Observação: É colocado no mapa de segurança com o valor objetoIndexado.chavaComponente_disabled
	 */
	public void registerReadonlyComponent(String chaveComponete,String objetoIndexado, boolean value ) {
		
		StringBuilder objetoIndexadoSeguranca = new StringBuilder();
		objetoIndexadoSeguranca.append(objetoIndexado!=null?objetoIndexado:"").append(".").append(chaveComponete);

		Map<String, Boolean> m = getSecurityMap();

		boolean temMapaSeguranca = m != null;
		if ( !temMapaSeguranca)
			m = new HashMap<String, Boolean>();

		if ( value)
			m.put(objetoIndexadoSeguranca + "_" + DISABLED, true);
		else
			m.remove(objetoIndexadoSeguranca + "_" + DISABLED);

		if (!temMapaSeguranca){
			HttpServletRequest request 	= contextUtil.getRequest();
			request.setAttribute(PlcConstants.GUI.MAPA_SEGURANCA,m);
		}
			
			
	}


	/**
	 * Recupera o Mapa de segurança. Se não existir retorno Null.
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Boolean> getSecurityMap() {
		
		HttpServletRequest request 	= contextUtil.getRequest();
		Map<String,Boolean> m 		= (Map<String,Boolean>) request.getAttribute(PlcConstants.GUI.MAPA_SEGURANCA);
		return m;
	}
	
	public boolean textMode() {
		HttpServletRequest request = contextUtil.getRequest();
		if ( 
				(request.getParameter("mcPlc")!=null && request.getParameter("mcPlc").toString().equalsIgnoreCase("t")) ||
				(request.getParameter("mcPlc")!=null && request.getParameter("mcPlc").toString().equalsIgnoreCase("texto")) ||
				(request.getAttribute("visualizaCampoPlc")!=null && request.getAttribute("visualizaCampoPlc").toString().equalsIgnoreCase("t")) ||
				(request.getAttribute("exibeEdDocPlc")!=null && request.getAttribute("exibeEdDocPlc").toString().equalsIgnoreCase("s")) 				
			)
			return true;
		else 
			return false;
	}
	
	public boolean isReadOnly(){
		HttpServletRequest request = contextUtil.getRequest();
		if ( 
				(request.getParameter("visualizaCampoPlc")!=null && request.getParameter("visualizaCampoPlc").toString().equalsIgnoreCase("p")) ||
				(request.getParameter("visualizaCampoPlc")!=null && request.getParameter("visualizaCampoPlc").toString().equalsIgnoreCase("protegido")) ||
				(request.getAttribute("visualizaCampoPlc")!=null && request.getAttribute("visualizaCampoPlc").toString().equalsIgnoreCase("p")) ||
				(request.getAttribute("visualizaCampoPlc")!=null && request.getAttribute("visualizaCampoPlc").toString().equalsIgnoreCase("protegido")) 				
			)
			return true;
		else 
			return false;
		
	}
	
	/**
	 * Método responsável pelo tratamento dos botões de acordo com a requisição,
	 * com o uso do {@link PlcViewControllerUtil}.
	 * 
	 * @param configUrl
	 * 
	 */
	public void handleButtonsAccordingUseCase()  {

		String nomeAction = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_COM_BARRA);

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcConfigAggregationPOJO configGrupoAgregacao = configUtil.getConfigAggregation(url);
		PlcConfigCollaborationPOJO configGrupoControle = configUtil.getConfigCollaboration(url);
		handleButtonsAccordingFormPattern(
				contextUtil.getRequest(), 
				configGrupoAgregacao.pattern().formPattern(),
				configGrupoControle.print().smartPrint() ? "S" : "N",
				configGrupoControle.behavior().windowMode(), nomeAction, null,
				configGrupoAgregacao.details().length, "S",
				configGrupoAgregacao.pattern().modality().toString(),
				configGrupoControle.help().urlHelpConcept(),
				configGrupoControle.rssSelection().useRss(),
				configUtil.getConfigApplication().behavior().useRestfulSearch()

		);

		if (nomeAction.contains("/esquema")) {
			handleSchemaButtons();
		} else if (nomeAction.contains("/personalizar")) {
			handleCustomizeButtons();
		}

	}

	/**
	 * Método para tratamento específico no caso da personalização.
	 */
	protected void handleCustomizeButtons() {
		Map<String, Object> requestMap = contextUtil.getRequestMap();
		requestMap.put(PlcConstants.LOGICAPADRAO.GERAL.AJAX_USA, false);
		requestMap.put(PlcConstants.LOGICAPADRAO.GERAL.AJAX_AUTOMATICO, false);
		requestMap.put(PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.EXIBIR);
	}

	/**
	 * Método para tratamento específico no caso da geração de esquema.
	 */
	protected void handleSchemaButtons() {
		Map<String, Object> requestMap = contextUtil.getRequestMap();
		requestMap.put(PlcConstants.ACAO.EXIBE_BT_GRAVAR,PlcConstants.NAO_EXIBIR);
		requestMap.put(PlcConstants.ACAO.EXIBE_BT_GERAR_ESQUEMA,PlcConstants.EXIBIR);
	}
}
