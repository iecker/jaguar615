/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.util;


import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigNestedCombo;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcNaturalKeyUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;

/**
 * Classe utilitária para Entidades
 */

@SPlcUtil
@QPlcDefault
public class PlcNestedComboUtil implements Serializable {
	
	private static final long serialVersionUID = -6238291125072973115L;

	@Inject
	protected transient Logger log;
	
	@Inject @QPlcDefault 
	private PlcMetamodelUtil metamodelUtil;
	
	@Inject @QPlcDefault 
	private PlcURLUtil urlUtil;	

	@Inject @QPlcDefault 
	private PlcConfigUtil configUtil;	
	
	@Inject @QPlcDefault 
	protected PlcAnnotationUtil annotationUtil;
	
	/**
	 * Instância da classe baseEntityUtil.
	 */
	@Inject @QPlcDefault
	protected PlcCreateContextUtil contextMontaUtil;
	
	/**
	 * Utilitário para ler e gravar atributos no request e na sessão
	 */
	@Inject @QPlcDefault
	protected PlcContextUtil contextUtil;	
	
	@Inject @QPlcDefault 
	protected PlcDateUtil dateUtil;
	
	@Inject @QPlcDefault 
	protected PlcNaturalKeyUtil chaveNaturalUtil;
	
	
	public PlcNestedComboUtil() {
		
	}

	/**
	 * Recupera lista para cada combo aninhado configurado na anotação do MB. Normalmente chamado na edição.
	 * 
	 * É importante resaltar que é o próprio combo dinamico que busca a sua lista no escopo de conversação
	 */	
	public void retrieveNestedCombo(Object entidadeCorrente,int indexColecao, boolean vemDeDetalhe)  {
		
		try {
			//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized no loop abaixo
			PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();

			PlcConfigCollaborationPOJO _configAcaoControle = configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
			for (PlcConfigNestedCombo combo : _configAcaoControle.nestedCombo()) {
				if (StringUtils.isNotBlank(combo.origemProp()) && StringUtils.isNotBlank(combo.destinyProp())) {

					Object[] parametros 	= null;
					Method metodo			= null;
					Object entidadeOrigem		= null;

					// Se o combo não é de um detalhe, ou seja é do mestre ou uma crud 
					if (StringUtils.isEmpty(combo.detailName()) || vemDeDetalhe){
						PropertyDescriptor pd 	= propertyUtilsBean.getPropertyDescriptor(entidadeCorrente, combo.origemProp());
						metodo 					= pd.getReadMethod();
						parametros 				= metodo.getParameterTypes();

						// Se a propriedade é componente
						if (combo.origemProp().indexOf('.') > 0) {

							String componente 	= StringUtils.substringBefore(combo.origemProp(), ".");
							Method m 			= propertyUtilsBean.getPropertyDescriptor(entidadeCorrente, componente).getReadMethod();
							Object entycComp = m.invoke(entidadeCorrente,(Object[]) m.getParameterTypes());
							if (metodo != null) {
								entidadeOrigem = metodo.invoke(entycComp,(Object[]) parametros);
							}
						}
						else{
							if (metodo != null) {
								entidadeOrigem = metodo.invoke(entidadeCorrente,(Object[]) parametros);
							}
						}

						// tenta novamente buscando da relacao do combo
						if (entidadeOrigem == null && combo.origemProp().indexOf('.') > 0) {

							String proOrigem 	= combo.origemProp().substring(combo.origemProp().indexOf('.') + 1);
							String propDestino 	= combo.destinyProp();

							if (propDestino.indexOf('.') > 0)
								propDestino 	= propDestino.substring(propDestino.indexOf('.') + 1);

							if (propertyUtilsBean.getPropertyDescriptor(entidadeCorrente, propDestino) != null){
								Method m				 	= propertyUtilsBean.getPropertyDescriptor(entidadeCorrente, propDestino).getReadMethod();
								Object entidadeDestino 	= m.invoke(entidadeCorrente, (Object[]) parametros);

								// Se foi informado o destino
								if (entidadeDestino != null) {
									m 			= propertyUtilsBean.getPropertyDescriptor(entidadeDestino, proOrigem).getReadMethod();
									entidadeOrigem 	= m.invoke(entidadeDestino,(Object[]) parametros);
									propertyUtilsBean.setProperty(entidadeCorrente, proOrigem,entidadeOrigem);
								}
							}
						}

						if (entidadeOrigem != null) {

							// Recuperando a referência do request
							HttpServletRequest request = contextUtil.getRequest();
							String url = urlUtil.resolveCollaborationNameFromUrl(request);
							// montando um contexParam
							contextMontaUtil.createContextParam(null);
							PlcBaseContextVO context 	= getContext();
							
							// Recuperando a classe destino da propriedade informada
							Class classeDestino 				= annotationUtil.getClassManyToOne(entidadeCorrente.getClass(), combo.destinyProp());
							PlcEntityInstance voOrigemInstance 	= metamodelUtil.createEntityInstance(entidadeOrigem);
							Collection listaNavegacao 			= getServiceFacade(url).findNavigation(context,entidadeOrigem.getClass(), voOrigemInstance.getId(),classeDestino);

							PlcDomainLookupUtil dominioLookupUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDomainLookupUtil.class, new PlcNamedLiteral(PlcJsfConstantes.PLC_DOMINIOS));
							PlcConfigAggregationPOJO configGrupoAgregacao = configUtil.getConfigAggregation(url);
							String nomeDominio 	= combo.destinyProp();
							if ( StringUtils.isNotBlank(combo.detailName())){
								nomeDominio 	= combo.detailName()+ "[" + indexColecao +"]"  + nomeDominio;
							}
							else{
								if ( configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Tab) ||  configGrupoAgregacao.pattern().formPattern().equals(FormPattern.Ctb)){
									nomeDominio 	= "itensPlc[" + indexColecao +"]"  + nomeDominio;
								}
							}
							// montando o nome da lista "dominio" que ficará em conversação
							nomeDominio 		= dominioLookupUtil.createDomainNameToConversation(configGrupoAgregacao.entity(),nomeDominio );
							// Atualizando lista no scopo de conversação
							dominioLookupUtil.addDomainConversation(nomeDominio, ((List) listaNavegacao));

						}

					}
					else{
						PropertyDescriptor pd 	= propertyUtilsBean.getPropertyDescriptor(entidadeCorrente, combo.detailName());
						metodo 					= pd.getReadMethod();
						parametros 				= metodo.getParameterTypes();
						Collection<Object> detalhes  = (Collection<Object>)metodo.invoke(entidadeCorrente,(Object[]) metodo.getParameterTypes());
						int indexDetalhe = 0;
						for(Object detalhe : detalhes){
							retrieveNestedCombo(detalhe,indexDetalhe, true);
							indexDetalhe++;
						}
					}

				}
			}
		} catch (Exception e) {
			throw new PlcException("PlcEntityUtil", "retrieveNestedCombo", e, log, "");
		}
	}	

	/**
	 * @return Interface para acesso a camada de persistencia.
	 */
	protected IPlcFacade getServiceFacade(String colaboracao)  {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcIocControllerFacadeUtil.class, QPlcDefaultLiteral.INSTANCE).getFacade(colaboracao);
	}
	
	protected PlcBaseContextVO getContext() {
		return (PlcBaseContextVO)contextUtil.getRequest().getAttribute(PlcConstants.CONTEXT);
	}
}


