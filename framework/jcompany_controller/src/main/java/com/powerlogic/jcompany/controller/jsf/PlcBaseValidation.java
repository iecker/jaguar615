/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.jsf;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.OneToMany;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigSubDetail;
import com.powerlogic.jcompany.config.domain.PlcConfigDomainPOJO;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcDeclarativeValidationUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcEntityUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.controller.util.PlcValidationUtil;

/**
 * TODO Retirar após garantir que teste de duplicatas de tabular estão sendo feitos em outro local.
 */
@QPlcDefault
@ApplicationScoped
public class PlcBaseValidation implements Serializable{

	@Inject
	protected transient Logger log;

	private static final long serialVersionUID = 1L;
		
	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;
	
	@Inject @QPlcDefault 
	protected PlcEntityUtil entityUtil;
	
	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;	
	
	@Inject @QPlcDefault 
	protected PlcMsgUtil msgUtil;
	
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;	
	
	@Inject @QPlcDefault 
	protected PlcDeclarativeValidationUtil validacaoDeclarativaUtil;
	
	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;
	
	@Inject @QPlcDefault 
	protected PlcValidationUtil validacaoUtil;	
	
	/**
	 * Verifica se a cardinalidade dos detalhes está na faixa (max/min) definido.
	 *
	public boolean validMultiplicity(Object entityPlc)  {
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		
		PlcConfigAggregationPOJO configAcaoCorrente = configUtil.getConfigAggregation(url);

		for (PlcConfigDetail detalhe : configAcaoCorrente.details()) {
			// 
			// TODO Retirar - feito via BV
			//Verifica cardialidadde de detalhes
		//	if (!validacaoUtil.validadeMultiplicity(contextUtil.getRequest(), detalhe.multiplicity(),	findCountDetails(entityPlc, detalhe), detalhe.clazz().getName(),detalhe.collectionName(), detalhe.multiplicityMessage())) {
		//		return false;
		//	}
			// 
			// Verifica cardialidade de subdetalhe se tiver detalhes
			try{
				if ((findCountDetails(entityPlc, detalhe) > 0) && StringUtils.isNotBlank(detalhe.collectionName())){
					PlcConfigSubDetail subDetalhe = detalhe.subDetail();
					if (subDetalhe != null && !subDetalhe.clazz().equals(Object.class)){
						Collection <Object> colDetalhes = (Collection<Object>)PropertyUtils.getProperty(entityPlc, detalhe.collectionName());
						if (colDetalhes != null){
							for (Object umDetalhe : colDetalhes) {
								if (!validacaoUtil.validadeMultiplicity(contextUtil.getRequest(), subDetalhe.multiplicity(), findCountDetails(umDetalhe, subDetalhe), subDetalhe.clazz().getName(),subDetalhe.collectionName(), null)){
									return false;
								}
							}
						}

					}
				}
			}catch (Exception e) {
				throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] { "validacaoCardinalidade", e }, e, log);
			}
		}
		return true;
	}
	*/
	
	/**
	 * Método para validação de registros duplicados em detalhes e subdetalhes.
	 * @return
	 * 
	 *
	public boolean validDuplicated(Object entityPlc)  {
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		PlcConfigAggregationPOJO configAcaoCorrente = configUtil.getConfigAggregation(url);

		for (PlcConfigDetail detalhe : configAcaoCorrente.details()) {
			if (StringUtils.isNotBlank(detalhe.collectionName())){
				// Verifica duplicidade de detalhes
				Collection c = (Collection) reflectionUtil.getEntityPropertyPlc(entityPlc,detalhe.collectionName());//recuperaListaDetalhe(entityPlc, detalhe);
				
				if (StringUtils.isNotEmpty(detalhe.despiseProperty()) && detalhe.testDuplicity()){
					validateDuplicate(c, true, detalhe.despiseProperty());
				}else{
					
					PlcConfigDomainPOJO dominioDetalhe = configUtil.getConfigDomain(detalhe.clazz());
					if(dominioDetalhe!=null && dominioDetalhe.testDuplicity()){ 
						validateDuplicate(c, true, dominioDetalhe.despiseProperty());
					}
				}
				
				// Verifica duplicidade de subdetalhe
				PlcConfigSubDetail subDetalhe = detalhe.subDetail();
				if (StringUtils.isNotBlank(subDetalhe.collectionName())){
					if (subDetalhe != null){
						Collection <Object> colDetalhes;
						try{
							colDetalhes = (Collection<Object>)PropertyUtils.getProperty(entityPlc, detalhe.collectionName());
						}catch (Exception e) {
							throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] { "validacaoDuplicidade", e }, e, log);
						}

						if (StringUtils.isNotEmpty(subDetalhe.despiseProperty()) && subDetalhe.testDuplicity()){
							for (Object umDetalhe : colDetalhes) {
								Collection colecaoSubdetalhe = (Collection) reflectionUtil.getEntityPropertyPlc(umDetalhe,subDetalhe.collectionName());
								validateDuplicate(colecaoSubdetalhe, true, subDetalhe.despiseProperty());
							}
						}else{
							PlcConfigDomainPOJO dominioSubDetalhe = configUtil.getConfigDomain(subDetalhe.clazz());
							if (colDetalhes != null && dominioSubDetalhe!=null && dominioSubDetalhe.testDuplicity()){
								for (Object umDetalhe : colDetalhes) {
									Collection colecaoSubdetalhe = (Collection) reflectionUtil.getEntityPropertyPlc(umDetalhe,subDetalhe.collectionName());
									validateDuplicate(colecaoSubdetalhe, true, dominioSubDetalhe.despiseProperty());
								}
							}
						}

					}
				}
			}
		}
		return true;
	}
	*/

	/**
	 * Verifica o número de detalhe na entidade, para um tipo de detalhe.
	 * @param config configuração do detalhe ou subDetalhe.
	 * @return número de detalhes desse tipo na entidade corrente.
	 * 
	 *
	public Long findCountDetails(Object entidade, Annotation config)	 {
		
		try {
			
			Field[] fields = reflectionUtil.findAllFieldsHierarchically(entidade.getClass(), false, false);
 
			Class classe = null;
			String propDesprezar = null;
			
			if (config instanceof PlcConfigDetail){
				classe = ((PlcConfigDetail)config).clazz();
				PlcConfigDomainPOJO dominioDetalhe = configUtil.getConfigDomain(classe);
				propDesprezar = StringUtils.isNotEmpty(((PlcConfigDetail)config).despiseProperty()) ? ((PlcConfigDetail)config).despiseProperty() :  dominioDetalhe.despiseProperty();
			} else if (config instanceof PlcConfigSubDetail && !((PlcConfigSubDetail)config).clazz().equals(Object.class)){
				classe = ((PlcConfigSubDetail)config).clazz();
				PlcConfigDomainPOJO dominioDetalhe = configUtil.getConfigDomain(classe);
				propDesprezar = StringUtils.isNotEmpty(((PlcConfigSubDetail)config).despiseProperty()) ? ((PlcConfigSubDetail)config).despiseProperty() : dominioDetalhe.despiseProperty();
			}
			if (classe==null)
				return 0l;
			for (Field f : fields) {
				if (f.getAnnotation(OneToMany.class) != null && f.getAnnotation(OneToMany.class).targetEntity().isAssignableFrom(classe)) {
					Collection<Object> colecaoDetalhes;
					colecaoDetalhes = (Collection<Object>) PropertyUtils.getProperty(entidade, f.getName());
					if (colecaoDetalhes != null) {
						int cont = colecaoDetalhes.size();
						for (Object baseVO : colecaoDetalhes) {
							if (PropertyUtils.isReadable(baseVO, propDesprezar)){
								Object valorPropDesprezar = this.entityUtil.checkDespiseFieldValue(propDesprezar, baseVO);
								PlcEntityInstance baseVOInstance = metamodelUtil.createEntityInstance(baseVO);
								if (StringUtils.equals(baseVOInstance.getIndExcPlc(), "S") || (valorPropDesprezar==null 
										|| (valorPropDesprezar.toString() != null && StringUtils.isEmpty((String)valorPropDesprezar.toString()))))
									cont--;
							}
						}
						return new Long(cont).longValue();
					}

				}
			}
		} catch (Exception e) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_GENERIC, new Object[] {"recuperaQuantidadeDetalhes", e }, e, log);
		}
		return 0l;
	}
	*/
	
	/**
	 * Executa a Validação Programatica para as classes declaradas nas anotações
	 * do caso de uso, e disponibiliza as Mensagens de erro para os métodos que
	 * a validação falhar.
	 * 
	 * @param request,
	 *            Referência ao request
	 * @return true, Se todos as validações estão Ok, e false se falhou
	 *
	@SuppressWarnings( { "unchecked" })
	public boolean validProgrammaticValidation(Object objeto)  {

		PlcEntityList entityListPlc = null;
		Object entityPlc 			= null;

		if (objeto.getClass().isAssignableFrom(PlcEntityList.class)) {
			entityListPlc = (PlcEntityList) objeto;
		} else {
			entityPlc = objeto;
		}
		
		boolean validacaoOk = true;
		PlcConfigCollaborationPOJO configAcaoControle = configUtil.getConfigCollaboration(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));

		Set<ConstraintViolation<Object>> cv = null;
		
		if ( entityListPlc != null && entityListPlc.getItensPlc() != null){
			for (Object entity : entityListPlc.getItensPlc()){
				cv = validacaoDeclarativaUtil.declarativeValidationMergeMsgs(contextUtil.getRequest().getLocale(),"ApplicationResources", entity);
				if (cv != null && !cv.isEmpty() ) {
					// Disponibiliza as mensagens de erro.
					msgUtil.availableInvariantMessages(cv, null);
					validacaoOk = false;
				}		
			}
		} else{
			cv = validacaoDeclarativaUtil.declarativeValidationMergeMsgs(contextUtil.getRequest().getLocale(),"ApplicationResources", entityPlc);
			if (cv != null && !cv.isEmpty()) {
				// Disponibiliza as mensagens de erro.
				msgUtil.availableInvariantMessages(cv, null);
				validacaoOk = false;
			}		

		}
		

		// TODO: Implementar validaçãoProgramatica, não esquecer da validação no Service
		Set<ConstraintViolation<Object>> cvO = null;
		PlcConfigProgrammaticValidation validacoesProgramaticas[] = configAcaoControle.programmaticValidation();
		//cvO = validacaoDeclarativaUtil.declarativeValidationMergeMsgs(contextUtil.getRequest().getLocale(),"ApplicationResources", null);
		if (cvO != null && !cvO.isEmpty()) {
			// Disponibiliza as mensagens de erro.
			msgUtil.availableInvariantMessages(cvO, null);
			validacaoOk = false;
		}
 
		return validacaoOk;

	}
	*/
	
	
}
