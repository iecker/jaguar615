/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
*/

package com.powerlogic.jcompany.controller.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;

/**
 * jCompany 5.0 Utilitário para manipulação de chaves naturais em arquitetura MVC,
 * independente da tecnologia da camada de controle. 
 * @since jCompany 5.0
 */
@SPlcUtil
@QPlcDefault
public class PlcNaturalKeyUtil{
	/**
	 * Serviço injetado e gerenciado pelo CDI
	 */
	@Inject @QPlcDefault 
	protected PlcServletRequestUtil servletRequestUtil;
	
	@Inject @QPlcDefault 
	protected PlcDateUtil dateUtil;

	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	public PlcNaturalKeyUtil() { 
		
	}

	@Inject
	protected transient Logger log;

	/**
	 * jCompany 5.0. Monta dinamicamente uma chave primária baseado nos valores da PK passados na URL.
	 * Monta baseado no {@link PlcPrimaryKey} informado, e volta o a chave mesmo que não exista nenhum parâmetro na requisição.
	 */
	public Object mountNaturalKeyByRequest(com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey chavePrimaria, HttpServletRequest request)  {
		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		if (chavePrimaria==null || metamodelUtil.isEntityClass(chavePrimaria.classe()) || chavePrimaria.propriedades().length==0) {
			return null;
		}
		
		PropertyDescriptor[] propertyDescriptors = propertyUtilsBean.getPropertyDescriptors(chavePrimaria.classe());
		Map<String, String> propriedadesChaveNatural = new HashMap<String, String>(chavePrimaria.propriedades().length);
		for (String propriedade : chavePrimaria.propriedades()) {
			Class<?> propertyType = getPropertyType(propertyDescriptors, propriedade);
			String tipo;
			if (propertyType!=null && metamodelUtil.isEntityClass(propertyType))
				tipo = propertyType.getName();
			else if (propertyType!=null && Date.class.equals(propertyType))
				tipo = "date";
			else
				tipo = null;

			propriedadesChaveNatural.put(propriedade, tipo);
		}

		return mountNaturalKeyByRequest(chavePrimaria.classe(), propriedadesChaveNatural, request);
		
	}
	
	/**
	 * Verifica se a entidade é com  Chave Natural
	 */
	public boolean hasNaturalKey(PlcPrimaryKey chavePrimaria) {
		
		return ( chavePrimaria != null && chavePrimaria.classe() != null) ;
	}
	
	public boolean checkEmptyPropsMap (Map<String, Object> propriedades) {

		Set<Entry<String, Object>> listaPropValores = propriedades.entrySet();

		boolean possuiId = true;

		if (listaPropValores == null || listaPropValores.isEmpty())
			possuiId = false;
		
		for (Entry<String, Object> propChave : listaPropValores) {
			Object value = propChave.getValue();
			if (value instanceof String && StringUtils.isBlank(value.toString())){
				possuiId = false;
			} else if (value == null){
				possuiId = false;
			}
		}

		return possuiId;

	}
	
	/**
	 * Verifica se a chave natural não está preenchida
	 * 
	 * @param entidade
	 * @return
	 */
	public boolean checkNullId(Object entidade)  {

		try {
			
			PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

			PlcEntityInstance entidadeInstance = metamodelUtil.createEntityInstance(entidade);
			
			boolean possuiId = StringUtils.isNotBlank(entidadeInstance.getIdAux());

			if (!possuiId && entidadeInstance.getIdNaturalDinamico() != null){

				PlcPrimaryKey chavePrimaria = entidade.getClass().getAnnotation(PlcPrimaryKey.class);
				Object idNatural = entidadeInstance.getIdNaturalDinamico();

				String[] propriedades = chavePrimaria.propriedades();

				possuiId = true;

				for (String propChave : propriedades) {

					Object valor = propertyUtilsBean.getProperty(idNatural, propChave);

					if (valor instanceof String && StringUtils.isBlank(valor.toString())){
						possuiId = false;
					} else if (valor == null){
						possuiId = false;
					}
				}
			}
			
			return possuiId;
			
		} catch (Exception e) {		
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * Verifica se a Entidade tem chave Natural, se tiver clona 'inicializa' o Id Natural.
	 */
	public Object cloneNaturalKey(Object entity) {
		
		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);
		
		PlcPrimaryKey chavePrimaria = entity.getClass().getAnnotation(PlcPrimaryKey.class);
		if (hasNaturalKey(chavePrimaria)){
			PlcEntityInstance entityInstance = metamodelUtil.createEntityInstance(entity);
			try {				
				entityInstance.setIdNatural(chavePrimaria.classe().newInstance());
			} catch (Exception e) {
				entityInstance.setIdNatural(null);
			}
		}
		return entity;
	}
	
	/**
	 * Verifica se tem alguma das propriedades da chave como parâmetro de request.
	 */
	public boolean hasKeyProperty(com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey chavePrimaria, HttpServletRequest request) {
		boolean temPropriedade=false;
		for (String propriedade : chavePrimaria.propriedades()) {
			if (StringUtils.isNotBlank(request.getParameter(propriedade))) {
				temPropriedade=true;
				break;
			}
		}
		return temPropriedade;
	}
	
	/**
	 * Busca o tipo de propriedade, baseado só na classe.
	 */
	private Class<?> getPropertyType(PropertyDescriptor[] propertyDescriptors, String prop) {
		for(PropertyDescriptor propDesc : propertyDescriptors) {
			if (prop.equals(propDesc.getName())) {
				return propDesc.getPropertyType();
			}
		}
		return null;
	}

	/**
	 * jCompany 5.0. Monta dinamicamente uma chave primária baseado nos valores da PK passados na URL.
	 */
	public Object mountNaturalKeyByRequest(Class<?> classeChavePrimaria,
			Map<String, String> propriedadesChaveNatural,
			HttpServletRequest request)  {

		log.debug( "########## Entrou em montaChaveNatural");
		
		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		try {

			//chave composta a montar
			Object pk = classeChavePrimaria.newInstance();

			// Varre cada propriedade da primary key, transformando de String no tipo apropriado
			for(String propriedade : propriedadesChaveNatural.keySet()) {
				String valorParametro = servletRequestUtil.getParameter(request, propriedade);
				String tipo = propriedadesChaveNatural.get(propriedade);

				if (log.isDebugEnabled())
					log.debug( "vai montar chave para prop "+propriedade+" com tipo "+tipo +" e valor "+valorParametro);

				if (tipo!=null && tipo.indexOf(".")>-1) {
					// É uma classe e assume OID se for participantes de chave composta. Se não for deverá montar na mão
					Object valor = null;
					try {
						valor = Class.forName(tipo).newInstance();
					} catch (Exception e) {
						// Se não conseguiu instaciar a classe, procura a classe entidade
						Field field = classeChavePrimaria.getDeclaredField(propriedade);
						ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
						Class classeEntity = manyToOne.targetEntity();
						valor = classeEntity.newInstance();
						
					}
					try {
						if (valorParametro!= null ) {
							
							PlcEntityInstance valorInstance = metamodelUtil.createEntityInstance(valor);
							
							PlcPrimaryKey chavePrimaria = valor.getClass().getAnnotation(PlcPrimaryKey.class);
							if (chavePrimaria != null){
								String propertyKey 	= chavePrimaria.propriedades()[0];
								Object keyVO  	= valorInstance.getIdNaturalDinamico();
								if ( keyVO == null)
									keyVO = chavePrimaria.classe().newInstance();
								
								BeanUtils.setProperty(keyVO,propertyKey,valorParametro);
								BeanUtils.setProperty(valor,"idNatural",keyVO);
							}
							else{
								if (NumberUtils.isNumber(valorParametro))
									valorInstance.setId(new Long(valorParametro));
							}	

							BeanUtils.setProperty(pk,propriedade,valor);
						}
					} catch (Exception e) {
						if (log.isDebugEnabled()) {
							log.debug( "Erro ao tentar assumir que classe da PK: "+tipo+" tem chave OID." +
									" Nao tem consequencias porque neste caso deve ser montado em metodo especifico. " +
									"Erro: "+e);
							e.printStackTrace();
						}
					}

				} else if (tipo!=null && tipo.equalsIgnoreCase("date")) {

					// Se o valor for um timestamp
					if (valorParametro != null){
						Date valor = null;
						if (NumberUtils.isNumber(valorParametro))
							valor = new Date(Long.parseLong(valorParametro));
						else
							valor = dateUtil.stringToDate(valorParametro);
						propertyUtilsBean.setProperty(pk,propriedade,valor);

					} else {

						SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
						try {
							if (valorParametro!= null) {
								Date valor = sf.parse(valorParametro);
								propertyUtilsBean.setProperty(pk,propriedade,valor);
							}
						} catch (Exception e) {
							if (log.isDebugEnabled()) {
								log.debug( "Erro ao tentar assumir que data da PK: "+propriedade+" tem formato dd/MM/yyyy." +
										" Nao tem consequencias porque neste caso deve ser montado em metodo especifico. Erro: "+e);
								e.printStackTrace();
							}
						}
					}

				} else {
					// Para demais valores, usa o copyProperties.
					if (valorParametro != null) {
						BeanUtils.copyProperty(pk, propriedade,valorParametro);
					}
				}
			}
			return pk;
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcNaturalKeyUtil", "mountNaturalKeyByRequest", e, log, "");
		}

	}

}
