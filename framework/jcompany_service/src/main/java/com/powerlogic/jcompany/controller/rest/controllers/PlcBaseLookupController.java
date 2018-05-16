/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.rest.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcEntityId;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.util.PlcClassLookupUtil;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcI18nUtil;

/**
 * Controle que recupera classes de DD (Domínio Discreto) ou Lookup. url:
 * /service/lookup(<dominio_lookup>, ...)
 */
@SPlcController
@QPlcControllerName("lookup")
public class PlcBaseLookupController<E, I> extends PlcBaseController<E, I> {
	// <Map<String, List<?>>, String>
	
	@Inject
	private HttpServletRequest request;

	/**
	 * Util utilizado para obter as classes configuradas como DD e Lookup.
	 */
	@Inject
	@QPlcDefault
	private PlcConfigUtil configUtil;

	/**
	 * Util utilizado no carregamento da classe de lookup do cache.
	 */
	@Inject
	@QPlcDefault
	private PlcClassLookupUtil classeLookupUtil;

	@Inject
	@QPlcDefault
	private PlcI18nUtil i18nUtil;


	@Inject
	@QPlcEntityId
	private String lookups;

	public String[] getLookupNames() {
		return lookups != null ? lookups.split("\\s*,\\s*") : null;
	}

	/**
	 * Procura por um ou mais classes lookups.
	 * 
	 * Preenche um mapa com a lista de cada lookup, e
	 * {@linkplain #setEntity(Map) injeta} como entidade do controle.
	 * 
	 * @param lookups
	 *            Classes lookups separadas por virgula
	 *            "lookup(lookup1, lookup2, ...)".
	 * 
	 *            {@inheritDoc}
	 */
	@Override
	public void retrieve(I id) {
		retrieveLookups(getLookupNames());
	}

	@Override
	public void update() {

		String[] nomes = getLookupNames();

		if (nomes != null) {
			Class<?>[] lookups = configUtil.getConfigApplication().application().classesLookup();
			String[] orderBys = configUtil.getConfigApplication().application().classesLookupOrderBy();
			for (int i = 0; i < lookups.length; i++) {
				if (orderBys!=null && orderBys.length == lookups.length)
					classeLookupUtil.retrieveOneClassLookupFromPersistenceToCache(lookups[i], orderBys[i]);
				else
					classeLookupUtil.retrieveOneClassLookupFromPersistenceToCache(lookups[i], "");
			}
		} else {
			classeLookupUtil.retrieveAllClassesLookupFromPersistenceToCache();
		}

		retrieve(null);
	}

	protected void retrieveLookups(String[] nomes) {
		// Cria um Objeto com todas as listas de lookup.
		Map<String, List<?>> mapa = new LinkedHashMap<String, List<?>>(nomes.length);
		for (String nome : nomes) {
			if (StringUtils.isNotEmpty(nome)) {
				mapa.put(nome, retrieveList(nome));
			}
		}
		setEntity((E)mapa);
	}

	/**
	 * Procura por uma lista do lookup específicado.
	 * 
	 * @param lookupOuDD
	 *            Nome do DD ou lookup.
	 * @return Lista de DD ou lookup
	 */
	protected List<?> retrieveList(String lookupOuDominioDiscreto) {
		// Procura por DD
		Class<?> tipo = findDiscretDomain(lookupOuDominioDiscreto);
		if (tipo != null) {
			return retrieveDiscretDomainList(tipo);
		} else {
			// Procura por Lookup.
			tipo = findLookup(lookupOuDominioDiscreto);
			if (tipo != null) {
				return retrieveLookupList(tipo);
			}
		}
		return null;
	}

	/**
	 * Recupera uma lista de objetos do cache, armazenada para o tipo
	 * especifico.
	 * 
	 * @param type
	 *            Tipo da lista armazenada.
	 * @return Lista do cache.
	 */
	protected List<?> retrieveLookupList(Class<?> type) {
		return classeLookupUtil.getListFromCache(type);
	}

	/**
	 * Obtem uma lista de objetos do DD específicado.
	 * 
	 * @param type
	 *            Classe do DD.
	 * @return Lista de Objetos do DD
	 */
	protected List<?> retrieveDiscretDomainList(Class<?> type) {
		Enum<?>[] enumConstants = (Enum<?>[]) type.getEnumConstants();
		// Lista de Objetos enum convertidos!
		List<Object> listaEnums = new ArrayList<Object>(enumConstants.length);
		// Monta o Prefixo da mensagem para o tipo.
		String msgPrefixo = StringUtils.uncapitalize(type.getSimpleName()).concat(".");
		// Para cara Enum, monta ID e Lookup.
		for (Enum<?> e : enumConstants) {
			Map<String, String> propriedadesEnum = new HashMap<String, String>(2);
			propriedadesEnum.put("id", e.name());
			propriedadesEnum.put("lookup", i18nUtil.getMessage(msgPrefixo.concat(e.name())));
			listaEnums.add(propriedadesEnum);
		}
		return listaEnums;
	}

	/**
	 * Procura pela classe de DD correspondente ao nome passado.
	 * 
	 * @param configUtil
	 * @param nome
	 * @return
	 * @see PlcConfigApplication#classesDiscreteDomain()
	 */
	protected Class<?> findDiscretDomain(String nome) {
		// Procura entre as classes configuradas como DD
		Class<?>[] classesDominioDiscreto = configUtil.getConfigApplication().application().classesDiscreteDomain();
		if (classesDominioDiscreto != null && classesDominioDiscreto.length > 0) {
			for (Class<?> classeDominioDiscreto : classesDominioDiscreto) {
				if (classeDominioDiscreto != null && nome.equals(classeDominioDiscreto.getSimpleName())) {
					return classeDominioDiscreto;
				}
			}
		}
		return null;
	}

	/**
	 * Procura por uma lista de objetos lookup.
	 * 
	 * @param configUtil
	 * @param nome
	 *            Nome da colecao lookup.
	 * @return Lista de objetos.
	 * @see PlcConfigApplication#classesLookup()
	 * @see PlcClassLookupUtil
	 */
	protected Class<?> findLookup(String nome) {
		// Monta o nome da classe com Sufixo da entidade.
		String nomeClasse = nome + configUtil.getConfigApplication().suffixClass().entity();
		// Obtem as classes configuradas na aplicacao como Lookup.
		Class<?>[] classesLookup = configUtil.getConfigApplication().application().classesLookup();
		// procura pela classe Lookup.
		if (classesLookup != null && classesLookup.length > 0) {
			for (Class<?> classeLookup : classesLookup) {
				if (classeLookup != null) {
					if (nome.equals(classeLookup.getSimpleName()) || nomeClasse.equals(classeLookup.getSimpleName())) {
						return classeLookup;
					}
				}
			}
		}
		return null;
	}
}
