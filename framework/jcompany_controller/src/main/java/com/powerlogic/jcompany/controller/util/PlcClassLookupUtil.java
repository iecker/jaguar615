/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.PlcAnnotationUtil;
import com.powerlogic.jcompany.commons.util.PlcConfigUtil;
import com.powerlogic.jcompany.commons.util.PlcEntityCommonsUtil;
import com.powerlogic.jcompany.commons.util.PlcStringUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;

/**
 * Serviço de manipulação de classes de Lookup, gerenciando-as no cache e capturando-as na persistência.
 * Classes de lookup são, por default, classes envolvidas em lógicas 'tabular', mas podem ser estendidas.<p>
 * @since jCompany 3.0
 */
@SPlcUtil
@QPlcDefault
public class PlcClassLookupUtil implements Serializable {

	@Inject
	protected transient Logger log;

	/**
	 * Serviços injetados e gerenciado pelo CDI
	 */
	
	@Inject @QPlcDefault 
	protected PlcConfigUtil configUtil;

	@Inject @QPlcDefault 
	protected PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleUtil;

	@Inject @QPlcDefault
	protected PlcEntityCommonsUtil entityCommonsUtil;

	@Inject @QPlcDefault 
	protected PlcAnnotationUtil annotationUtil;

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @QPlcDefault 
	protected PlcStringUtil stringUtil;

	@Inject @QPlcDefault 
	protected PlcCacheUtil cacheUtil;

	private Boolean initilized = false;
	
	/**
	 *  Retorna a Interface do Serviço de Persistência armazenado no escopo da aplicação
	 *  @since jCompany 5.0
	 */
	protected IPlcFacade getServiceFacade()  {
		return iocControleUtil.getFacade();
	}

	/**
	 * Recupera um objeto especifico de uma colecao em cache.
	 * @param nomeClasseComPackage Nome completo da classe em caching "com.empresa.xxx.vo.MinhaClasseVO"
	 * @param OID chave primária da instancia a ser recuperada
	 * @since jCompany 3.0
	 */
	public Object getObjectFromCache(Class clazz, Long OID) {

		String nomeSemPackage = clazz.getSimpleName();

		List l = (List) cacheUtil.getObject(PlcConstants.LOOKUP.PREFIXO_LOOKUP + nomeSemPackage);
		Iterator i = l.iterator();
		while (i.hasNext()) {
			Object vo = i.next();
			PlcEntityInstance voInstance = metamodelUtil.createEntityInstance(vo);
			if (voInstance.getId().intValue()==OID.intValue()) {
				return vo;
			}
		}

		return null;
	}
	
	/**
	 * Recebe um nome de classe com package e devolve lista que esta no cache.
	 * Atenção: A lista é uma referência e não deveria ser filtrada ou alterada para usuários específicos
	 * @param classeLookup Nome da classe com package
	 * @return Coleção de Objetos
	 * @since jCompany 3.0
	 */
	public List<? extends Object> getListFromCache(Class classeLookup)  {

		try {

			String nomeSemPackage = classeLookup.getSimpleName();

			if (log.isDebugEnabled()) {
				log.debug( "Recuperando classe do caching com chave = " + nomeSemPackage);
			}


			List<? extends Object> l = (List<? extends Object>) cacheUtil.getObject(PlcConstants.LOOKUP.PREFIXO_LOOKUP+nomeSemPackage);

			return l;

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcClassLookupUtil", "getListFromCache", e, log, "");
		}
	}

	/**
	 * Recupera todas as classes lookup configuradas da aplicação para o cache.
	 * @since jCompany 5.0
	 */
	public void retrieveAllClassesLookupFromPersistenceToCache()  {
		
		setInitilized(true);
		
		log.debug( "####### Entrou em retrieveAllClassesLookupFromPersistenceToCache para todas as classes");

		try {
			Class[] classes = configUtil.getConfigApplication().application().classesLookup();
			String[] orderBys = configUtil.getConfigApplication().application().classesLookupOrderBy();

			for (int i = 0; i < classes.length; i++) {
				if (orderBys!=null && orderBys.length == classes.length)
					this.retrieveOneClassLookupFromPersistenceToCache(classes[i], orderBys[i]);
				else
					this.retrieveOneClassLookupFromPersistenceToCache(classes[i], "");
			}
			setInitilized(true);
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcClassLookupUtil", "retrieveAllClassesLookupFromPersistenceToCache", e, log, "");
		}

	}

	/**
	 * Recupera uma classe de Lookup e coloca no caching.
	 * POJOs devem sempre ter package e não serem homônimos, então usa o padrão listaSel<nome-sem-package> para o cache
	 * @param context da aplicação com informações específicas
	 * @param classeLookup Classe para recuperação
	 * @param orderBy Cláusula OrderBy em OQL - tipicamente colocada no web.xml após "#". Ex: TipoCliente#nome asc, tipo asc
	 * @since jCompany 3.0
	 */
	public void retrieveOneClassLookupFromPersistenceToCache(PlcBaseContextVO context, Class classeLookup, String orderBy)  {
		
		log.debug( "########## Entrou em retrieveOneClassLookupFromPersistenceToCache");

		try {	
			
			if(context==null) {
				context = createContextParam();
			}
			
			context.setDbFactory(annotationUtil.getDbFactoryName(classeLookup));

			IPlcFacade plcFacade = getServiceFacade();
			
			Collection listaVOs  = plcFacade.findSimpleList(context,classeLookup,orderBy);

			storeClassLookup(classeLookup, listaVOs);

		} catch(PlcException plcE){
			throw plcE;						
		} catch (Exception e) {
			throw new PlcException("PlcClassLookupUtil", "retrieveOneClassLookupFromPersistenceToCache", e, log, "");
		}

	}
	
	/**
	 * Recupera uma classe de Lookup e coloca no caching.
	 * POJOs devem sempre ter package e não serem homônimos, então usa o padrão listaSel<nome-sem-package> para o cache
	 * @param classeLookup Classe para recuperação
	 * @param orderBy Cláusula OrderBy em OQL - tipicamente colocada no web.xml após "#". Ex: TipoCliente#nome asc, tipo asc
	 * @since jCompany 3.0
	 */
	public void retrieveOneClassLookupFromPersistenceToCache(Class classeLookup, String orderBy)  {
		retrieveOneClassLookupFromPersistenceToCache(null, classeLookup, orderBy);
	}
	
	/**
	 * Define uma lista de classes lookup no cache, a partir de uma lista já existente.
	 * Permite que se evite fazer uma nova pesquisa, caso já tenha a lista em memória atualizada.
	 * @param classeLookup Classe que será atualizada.
	 * @param listaVOs lista com as entidades. Se for null, adiciona uma lista vazia.
	 * @param checkLookupConfig
	 * @since jCompany 5.0
	 */
	public void storeClassLookup(Class classeLookup, List listaVOs, boolean checkLookupConfig) {
		
		if(checkLookupConfig&& isLookupClass(classeLookup)){
			storeClassLookup(classeLookup, listaVOs);
		}
		else if(!checkLookupConfig){
			storeClassLookup(classeLookup, listaVOs);
		}
	}

	/**
	 * Define uma lista de classes lookup no cache, a partir de uma lista já existente.
	 * Permite que se evite fazer uma nova pesquisa, caso já tenha a lista em memória atualizada.
	 * @param classeLookup Classe que será atualizada.
	 * @param listaVOs lista com as entidades. Se for null, adiciona uma lista vazia.
	 * @since jCompany 5.0
	 */
	public void storeClassLookup(Class classeLookup, Collection listaVOs) {
		
		// Garante que nunca é nula (primeira vez), para não cancelar
		// hashmap do cache
		if (listaVOs == null) {
			listaVOs = new ArrayList();
		}

		cacheUtil.putObject(PlcConstants.LOOKUP.PREFIXO_LOOKUP + classeLookup.getSimpleName(), listaVOs);

		if (listaVOs != null) {
			log.debug( "###### Carregou para lookup: "+ classeLookup + " com total de "+listaVOs.size()+" registros");
		}

	}

	/**
	 * Instancia um context mínimo para uso.
	 * @return context
	 */
	protected PlcBaseContextVO createContextParam()  {
		return contextMontaUtil.createContextParamMinimum();
	}

	/**
	 * Carrega classes declaradas no web.xml como classes de lookup em escopo de aplicação, no caching, para otimização de acesso e uso de memória.
	 * @param classeLookupListaGeral
	 */
	public void retrieveClassesLookupFromPersistenceToCache(Class[] classeLookupListaGeral)  {
		retrieveClassesLookupFromPersistenceToCache(classeLookupListaGeral, null);
	}
	
	/**
	 * Carrega classes declaradas no web.xml como classes de lookup em escopo de aplicação, no caching, para otimização de acesso e uso de memória.
	 * @param classeLookupListaGeral
	 * @param classesLookupOrderby
	 */
	public void retrieveClassesLookupFromPersistenceToCache(Class[] classeLookupListaGeral, String[] classesLookupOrderby)  {

		if (classeLookupListaGeral != null && classeLookupListaGeral.length>0) {
			try {
				if (log.isDebugEnabled())
					log.debug( "retrieveClassesLookupFromPersistenceToCache - Carregando classes de lookup:" );

				// Para cada classe, recupera e coloca no cache
				for(int i=0; i < classeLookupListaGeral.length; i++ ) {
					// Se lista especifica for nula recupera todos, senao recupera somente da lista especifica
					// Recupera e coloca no cache
					if (classesLookupOrderby!=null && classesLookupOrderby.length == classeLookupListaGeral.length) {
						retrieveOneClassLookupFromPersistenceToCache(classeLookupListaGeral[i],classesLookupOrderby[i]);
					} else {
						retrieveOneClassLookupFromPersistenceToCache(classeLookupListaGeral[i],"");
					}
				}
				setInitilized(true);
			} catch(PlcException plcE){
				throw plcE;					
			} catch (Exception e) {
				throw new PlcException("PlcClassLookupUtil", "retrieveClassesLookupFromPersistenceToCache", e, log, "");
			}

		} else {
			log.debug( "########## Nao existem classes de lookup declaradas");
		}
	}

	/**
	 * Verifica se a classe está configurada na aplicação como Classe Lookup.
	 * @param classeLookup
	 * @return Boolean informação se a classe é lookcup (True e False)
	 */
	protected boolean isLookupClass(Class<?> classeLookup) {
		for(Class<?> clazz: configUtil.getConfigApplication().application().classesLookup()){
			if(clazz.equals(classeLookup)){
				return true;
			}
		}
		return false;
	}

	public Boolean isInitilized() {
		return initilized;
	}

	public void setInitilized(Boolean initilized) {
		this.initilized = initilized;
	}

}
