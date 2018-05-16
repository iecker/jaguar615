/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */
package com.powerlogic.jcompany.controller.jsf.util;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.util.PlcBaseContextControllerUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

/**
 * Auxiliar para montagem do contexto segundo utilizando configurações por
 * anotações.
 */
@SPlcUtil
@QPlcDefault
public class PlcCreateContextUtil extends PlcBaseContextControllerUtil {

	@Inject
	@QPlcDefault
	protected PlcConfigUtil configUtil;

	@Inject
	@QPlcDefault
	protected PlcURLUtil urlUtil;

	public PlcCreateContextUtil() {

	}

	/**
	 * Monta um POJO com informações sobre a sessão, lógicas jcompany e outras
	 * informações de contexto, seguindo o Design Pattern Context Param.
	 * 
	 * @param plcMapping
	 *            Action-Mapping
	 * @param request
	 *            Request HTTTP
	 * @param f
	 *            Form-Bean
	 * @return POJO no padrão Context Param, contendo informações diversas para
	 *         camada Modelo.
	 */
	public PlcBaseContextVO createContextParam(PlcConversationControl controleConversacaoPlc) {

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcConfigAggregationPOJO config = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));

		PlcConfigCollaborationPOJO configAcaoControle = configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));

		PlcBaseContextVO context = PlcCDIUtil.getInstance().getInstanceByType(PlcBaseContextVO.class, QPlcDefaultLiteral.INSTANCE);

		HttpServletRequest request = contextUtil.getRequest();
		
		return createContextParam(controleConversacaoPlc, config, configAcaoControle, context, request);

	}

	public PlcBaseContextVO createContextParam(PlcConversationControl controleConversacaoPlc, PlcConfigAggregationPOJO config, PlcConfigCollaborationPOJO configAcaoControle, PlcBaseContextVO context, HttpServletRequest request) {
		
		if (config != null) {
			log.debug("########## Entrou em montaContextParam");

			try {

				PlcConfigAggregationPOJO _configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(request));
				createDbFactory(context, config.entity(), _configAcao, getModulesAcronym());
				createAggregation(request, context, config, configAcaoControle, controleConversacaoPlc);

				// SAVIO: Refactoring PlcBaseContextMontaUtil para pacote
				// comuns: Obtem parametros necessarios de request, sessao, etc.
				PlcBaseUserProfileVO voPerfilUsuario = null;
				Principal userPrincipal = request.getUserPrincipal();
				String ipUsuario = request.getRemoteAddr();
				HttpSession session = request.getSession();
				if (session.getAttribute(PlcConstants.PROFILE.USER_INFO_KEY) != null) {
					voPerfilUsuario = (PlcBaseUserProfileVO) session.getAttribute(PlcConstants.PROFILE.USER_INFO_KEY);
				}
				createProfileRequest(context, voPerfilUsuario, userPrincipal, ipUsuario);

				context.setUrl((String) request.getAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA));

				createMiscellany(context, config, request, null);

				// Coloca em escopo de request para controladores Tiles
				request.setAttribute(PlcConstants.CONTEXT, context);

			} catch(PlcException plcE){
				throw plcE;				
			} catch (Exception e) {
				throw new PlcException("PlcCreateContextUtil", "createContextParam", e, log, "");
			}
		}

		return context;
	}

	public void createMiscellany(PlcBaseContextVO context, PlcConfigAggregationPOJO configAcao, HttpServletRequest request, Object f) {

		context.setExecutionMode(request.getSession().getServletContext().getInitParameter(PlcConstants.CONTEXTPARAM.INI_MODO_EXECUCAO));

		if (configUtil.getConfigApplication().application().definition() != null)
			context.setAppAcronym(configUtil.getConfigApplication().application().definition().acronym());

	}

	private String createParameters(String[] lista) {
		StringBuffer tripa = new StringBuffer();
		int i = 0;
		while (i < lista.length) {
			tripa.append(lista[i]);
			if (++i < lista.length) {
				tripa.append(",");
			}
		}
		return tripa.toString();
	}

	@SuppressWarnings("unchecked")
	public void createAggregation(HttpServletRequest request, PlcBaseContextVO context, PlcConfigAggregationPOJO configAgregacao, PlcConfigCollaborationPOJO configAcaoControle, PlcConversationControl controleConversacaoPlc) {

		log.debug("############# Entrou em montaActionMapping");

		PlcContextUtil contextUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcContextUtil.class, QPlcDefaultLiteral.INSTANCE);

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		String formPattern = configAgregacao.pattern().formPattern().toString() != null ? configAgregacao.pattern().formPattern().toString() : configAgregacao.pattern().collaborationPatternPlx();
		context.setFormPattern(formPattern);
		PlcPrimaryKey chavePrimaria = (PlcPrimaryKey) configAgregacao.entity().getAnnotation(PlcPrimaryKey.class);
		if (chavePrimaria != null && !metamodelUtil.isEntityClass(chavePrimaria.classe())) {
			context.setPkClass(chavePrimaria.classe().getName());
		} else {
			context.setPkClass(null);
		}
		context.setEntityClassPlc(configAgregacao.entity());

		context.setApiQuerySel(configAcaoControle.selection() != null && StringUtils.isNotBlank(configAcaoControle.selection().apiQuerySel()) ? configAcaoControle.selection().apiQuerySel() : null);

		context.setArgPreference(configAgregacao.userpref().argument());

		PlcConfigDetail detalhes[] = configAgregacao.details();
		List<String> nomeProps = new ArrayList<String>();
		List<String> nomeClasses = new ArrayList<String>();

		Map<String, Class> detalhesPorDemanda = new HashMap<String, Class>();
		Map<String, Class<?>> detalhesExclusaoLogica = new HashMap<String, Class<?>>();
		Map<String, Map<String,Class<?>>> subDetalhesExclusaoLogica = new HashMap<String, Map<String,Class<?>>>();
		Map<String,Class<?>> mapaDetalheSubdetalhe = new HashMap<String,Class<?>>();
		boolean verificaDetalhesPorDemanda = !"N".equals(contextUtil.getRequest().getAttribute(PlcJsfConstantes.ACAO.PLC_IND_NAO_VERIFICAR_DETALHE_DEMANDA));

		for (PlcConfigDetail detalhe : detalhes) {
			if (detalhe != null && detalhe.clazz() != null && !detalhe.clazz().equals(Object.class) && metamodelUtil.isEntityClass(detalhe.clazz())) {
				nomeProps.add(detalhe.collectionName());
				nomeClasses.add(detalhe.clazz().getName());
				if (detalhe.onDemand() && verificaDetalhesPorDemanda)
					detalhesPorDemanda.put(detalhe.collectionName(), detalhe.clazz());

				if (detalhe.subDetail().clazz() != Object.class) {
					context.setSubDetailPropNameCollection(detalhe.subDetail().collectionName());
					context.setSubDetailClass(detalhe.subDetail().clazz().getName());
					context.setSubDetailParent(detalhe.clazz().getName());
					if (detalhe.subDetail().exclusionMode() == ExclusionMode.LOGICAL) {
						mapaDetalheSubdetalhe.put(detalhe.subDetail().collectionName(), detalhe.subDetail().clazz());
						subDetalhesExclusaoLogica.put(detalhe.collectionName(),mapaDetalheSubdetalhe);
					}
				}

			}

			if (detalhe.exclusionMode() == ExclusionMode.LOGICAL) {
				detalhesExclusaoLogica.put(detalhe.collectionName(), detalhe.clazz());
			}
		}

		context.setDetailNames(nomeClasses);
		context.setDetailNamesProps(nomeProps);
		// Se não estiver nulo é porque já foi registrado para a conversação
		if (verificaDetalhesPorDemanda && controleConversacaoPlc != null ) {
			try {
				if (controleConversacaoPlc.getDetalhesPorDemanda() != null)
					detalhesPorDemanda = controleConversacaoPlc.getDetalhesPorDemanda();
			} catch (Exception e) {
				// Erro de conversação não ativa
				log.warn("Conversação não está ativa", e.getCause());
			}	
			
		}

		context.setDetailOnDemand(detalhesPorDemanda);
		context.setLogicalExclusionDetails(detalhesExclusaoLogica);
		context.setLogicalExclusionSubDetails(subDetalhesExclusaoLogica);

	}

}
