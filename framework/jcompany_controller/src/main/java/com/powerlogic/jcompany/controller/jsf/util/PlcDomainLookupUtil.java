/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigModule;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;

/**
 * Classe de serviço responsável pelas listas de dominios utilizados em Combos na tela.
 * É um componente em escopo de Evento (request), para que a cada requisição do usuário
 * busque a lista mais atualizada possível.
 * @author Bruno Grossi
 * @author Moisés Paula
 * @since jCompany 5.0
 */

@SPlcUtil
@QPlcDefault
public class PlcDomainLookupUtil {

	@Inject
	protected transient Logger log;
	
	
	/**
	 * Armazena as classes que não variam na aplicação (para otimização), ou sejam, que vêm de Enum.
	 */
	private static Map<String, List<SelectItem>> dominiosEstaticos = null;
	
	/**
	 * Mapa de domínios disponíveis.
	 */
	private Map<String, List> dominios = new ConcurrentHashMap<String, List>();
	
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;

	/**
	 * Utilitário para ler e gravar atributos no request e na sessão
	 */
	@Inject @QPlcDefault 
	protected PlcContextUtil contextUtil;
	
	/**
	 * Instancia da classe auxiliar.
	 */
	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @QPlcDefault 
	protected PlcClassLookupUtil classeLookupUtil;
	
	@Inject @QPlcDefault 
	protected PlcI18nUtil i18nUtil;
	
	@Inject @QPlcDefault 
	protected PlcReflectionUtil reflectionUtil;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	public PlcDomainLookupUtil() {
		
	}
	
    @PostConstruct
    public void init() {
    	transferFromCacheToMap();
    }
	
	/**
	 * Transfere os dados do cache da aplicação para o mapa de dominios.
	 * É executado na criação do componente.
	 *  
	 */    
	public void transferFromCacheToMap()  {
		transferClassesLookup();
		transferClassesDiscreteDomain();
	}
	
	@Produces @RequestScoped @Named(PlcConstants.PlcJsfConstantes.PLC_DOMINIOS)
	public PlcDomainLookupUtil createDomainLookup () {		
		return this;
	}
	
	/**
	 * Metodo que faz a transferencia das classes lookup do cache para o mapa local.
	 * 
	 */
	protected void transferClassesLookup()  {

		Class[] _classes = configUtil.getConfigApplication().application().classesLookup();
		if (_classes!=null && _classes.length>0) {
			for (Class classeLookup : _classes) {
				if (classeLookup!=null) {
					List<? extends Object> listaVO = classeLookupUtil.getListFromCache(classeLookup);
					addDomain(classeLookup.getSimpleName(), listaVO);
					addDomainJsf(classeLookup.getSimpleName(), listaVO);
				}
			}
		}
	}
	
	/**
	 * Método que faz a transferencia das classes de dominio discreto para o mapa de dominio.
	 * Faz um cache para otimização.
	 */
	protected void transferClassesDiscreteDomain()  {
		
		this.dominios.putAll(getMapStaticDomains());
		
	}
	
	/**
	 * Cria o mapa de dominos estáticos caso ainda não tenha sido criado.
	 * A implementação default utiliza um cache, para não criar várias vezes.
	 */
	protected Map<String, List<SelectItem>> getMapStaticDomains()  {
		if (dominiosEstaticos==null) {
			dominiosEstaticos = new HashMap<String, List<SelectItem>>();
			
			PlcConfigApplication configControleApp = configUtil.getConfigApplication().application();
			if (configControleApp!=null) {
				Class<? extends Enum> [] dominioDiscreto = configControleApp.classesDiscreteDomain();
			
				// Verificando se existe módulos e recuperando suas classes de Domínio Discreto
				List<Class<? extends Enum>> lista = new ArrayList<Class<? extends Enum>> ();
				for (Class<? extends Enum> en : dominioDiscreto) {
					lista.add(en);
				}
				
				PlcConfigApplication configControleModulo;
				String siglaModulo;
				PlcConfigModule[] modulos = configControleApp.modules();
				if (modulos != null){
					for(PlcConfigModule modulo : modulos){
						siglaModulo 			= "." + modulo.acronym();
						configControleModulo 	= configUtil.getConfigModule(siglaModulo).application();
						if (configControleModulo != null){
							Class<? extends Enum>[] classesDominioDiscreto = configControleModulo.classesDiscreteDomain();
							for (Class<? extends Enum> classDominioDiscreto : classesDominioDiscreto) {
								lista.add(classDominioDiscreto);
							}
						}
					}
				}
								
				if (lista != null && !lista.isEmpty()) {
					for (Class<? extends Enum> classeDiscreto : lista) {

						String nomeDominio = classeDiscreto.getSimpleName();
						
						if(dominiosEstaticos.containsKey(nomeDominio)) {
							dominiosEstaticos.remove(nomeDominio);
						}

						HttpServletRequest request = contextUtil.getRequest();

						List<SelectItem> uiList = new ArrayList<SelectItem>();
						Enum[] obj = classeDiscreto.getEnumConstants();
						if (obj!=null) {
							for (Enum e : obj) {
								
								SelectItem item = new SelectItem();

								String label = "";
								
								try {
									Method m = e.getClass().getMethod("getLabel");
									if(m != null) {
										label = (String) m.invoke(e);
									}
									if(label.startsWith("{") && label.endsWith("}")) {
										label= label.substring(1, label.length() - 1);										
									} 
									
								} catch (Exception exception) {
									// Não é um  erro, por ser uma implementação que mantem o legado funcionando
									if(label.equals("")) {
										String nome = contextUtil.capitalizeFirstLetter(e.getClass().getSimpleName());
										label = nome+'.'+e.name();
									}
								}
								
								item.setLabel(label);
								item.setValue(e);				
								uiList.add(item);
							}
						}

						dominiosEstaticos.put(nomeDominio, uiList);
					}
				}

			}
		}
		return dominiosEstaticos;
	}

	
	/**
	 * Adiciona um novo domínio ao mapa.
	 * @param nomeDominio
	 * @param listaEntidades
	 */
	public void addDomain(String nomeDominio, List<? extends Object> listaEntidades) {
		if(dominios.containsKey(nomeDominio)) {
			dominios.remove(nomeDominio);
		}
		
		List<Object> uiList = new ArrayList<Object>();
		
		if (listaEntidades!=null) {
			for (Object entidade : listaEntidades) {
				//SelectItem item = new SelectItem();
				//item.setValue(vo);

				if (propertyUtilsBean.isReadable(entidade, "indExcPlc")) {
					try{
						Object indExcPlcValor = propertyUtilsBean.getProperty(entidade, "indExcPlc");
						if (indExcPlcValor!=null 
								&& ("S".equalsIgnoreCase(indExcPlcValor.toString()) 
										|| "true".equalsIgnoreCase(indExcPlcValor.toString()))) {
							continue; //Não deve ser incluido na lista de tiver o indExcPlc marcado como true.
						}
					}catch (Exception e) {
						//Essa exceção nunca irá acontecer, já que está sendo testado com isReadable.
						log.info( "Exception tratada:",e);
					}
				}

				uiList.add(entidade);
			}
		}
		
		dominios.put(nomeDominio, uiList);
		
	}
	
	/**
	 * Armazena objetos de classes de lookup (simples, estereótipo "plcTabular") em caching, em formato JSF
	 * @param classeLookup a classe em si
	 * @param listaVOs a sua lista de objetos
	 */
	protected void addDomainJsf(String classeLookup, Collection listaVOs) {
		try {
			
			List<SelectItem> selectItems = new ArrayList();
			if (listaVOs != null) {
				for (Iterator iterator = listaVOs.iterator(); iterator.hasNext();) {
					Object entidade = iterator.next();
					// Armazena o Object-Id ou chave natural em valor - e 
					if (propertyUtilsBean.isReadable(entidade, "id"))
						selectItems.add(new SelectItem(propertyUtilsBean.getSimpleProperty(entidade, "id"),entidade.toString()));
					else if (propertyUtilsBean.isReadable(entidade, "idNatural"))
						selectItems.add(new SelectItem(propertyUtilsBean.getSimpleProperty(entidade, "idNatural"),entidade.toString()));
				}
			}	
		
			// Coloca no caching
			dominios.put(classeLookup+PlcConstants.LOOKUP.SUFIXO_JSF_LOOKUP , selectItems);
			
		} catch (Exception e) {
			throw new PlcException("PlcDomainLookupUtil", "addDomainJsf", e, log, "");
		}
		
	}

	/**
	 * Mapa de domínios.
	 * @return
	 */
	public Map<String, List> getDominios() {
		return dominios;
	}

	/**
	 * Adiciona uma lista de VO's no scopo de conversação.
	 * Estas listas são normalmente utilizadas em combos que sofrem alteração da sua lista. Ex: combo aninhados
	 * 
	 * @param nomeDominio, nome do Dominio que será adicionado.  [ nomeEntidade_propriedade ] 
	 * @param vos, lista de VO's para o dominio
	 */
	public void addDomainConversation(String nomeDominio, List<Object> listaEntidades ){
	    try {
	    	Map<String, List<Object>> mapa = PlcCDIUtil.getInstance().getInstanceByType(Map.class, new PlcNamedLiteral(PlcConstants.COMBOANINHADO.MAPA));
	    	mapa.put(nomeDominio, listaEntidades);

	    } catch(PlcException plcE){
			throw plcE;	    	
	    } catch (Exception e) {
	    	throw new PlcException("PlcDomainLookupUtil", "addDomainConversation", e, log, "");
	    }
	}
	
	/**
	 * Recupera uma lista de VO's no scopo de conversação, conforme dominio.
	 * Estas listas são normalmente utilizadas em combos que tem alteração da sua lista. Ex: combos aninhados
	 * 
	 * @param nomeDominio, nome do Dominio que será adicionado. 
	 */
	public List getDomainConversation(String nomeDominio){
		try {
			Map<String, List<Object>> mapa = PlcCDIUtil.getInstance().getInstanceByType(Map.class, new PlcNamedLiteral(PlcConstants.COMBOANINHADO.MAPA));
			if (mapa!=null)
				return 	mapa.get(nomeDominio);

			else
				return  new ArrayList();
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcDomainLookupUtil", "getDomainConversation", e, log, "");

		}
	}
	
	/**
	 * Monta um nome padrão para uma lista de VO's que será adicionada no scopo de conversação.
	 */
	public String createDomainNameToConversation(Class classeEntidade, String nomePropriedade){
		return classeEntidade.getSimpleName() + "Lookup" + nomePropriedade;
	}

	/**
	 * Recupera o Dominio para um Combo Aninhado.
	 */
	public List getNestedComboDomain(Class classVOPrincipal, String propriedadeComboAninhado){
		try {

			String dominio 	= createDomainNameToConversation(classVOPrincipal, propriedadeComboAninhado);
			Map mapaItensStatus = (Map) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.PLC_ITENS_STATUS);
			return getDomainConversation(dominio);

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcDomainLookupUtil", "getNestedComboDomain", e, log, "");
		}
	}
	
	/**
	 * Busca a lista referendo a um domínio.
	 * @param nomeDominio
	 * @return
	 */
	public List getDominio (String nomeDominio){
		return dominios.get(nomeDominio);
	}
	
	/**
	 * @since jCompany 5.0
	 * Recupera todas as classes lookup configuradas da aplicação para o cache.
	 * Apenas aponta para o {@link PlcClassLookupUtil}.
	 * 
	 */
	public void loadLookupClasses()  {
		classeLookupUtil.retrieveAllClassesLookupFromPersistenceToCache();
		transferClassesLookup();
	}
	
	
	/**
	 * jCompany 3.0 DP Context POJO. Informações gerais para envia à camada
	 * modelo. São montadas inicialmente no execute e disponibilizadas para
	 * complemento neste método
	 */
	protected PlcBaseContextVO getContext(HttpServletRequest request) {
		return (PlcBaseContextVO) request.getAttribute(PlcConstants.CONTEXT);
	}
	
	@Produces @ConversationScoped @Named(PlcConstants.COMBOANINHADO.MAPA)
	public Map<String, List<Object>> createNestedComboMap()  {
		
		return new HashMap<String, List<Object>>();
	}
}
