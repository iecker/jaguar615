/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */

package com.powerlogic.jcompany.controller.util;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.domain.type.PlcYesNo;

/**
 * jCompany 6.0
 */
@SPlcUtil
@QPlcDefault
public class PlcBeanPopulateUtil {

	@Inject
	protected transient Logger log;

	@Inject
	@QPlcDefault
	private PlcConfigUtil configUtil;

	@Inject
	@QPlcDefault
	private PlcClassLookupUtil classeLookupUtil;
	
	@Inject
	@QPlcDefault
	private PlcMetamodelUtil metamodelUtil;
	
	//cria instancia do propertyUtilsBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
	/**
	 * 
	 * @param bean
	 *            bean de saida
	 */
	@SuppressWarnings("unchecked")
	public void transferMapToBean(Map<String, Object> mapa, Object bean) {
		transferMapToBean(mapa,bean, null);
	}

	/**
	 * 
	 * @param bean
	 *            bean de saida
	 */
	@SuppressWarnings("unchecked")
	public void transferMapToBean(Map<String, Object> mapa, Object bean, Map<String, Class> classesDet) {

		log.debug("########## Entrou no transfereBeans");

		
		Class voClass = null;
		PropertyDescriptor[] pd = null;
		

		try {

			// Para cada atributo do formulário de entrada verifica se
			// existe
			// um atributo correspondente no VO (value object persistência).
			// Se existir, verifica se o tipo do atributo corresponde a uma
			// classe de persistência. Caso afirmativo, cria esta classe
			// e atribui o valor do formulário de entrada.

			log.debug("############ TransfereBeans: ORIGEM form ==> DESTINO vo  ");

			// Destino = VO persistência
			voClass = bean.getClass();
			pd = Introspector.getBeanInfo(voClass).getPropertyDescriptors();

			Set<String> mapaKeys = mapa.keySet();
			if (!mapaKeys.isEmpty()) {
				for (String nome : mapaKeys) {
					// identifica que campo fornece indice de listagem e não id
					// do
					// registro.
					if (nome.endsWith("_idx"))
						continue;
					Object valor = mapa.get(nome);

					if (valor != null && valor.getClass().isArray() && Array.getLength(valor) > 0) {
						valor = Array.get(valor, 0);
					}
					nome = nome.replace("_", ".");
					if (!nome.contains(".")) {

						for (int k = 0; k < pd.length; k++) {
							if (nome.equals(pd[k].getName())) {
								if (valor == null)
									continue;
								Object o = null;
								if (valor instanceof Map && valor instanceof JSONObject && !((JSONObject)valor).isNullObject()) {
									// TODO - Ao instanciar um objeto que é uma
									// entidade, tem que ver uma forma de
									// pegar a Entity. No momento ele esta
									// usando a classe abstrata e esta dando
									// erro.
									// Procura pelo valor no bean.
									o = propertyUtilsBean.getProperty(bean, nome);
									if (o == null) {
										if (((Map) valor).containsKey("id")) {
											transferOneValueToBean(pd[k], ((Map) valor).get("id"), bean, true, mapa);
											break;
										}
										o = pd[k].getPropertyType().newInstance();
										propertyUtilsBean.setProperty(bean, nome, o);
										// injeta o objeto no Bean.
									}
									// Transfere o mapa para o inner-bean.
									transferMapToBean((Map) valor, o);
								}
								else if(valor instanceof Collection){
									
									transferCollectionToBean(bean, classesDet, pd, nome, valor, k);
								}
								
								else {
									transferOneValueToBean(pd[k], valor, bean, mapa.containsKey((nome.replace(".", "_") + "_idx")),mapa);
								}
								break;
							}
						}
					} else {
						String primeiroNome = nome.substring(0, nome.indexOf("."));
						String restanteNome = nome.substring(nome.indexOf(".") + 1);

						PropertyDescriptor _pd = propertyUtilsBean.getPropertyDescriptor(bean, primeiroNome);
						if (_pd != null) {
							Object o = propertyUtilsBean.getProperty(bean, primeiroNome);
							if (o == null)
								o = _pd.getPropertyType().newInstance();
							Map<String, Object> _mapa = new HashMap<String, Object>();
							_mapa.put(restanteNome, valor);
							if (mapa.containsKey((nome.replace(".", "_") + "_idx")))
								_mapa.put(restanteNome + "_idx", mapa.get((nome.replace(".", "_") + "_idx")));
							transferMapToBean(_mapa, o);
							transferOneValueToBean(_pd, o, bean, false,mapa);
						}
					}
				}
			}
			if (log.isDebugEnabled())
				log.debug("NAO VAI RETORNAR CHAVE NATURAL");

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception ex) {
			throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_TRANSFER_BEANS, new Object[] { ex }, ex, log);
		}

	}

	protected void transferCollectionToBean(Object bean, Map<String, Class> classesDet, PropertyDescriptor[] pd, String nome, Object valor, int k) throws InstantiationException, IllegalAccessException, InvocationTargetException {
		Collection collection=null, entityCollection=null;
		if(valor instanceof List){
			collection = (List)valor;
			entityCollection=  new ArrayList();
		}
		else if(valor instanceof Set){
			collection = (Set)valor;
			entityCollection=  new LinkedHashSet();
		}
		else
			return;
		Iterator i = collection.iterator();
		while(i.hasNext()){
			Object obj = i.next();
			Object entity = classesDet.get(nome).newInstance();
			transferMapToBean((Map) obj, entity);
			transferMasterToDetail(bean, entity);
			entityCollection.add(entity);
		}
		transferOneValueToBean(pd[k],entityCollection,bean,true);
	}

	protected void transferOneValueToBean(PropertyDescriptor pd, Object valorInformado, Object bean, boolean indice, Map<String, Object> mapa) {
		
		try {

			Class<?> clazz = pd.getPropertyType();

			if (clazz.isPrimitive()) {
				if (int.class.equals(clazz)) {
					clazz = Integer.class;
				}
			}

			if (clazz.isAnnotationPresent(Entity.class) || clazz.isAnnotationPresent(MappedSuperclass.class)) {

				// testando se o valorInformado já é uma entidade
				if (valorInformado != null && metamodelUtil.isEntityClass(valorInformado.getClass())) {
					propertyUtilsBean.setProperty(bean, pd.getName(), valorInformado);
					return;
				}
				
				if (clazz.isAnnotationPresent(MappedSuperclass.class))
					clazz = Class.forName(pd.getPropertyType().getCanonicalName() + configUtil.getConfigApplication().suffixClass().entity());

				List<?> lista = classeLookupUtil.getListFromCache(clazz);

				Object o = clazz.newInstance();

				if (("id".equals(pd.getName()) && lista != null && !lista.isEmpty()) ||
						( lista != null && propertyUtilsBean.isReadable(valorInformado, "id")  && StringUtils.isNotEmpty( String.valueOf(propertyUtilsBean.getProperty(valorInformado, "id"))) )) {
					for (Object object : lista) {
						if ((valorInformado.toString() != null && valorInformado.toString().equals(propertyUtilsBean.getProperty(object, "id"))) ||
								(propertyUtilsBean.getProperty(valorInformado, "id") != null && propertyUtilsBean.getProperty(valorInformado, "id").equals(propertyUtilsBean.getProperty(object, "id"))) ) {
							propertyUtilsBean.setProperty(bean, pd.getName(), object);
							return;
						}
					}
				}
 
				if (indice && lista != null && lista.size() > Integer.valueOf(valorInformado.toString())) {// combo dinamico
					o = lista.get(Integer.valueOf(valorInformado.toString()));
				} else {
					if (StringUtils.isNotEmpty(ObjectUtils.toString(valorInformado))) {
						PlcPrimaryKey chavePrimaria = o.getClass().getAnnotation(PlcPrimaryKey.class);
						if (chavePrimaria != null){
							Object chave = chavePrimaria.classe().newInstance();
							String[] propriedades = chavePrimaria.propriedades();
							boolean firstTime=true;
							for(String propriedade : propriedades){
								PropertyDescriptor pdTemp = propertyUtilsBean.getPropertyDescriptor(chave, propriedade);
								if(firstTime){
									transferOneValueToBean(pdTemp, valorInformado, chave, indice,mapa);
									firstTime=false;
								}
								else{
									valorInformado=mapa.get(pd.getName()+propriedade);
									transferOneValueToBean(pdTemp, valorInformado, chave, indice,mapa);
								}
								
							}
							propertyUtilsBean.setProperty(o, "idNatural", chave);
						}
						else
							propertyUtilsBean.setProperty(o, "id", new Long(valorInformado.toString()));
					} else {
						o = null;
					}
				}
				
				propertyUtilsBean.setProperty(bean, pd.getName(), o);

			} else if (Enum.class.isAssignableFrom(clazz)) {

				// Nesta versão considera como String
				// Class classe = pd.getPropertyType();
				if (valorInformado == null || "".equals(valorInformado.toString().trim()))
					propertyUtilsBean.setProperty(bean, pd.getName(), null);
				else if (PlcYesNo.class.isAssignableFrom(clazz)) {
					String _valor = ((String) valorInformado).replace("true", "S").replace("false", "N").replace("t", "S").replace("f", "N");
					propertyUtilsBean.setProperty(bean, pd.getName(), Enum.valueOf((Class<? extends Enum>) clazz, _valor));
				} else {
					Object[] enumConstantes = clazz.getEnumConstants();
					Object _enum = null;
					for (Object constante : enumConstantes) {
						if (constante.toString().equals(valorInformado.toString()))
							_enum = constante;
					}
					if (_enum == null && StringUtils.isNumeric(valorInformado.toString())) {
						_enum = enumConstantes[Integer.parseInt(valorInformado.toString())];
					}
					propertyUtilsBean.setProperty(bean, pd.getName(), _enum);
				}

			} else if (Date.class.isAssignableFrom(clazz) || Calendar.class.isAssignableFrom(clazz)) {

				if (log.isDebugEnabled())
					log.debug(" transfereBeans: tipo igual a Date - valor = " + valorInformado);

				// Nao transfere data da ultima atualizacao mantida pelo
				// jCompany
				if (!pd.getName().equalsIgnoreCase("dataUltAlteracao")) {
					if (valorInformado != null && !valorInformado.equals("")) {
						String valorInformadoAux = valorInformado.toString();

						if (StringUtils.countMatches(valorInformadoAux, ":") == 2) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
							log.debug(" transfereBeans: ANTES  copyProperties para DATE COM HORA/MINUTO/SEGUNDO");
							BeanUtils.copyProperty(bean, pd.getName(), formatter.parse((String) valorInformado));
						} else if (StringUtils.countMatches(valorInformadoAux, ":") == 1) {
							SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
							log.debug(" transfereBeans: ANTES  copyProperties para DATE COM HORA/MINUTO");
							BeanUtils.copyProperty(bean, pd.getName(), formatter.parse((String) valorInformado));
						} else {
							if(!(valorInformadoAux!=null && valorInformadoAux.equals("null"))){
								SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
								log.debug(" transfereBeans: ANTES  copyProperties para DATE SEM HORA");
								//BeanUtils.copyProperty(bean, pd.getName(), formatter.parse((String) valorInformado));
								copyProperty(bean, pd.getName(), formatter.parse((String) valorInformado));
							}
						}
					} else {
						// Anula a data, desde que não seja controlada pelo
						// jCompany

						if (!pd.getName().equals("dataUltAlteracao") && !ArrayUtils.contains(PlcConstants.VO.ALM_AUDIT_DATE, pd.getName())) {

							java.util.Date dataNula = null;
							if (Date.class.isAssignableFrom(clazz)) {
								BeanUtils.copyProperty(bean, pd.getName(), dataNula);
							} else if (Calendar.class.isAssignableFrom(clazz)) {
								// TODO ver como "anular" o calendar
							}
						}

					}
				}

			} else if (Long.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz) ||  Double.class.isAssignableFrom(clazz)) {

				if (valorInformado == null || "".equals(valorInformado.toString().trim()) || !(NumberUtils.isNumber(valorInformado.toString()))) {

					propertyUtilsBean.setProperty(bean, pd.getName(), null);

				} else {
					BeanUtils.copyProperty(bean, pd.getName(), valorInformado);
				}

			}
			else if (BigDecimal.class.isAssignableFrom(clazz)){
				String valorInformadoStr=valorInformado.toString().replace(",", ".");
				BeanUtils.copyProperty(bean, pd.getName(), valorInformadoStr);
			}
			else if (!Integer.class.isAssignableFrom(clazz)) {
				if (!pd.getName().equals("usuarioUltAlteracao") && !pd.getName().equals("versao") && !pd.getName().equals("version")) {
					try {
						// para não deixar o método copyProperty levantar
						// uma exceção por causa de tentar setar valor "" em
						// uma lista
						if (((Set.class.isAssignableFrom(pd.getPropertyType()) || List.class.isAssignableFrom(pd.getPropertyType())) && valorInformado != null && valorInformado.toString().equals("")))
							return;

						if (log.isDebugEnabled())
							log.debug("########## Vai tentar copiar " + pd.getName() + " do tipo" + pd.getPropertyType().getName() + " com valor " + valorInformado + " para " + bean);

						// Somente copia se tem setter
						if (propertyUtilsBean.isWriteable(bean, pd.getName())) {
							if (Set.class.isAssignableFrom(propertyUtilsBean.getPropertyType(bean, pd.getName()))) {
								HashSet<Object> valorInformadoSet = new HashSet<Object>();
								if (valorInformado instanceof List<?>) {
									valorInformadoSet.addAll((List<?>) valorInformado);
								}
								BeanUtils.copyProperty(bean, pd.getName(), valorInformadoSet);
							} else if (Boolean.class.isAssignableFrom(propertyUtilsBean.getPropertyType(bean, pd.getName()))) {
								BeanUtils.copyProperty(bean, pd.getName(), valorInformado.equals("true") || valorInformado.equals("t") || valorInformado.equals("s") || valorInformado.equals("1") ? true : false);
							} else {
								BeanUtils.copyProperty(bean, pd.getName(), valorInformado);
							}
						} else if (log.isDebugEnabled()) {
							log.debug("Nao copiou " + pd.getName() + " porque nao achou setter");
						}
					} catch (Exception e) {
						log.debug("Falha controlada ao tentar copiar propriedade " + pd.getName());
					}
				} else {
					log.debug("transfereBeans: nao copiou campo reservado");
				}

			}
		} catch(PlcException plcE){
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("PlcBeanPopulateUtil", "transferOneValueToBean", e, log, "");
		}
	}
		
	/**
	 * Transfere um valor do Form para o VO
	 * 
	 * @param pd
	 *            Propriedade contendo o Valor
	 * @param valorInformado
	 *            Valor informado na propriedade
	 * @param bean
	 *            Vo de Destino
	 */
	@SuppressWarnings("unchecked")
	protected void transferOneValueToBean(PropertyDescriptor pd, Object valorInformado, Object bean, boolean indice) {
		transferOneValueToBean(pd, valorInformado, bean, indice,new HashMap<String, Object>());

	}
	
	protected void transferMasterToDetail(Object master, Object detail) throws IllegalAccessException, InvocationTargetException {

		PropertyDescriptor[] pds = propertyUtilsBean.getPropertyDescriptors(detail);
		
		for(PropertyDescriptor pd : pds ){
			
			if(pd.getPropertyType().isAssignableFrom(master.getClass())){
				BeanUtils.copyProperty(detail, pd.getName(), master);
			}
			
		}
	}
	
	/**
	 * Verifica se a propriedade terá um atributo transiente para trabalhar o seu formato.
	 * Caso tenha será chamado o set de seu atributo transient e o atributo transient
	 * deve setar o valor no campo principal
	 * 
	 * @param bean
	 * @param name
	 * @param value
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
	
		if (propertyUtilsBean.isWriteable(bean, name + PlcConstants.VO.SUFIXO_PROPERTY_TRANSIENT)) {
			name = name + PlcConstants.VO.SUFIXO_PROPERTY_TRANSIENT;
		}	
		
		try {
			BeanUtils.copyProperty(bean, name, value);
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			//throw new PlcException("PlcBeanPopulateUtil", "copyProperty", e, log, "");
		}
	}
}	

