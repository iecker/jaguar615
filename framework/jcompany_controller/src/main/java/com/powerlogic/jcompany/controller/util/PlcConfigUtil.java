/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;


import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.persistence.Embedded;
import javax.persistence.OneToMany;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigComponent;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationPOJO;
import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigCollaborationPOJO;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;

/**
 *  @since jCompany 3.2 Classe de acesso à configuração Global  (meta-dados) do jCompany, para todas as camadas MVC-P. 
 *  Os meta-dados são armazenados como anotações em pacotes, abaixo do pacote base 'com.powerlogic.jcompany.config'
 */
@SPlcUtil
@QPlcDefault
@Specializes
@ApplicationScoped
public class PlcConfigUtil extends com.powerlogic.jcompany.commons.util.PlcConfigUtil{

	@Inject
	protected transient Logger log;

	@Inject @QPlcDefault
	protected PlcIocControllerUtil	iocControleUtil;

	@Inject @QPlcDefault
	protected PlcReflectionUtil	reflectionUtil;

	private Map<String, PlcConfigAggregationPOJO> cacheConfigAgregacao = new ConcurrentHashMap<String, PlcConfigAggregationPOJO>();
	private Map<String, PlcConfigCollaborationPOJO> cacheConfigColaboracao = new ConcurrentHashMap<String, PlcConfigCollaborationPOJO>();


	/**
	 * Força a inicialização das configurações logo no início da carga
	 */
	public static void touch() {

	}

	/**
	 * @since jCompany 5 
	 *  se nao encontrou 
	 */
	public PlcConfigAggregationPOJO getConfigAggregation(String raizUrlColaboracao)  {

		try {
			PlcConfigAggregationPOJO pojo = null;
			if (raizUrlColaboracao!=null && !raizUrlColaboracao.equals("") && !raizUrlColaboracao.equals("/")) {
				if (raizUrlColaboracao.startsWith("/"))
					raizUrlColaboracao = raizUrlColaboracao.substring(1);

				pojo = cacheConfigAgregacao.get(raizUrlColaboracao);

				if (pojo==null) {
					String sufixo="";
					if(!StringUtils.isEmpty(raizUrlColaboracao) && raizUrlColaboracao.length()>2 )
						sufixo = raizUrlColaboracao.substring(raizUrlColaboracao.length()-3);
					int idx = ArrayUtils.indexOf(SUFIXOS_URL, sufixo);
					String formPattern =  (idx != -1 ? NOMES_SUFIXOS_URL[idx] : "Ctl");
					
					Class<?> action = iocControleUtil.resolveBaseMBClass(raizUrlColaboracao);
					
					PlcConfigForm configForm = action!=null?action.getAnnotation(PlcConfigForm.class):null;
					if (configForm != null && configForm.formPattern() != null && formPattern.equals("Ctl")) {
						formPattern = configForm.formPattern().sufixoUrl();
					}
					PlcConfigAggregation configAgregacao = action!=null?action.getAnnotation(PlcConfigAggregation.class):null;
					if (configAgregacao!=null) {
						pojo = new PlcConfigAggregationPOJO();
						pojo.setComponents(configAgregacao.components());
						pojo.setDescendents(configAgregacao.descendents());
						pojo.setDetails(configAgregacao.details());
						pojo.setEntity(configAgregacao.entity());
						if (configForm != null) {
							pojo.setPattern(formPattern, configForm);
						} else {	
							pojo.setPattern(formPattern);
						}	
						pojo.setUserpref(configAgregacao.userpref());
					} else {
						
						PlcConfigSuffixClass plcConfigSufixoClasse = getConfigApplication().suffixClass();
						PlcConfigPackage plcConfigPacote = getConfigApplication().packagee();
						
						if (raizUrlColaboracao.contains("/")) {
							// quando tiver modulo.
							String modulePreffix = StringUtils.substringBeforeLast(raizUrlColaboracao, "/");
							PlcConfigApplicationPOJO configModule = getConfigModule(modulePreffix);
							plcConfigSufixoClasse = configModule.suffixClass();
							plcConfigPacote = configModule.packagee();
						}
						
						String nome = "";
						if(!StringUtils.isEmpty(raizUrlColaboracao) && raizUrlColaboracao.length()>2 )
							nome = raizUrlColaboracao.substring(0,raizUrlColaboracao.length()-3);
						if (idx==-1)
							nome = raizUrlColaboracao;
						if (StringUtils.contains(nome, "/")){
							nome = StringUtils.substringAfterLast(nome,"/");
						}

						Class<?> entidade = null;
						try {
							entidade = Class.forName(plcConfigPacote.application()+plcConfigPacote.entity()+StringUtils.capitalize(nome)+plcConfigSufixoClasse.entity());
						}catch (Exception e) {
							try {
								entidade = Class.forName(plcConfigPacote.application()+plcConfigPacote.entity()+nome+"."+StringUtils.capitalize(nome)+plcConfigSufixoClasse.entity());
							}catch (Exception e2) {
								try {
									//entidade sem ancestral
									entidade = Class.forName(plcConfigPacote.application()+plcConfigPacote.entity()+nome+"."+StringUtils.capitalize(nome));
								}catch (Exception e3) {
								}
							}
						}	
						pojo = new PlcConfigAggregationPOJO(true);
						if (entidade!=null)
							pojo.setEntity(entidade);
						else if (formPattern.equals(FormPattern.Ctl.name()))
							pojo.setEntity(Object.class);
						else
							throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_ENTITY_NOT_FOUND);
						pojo.setPattern(formPattern);

						if (!StringUtils.equalsIgnoreCase(FormPattern.Tab.toString(), formPattern)) {
							pojo.setComponents(reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entidade, Embedded.class));
							Field detalhes[] = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(entidade, OneToMany.class);
							Field subdetalhes[] = new Field[detalhes.length];
							int cont = 0 ;
							for(Field detalhe : detalhes) {
								Field _subdetalhes[] = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(detalhe.getAnnotation(OneToMany.class).targetEntity(), OneToMany.class);
								if (_subdetalhes!=null && _subdetalhes.length>0) 
									subdetalhes[cont] = _subdetalhes[0];
								cont++;
							}
							pojo.setDetails(detalhes, subdetalhes);
						} else {
							pojo.setComponents(new PlcConfigComponent[0]);
							pojo.setDetails(new PlcConfigDetail[0]);
						}
						
					}
					
					
					cacheConfigAgregacao.put(raizUrlColaboracao, pojo);
				}
			}
			return pojo;
		} catch(PlcException plcE){
			throw plcE;				
		} catch (Exception e) { 
			throw new PlcException("PlcConfigUtil", "getConfigAggregation", e, log, "");
		}
	}


	public PlcConfigCollaborationPOJO getConfigCollaboration(String urlColaboracao)   {

		PlcConfigCollaborationPOJO pojo = null;
		if (StringUtils.isNotBlank(urlColaboracao) && !"/".equals(urlColaboracao)) {
			if (urlColaboracao.startsWith("/"))
				urlColaboracao = urlColaboracao.substring(1);

			pojo = cacheConfigColaboracao.get(urlColaboracao);

			if (pojo==null) {			
				Class<?> action = iocControleUtil.resolveBaseMBClass(urlColaboracao);
				PlcConfigForm configColaboracao = action!=null?action.getAnnotation(PlcConfigForm.class):null;
				if (configColaboracao!=null) {
					pojo = new PlcConfigCollaborationPOJO();
					pojo.setHelp(configColaboracao.help());
					pojo.setNestedCombo(configColaboracao.nestedCombo());
					pojo.setBehavior(configColaboracao.behavior());
					pojo.setGoogleMap(configColaboracao.googleMap());
					pojo.setPrint(configColaboracao.print());
					pojo.setFormLayout(configColaboracao.formLayout());
					pojo.setReport(configColaboracao.report());
					pojo.setRssSelection(configColaboracao.rssSelection());
					pojo.setTabular(configColaboracao.tabular());
					pojo.setProgrammaticValidation(configColaboracao.programmaticValidation());
					pojo.setSelection(configColaboracao.selection());

				} else {
					pojo = new PlcConfigCollaborationPOJO(true);
				}
				cacheConfigColaboracao.put(urlColaboracao, pojo);
			}
		}
		return pojo;
	}

}
