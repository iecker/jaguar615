/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.

		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.controller.jsf.util;


import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.UIXComponent;

import com.powerlogic.jcompany.commons.IPlcFile;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcBeanMessages;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcConstants.PlcJsfConstantes;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcPrimaryKey;
import com.powerlogic.jcompany.commons.config.metamodel.PlcEntityInstance;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefaultLiteral;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcUtil;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;
import com.powerlogic.jcompany.commons.util.PlcDateUtil;
import com.powerlogic.jcompany.commons.util.PlcReflectionUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcCDIUtil;
import com.powerlogic.jcompany.commons.util.cdi.PlcNamedLiteral;
import com.powerlogic.jcompany.commons.util.metamodel.PlcMetamodelUtil;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregationPOJO;
import com.powerlogic.jcompany.config.aggregation.PlcConfigDetail;
import com.powerlogic.jcompany.config.aggregation.PlcConfigSubDetail;
import com.powerlogic.jcompany.config.collaboration.PlcConfigNestedCombo;
import com.powerlogic.jcompany.config.domain.PlcFileAttach;
import com.powerlogic.jcompany.controller.cache.PlcAggregatePropertyLocator;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.util.PlcConfigUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcNaturalKeyUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

/**
 * Classe utilitária para Entidades
 */

@SPlcUtil
@QPlcDefault
public class PlcEntityUtil implements Serializable {

	private static final long serialVersionUID = -6238291125072973115L;

	@Inject
	protected transient Logger log;

	@Inject @QPlcDefault 
	private PlcMetamodelUtil metamodelUtil;

	@Inject @QPlcDefault 
	private PlcReflectionUtil reflectionUtil;

	@Inject @QPlcDefault 
	private PlcURLUtil urlUtil;	

	@Inject @QPlcDefault 
	private PlcConfigUtil configUtil;	

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;

	//cria instancia do propertyUtilsBeanBean para evitar diversas chamadas a metodo syncronized
	private static PropertyUtilsBean propertyUtilsBean = BeanUtilsBean.getInstance().getPropertyUtils();
	
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


	public PlcEntityUtil() {

	}

	/**
	 * JCompany. Retorna a Interface com Implementação Padrão de Serviço da Camada Modelo
	 * @return Interface para acesso a camada de persistencia.
	 */
	protected IPlcFacade getServiceFacade(String colaboracao)  {
		return PlcCDIUtil.getInstance().getInstanceByType(PlcIocControllerFacadeUtil.class, QPlcDefaultLiteral.INSTANCE).getFacade(colaboracao);
	}

	/**
	 * jCompany 3.0 DP Context POJO. Informações gerais para envia à camada
	 * modelo. São montadas inicialmente no execute e disponibilizadas para
	 * complemento neste método
	 */
	public PlcBaseContextVO getContext(HttpServletRequest request) {
		return (PlcBaseContextVO) request.getAttribute(PlcConstants.CONTEXT);
	}


	/**
	 * Limpa os detalhes e subdetalhes da entidade, removendo aqueles que não preencheram a flag desprezar.
	 * @param configAcao configuração de dominio da colaboração atual.
	 * @param entidade Entidade que será limpa.
	 * 
	 */
	public void cleanDetails(PlcConfigAggregationPOJO configAcao, Object entidade)  {
		if (configAcao==null) {
			configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
		}
		PlcConfigDetail[] detalhes = configAcao.details();

		if (detalhes!=null && detalhes.length>0) {
			for (PlcConfigDetail configDetalhe : detalhes) {
				if (configDetalhe!=null) {
					PlcValMultiplicity valMultiplicity = reflectionUtil.getAnnotationFromProperty(PlcValMultiplicity.class, entidade.getClass(), configDetalhe.collectionName());
					String despiseProperty = (valMultiplicity != null && valMultiplicity.referenceProperty() != null ? valMultiplicity.referenceProperty() : configUtil.getConfigDomain(configDetalhe.clazz()).despiseProperty());
					cleanList(entidade, configDetalhe.clazz(), configDetalhe.collectionName(),despiseProperty, configDetalhe.subDetail());
				}
			}
		}
	}

	/**
	 * Limpa uma coleção específica a da entidade, verificando se a propriedade a desprezar foi preenchida ou não.
	 * Utilizado também para subdetalhe.
	 * 
	 * @param entidade value object atual.
	 * @param classe Classe do detalhe
	 * @param nomeColecao nome da propriedade do mestre que referencia a lista dos detalhes.
	 * @param propReferenciaDesprezar propriedade que deve ser desprezada.
	 * @param configSubDetalhe configuração do subdetalhe. Informado se tiver subdetalhe. Se já for o subdetalhe, não é informado.
	 * 
	 */
	private void cleanList(Object entidade, Class classe, String nomeColecao, String propReferenciaDesprezar, PlcConfigSubDetail configSubDetalhe)  {
		try {
			if (isDetailValid(classe, nomeColecao)) {

				Collection<Object> colecao = (Collection<Object>)propertyUtilsBean.getProperty(entidade, nomeColecao);
				if (colecao!=null && !colecao.isEmpty()) {
					if (!colecao.getClass().isAssignableFrom(TreeSet.class)){
						List<Object> listaRemover = new ArrayList<Object>();
						//Detalhe
						for (Object _entidade : colecao) {
							if (StringUtils.isNotEmpty(propReferenciaDesprezar)) {

								Object _valorPropDesprezar = checkDespiseFieldValue(propReferenciaDesprezar, _entidade);

								if ((_valorPropDesprezar==null 
										|| (_valorPropDesprezar.toString() != null && StringUtils.isEmpty((String)_valorPropDesprezar.toString())))
										&& (propertyUtilsBean.isReadable(_entidade, propReferenciaDesprezar))) {
									listaRemover.add(_entidade);
								}
							}

							//Subdetalhe
							if (configSubDetalhe!=null && !configSubDetalhe.clazz().equals(Object.class)) {
								PlcValMultiplicity valMultiplicity = reflectionUtil.getAnnotationFromProperty(PlcValMultiplicity.class, _entidade.getClass(), configSubDetalhe.collectionName());
								String despiseProperty = (valMultiplicity != null && valMultiplicity.referenceProperty() != null ? valMultiplicity.referenceProperty() : configSubDetalhe.despiseProperty());
								cleanList(_entidade, configSubDetalhe.clazz(), configSubDetalhe.collectionName(), despiseProperty, null);
							}

						}
						colecao.removeAll(listaRemover);
					} else {
						// Removendo detalhes utilizados como Set
						Collection <Object> collection = new ArrayList<Object> ();
						collection.addAll(colecao);

						colecao.clear();

						for (Object _entidade : collection){
							if (StringUtils.isNotEmpty(propReferenciaDesprezar)) {
								Object _valorPropDesprezar = propertyUtilsBean.getProperty(_entidade, propReferenciaDesprezar);
								if (_valorPropDesprezar!=null && 
										(_valorPropDesprezar.toString() != null && !StringUtils.isEmpty((String)_valorPropDesprezar.toString()))) {
									colecao.add(_entidade);
								}
							}
							// Subdetalhe
							if (configSubDetalhe!=null) {
								cleanList(_entidade, configSubDetalhe.clazz(), configSubDetalhe.collectionName(), configUtil.getConfigDomain(configSubDetalhe.clazz()).despiseProperty(), null);
							}
						}

						//propertyUtilsBean.setProperty(entidade, nomeColecao, colecao);
					}

				}
			}
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcEntityUtil", "cleanList", e, log, "");
		}
	}

	public Object checkDespiseFieldValue(String propReferenciaDesprezar, Object entidade) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Object valorPropDesprezar = null;

		if (propReferenciaDesprezar==null || !propertyUtilsBean.isReadable(entidade, propReferenciaDesprezar))
			return null;
		// Verifica se é propriedade do idNatural
		if (!propReferenciaDesprezar.startsWith("idNatural_")){
			valorPropDesprezar = propertyUtilsBean.getProperty(entidade, propReferenciaDesprezar);
		} else {
			Object idNatural = propertyUtilsBean.getProperty(entidade, "idNatural");
			String prop = propReferenciaDesprezar.replace("idNatural_", "");
			valorPropDesprezar = propertyUtilsBean.getProperty(idNatural, prop);
		}
		return valorPropDesprezar;
	}

	/**
	 * Valida se esse é uma configuração de detalhe ou subdetalhe válida.
	 * Verifica se a classe não é Object nem PlcEntityInstance, e o nomeColecaçao não é vazio ou "#".
	 * @param classe classe do detalhe
	 * @param nomeColecao nome da coleção do detale.
	 * @return true se a classe e nome da coleção forem válidos para um detalhe ou subdetalhe.
	 */
	public boolean isDetailValid(Class classe, String nomeColecao) {
		return classe!=null 
				&& !classe.equals(Object.class)
				&& StringUtils.isNotBlank(nomeColecao) && !nomeColecao.trim().equals("#");
	}

	/**
	 * @since jCompany 5.0
	 * Cria uma instância de uma classe Agregada para o VO e coloca o Id no objeto criado.  
	 * 
	 * @param vo, Value Object que contém a propriedade agregada.
	 * @param propAgregada, Nome da propriedade agregada do Value Object.
	 * @param valorInformado, Valor do "Id" para o objeto que será instanciado para a classe agregada.
	 * @param propsChaveNatural mapa de chaves naturais. Se não existir informe null ou um mapa vazio
	 */
	public void createAggregateToEntity(Object vo, String propriedade, String valorInformado,Map<String, Object> propsChaveNatural) {

		createAggregateToEntity(vo, propriedade, valorInformado, propsChaveNatural, true);
	}

	public Object createAggregateToEntity(Object vo, String propriedade, String valorInformado,Map<String, Object> propsChaveNatural, boolean invokeMethod) {

		Object v = vo;
		Object agregado = null;

		String propAgregada = propriedade;
		if (propriedade.contains(".")) {
			v = getAggregate(vo, propriedade.substring(0, propriedade.indexOf('.')));
			propAgregada = propriedade.substring(propriedade.indexOf('.')+1);
		}	
		if (v==null)
			v = vo;

		try {
			PropertyDescriptor[] pds =  (Introspector.getBeanInfo(v.getClass())).getPropertyDescriptors();
			for(PropertyDescriptor pd : pds){
				if (propAgregada.equals(pd.getName())){

					Method metodo  		= pd.getWriteMethod();
					Object[] objValor 	= new Object[1];

					if (StringUtils.isNotBlank(valorInformado) || ( propsChaveNatural!= null && ! propsChaveNatural.isEmpty())){
						/*
						 * Cria uma instância da classe agregada e coloca o Id "valorInformado" para o objeto
						 */
						PlcAggregatePropertyLocator agregadoPropLocator = PlcAggregatePropertyLocator.getInstance();
						agregado 	= agregadoPropLocator.getObjetoClasseAgregada(pd.getPropertyType());
						PlcEntityInstance agregadoInstance = metamodelUtil.createEntityInstance(agregado);

						if (propsChaveNatural == null || propsChaveNatural.isEmpty()){
							if (NumberUtils.isNumber(valorInformado))
								agregadoInstance.setIdAux(valorInformado);
						}
						else{
							Object key = agregadoInstance.getIdNaturalDinamico();
							Class<? extends Object> classeAgregada = agregado.getClass();
							PlcPrimaryKey chavePrimaria = classeAgregada.getAnnotation(PlcPrimaryKey.class);
							if (key == null){
								if (chavePrimaria != null){
									key = chavePrimaria.classe().newInstance();
								}
							}

							if ( key != null){

								for (String propriedadeCN : propsChaveNatural.keySet()) {

									Object value 		= propsChaveNatural.get(propriedadeCN);

									if (propertyUtilsBean.isWriteable(key, propriedadeCN)){

										Class typeOfValue 	= propertyUtilsBean.getPropertyType(key,propriedadeCN);
										if (StringUtils.isNotBlank(((String)value))){	
											if (typeOfValue != null && Date.class.isAssignableFrom(typeOfValue))
												propertyUtilsBean.setProperty(key, propriedadeCN, dateUtil.stringToDate((String)value));
											else
												if (typeOfValue != null && Long.class.isAssignableFrom(typeOfValue))
													propertyUtilsBean.setProperty(key, propriedadeCN, new Long((String)value));
												else
													if (typeOfValue != null && Integer.class.isAssignableFrom(typeOfValue))
														propertyUtilsBean.setProperty(key, propriedadeCN, new Integer((String)value));
													else
														propertyUtilsBean.setProperty(key, propriedadeCN, value);
										}

									} else if (propertyUtilsBean.isWriteable(agregado, propriedadeCN)){

										Class typeOfValue 	= propertyUtilsBean.getPropertyType(agregado,propriedadeCN);
										if (StringUtils.isNotBlank(((String)value))){	
											if (typeOfValue != null && Date.class.isAssignableFrom(typeOfValue))
												propertyUtilsBean.setProperty(agregado, propriedadeCN, dateUtil.stringToDate((String)value));
											else
												if (typeOfValue != null && Long.class.isAssignableFrom(typeOfValue))
													propertyUtilsBean.setProperty(agregado, propriedadeCN, new Long((String)value));
												else
													if (typeOfValue != null && Integer.class.isAssignableFrom(typeOfValue))
														propertyUtilsBean.setProperty(agregado, propriedadeCN, new Integer((String)value));
													else
														propertyUtilsBean.setProperty(agregado, propriedadeCN, value);

										}
									}

								}
								propertyUtilsBean.setProperty(agregado, "idNatural", key);
							}
						}


						objValor[0] 		= agregado;
					}
					else
						objValor[0] 		= null;

					// coloca o objeto da classe agregada no VO
					if (invokeMethod)
						metodo.invoke(v, objValor);

					break; // já encontrou a propriedade agregada. 

				}
			}

			return agregado;
		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcEntityUtil", "createAggregateToEntity", e, log, "");

		}
	}


	/**
	 * @since jCompany 5.0
	 * Recupera o objeto "PlcEntityInstance" para um agregado
	 *
	 * @param vo, Value Object que contém a propriedade agregada.
	 * @param propAgregada, Nome da propriedade agregada do Value Object.
	 * @return, Objeto referente à propriedade Agregada
	 */
	public Object getAggregate(Object vo, String propriedade) {
		if (vo == null)
			return null;
		Object v = vo;

		String propAgregada = propriedade;
		if (propriedade.contains(".")) {
			v = getAggregate(vo, propriedade.substring(0, propriedade.indexOf('.')));
			propAgregada = propriedade.substring(propriedade.indexOf('.')+1);
		}	
		if (v==null)
			v = vo;
		try {
			Object agregado = null;
			PropertyDescriptor[] pds =  (Introspector.getBeanInfo(v.getClass())).getPropertyDescriptors();
			for(PropertyDescriptor pd : pds){
				if (propAgregada.equals(pd.getName())){

					Method metodo = pd.getReadMethod();
					if (metodo != null){
						Object[] parametros = metodo.getParameterTypes();
						agregado = (Object)metodo.invoke(v, (Object[]) parametros);
						break;
					}
				}
			}	
			return agregado;	

		} catch(PlcException plcE){
			throw plcE;			
		} catch (Exception e) {
			throw new PlcException("PlcEntityUtil", "getAggregate", e, log, "");
		}
	}




	/**
	 * Verifica se a lista contém a entidade
	 */
	public boolean isInList (List listaDominio, Object entidade){

		PlcEntityInstance entidadeInstance = metamodelUtil.createEntityInstance(entidade);

		if (listaDominio != null && !listaDominio.isEmpty()){
			for (Object object : listaDominio) {
				Object voLista = object;
				PlcEntityInstance voListaInstance = metamodelUtil.createEntityInstance(voLista);
				if (voListaInstance.getIdAux().equals(entidadeInstance.getIdAux()))
					return true;
			}
		}

		return false;
	}

	/**
	 * Retorna o combo anterior do combo aninhado atual
	 */
	public String getPreviewsCombo(PlcConfigAggregationPOJO acao, PlcDomainLookupUtil dominioLookupUtil, String campo, PlcConfigNestedCombo[] comboAninhado) {

		for (PlcConfigNestedCombo _aninhado : comboAninhado) {
			if (_aninhado.destinyProp().equals(campo)){
				return _aninhado.origemProp();
			}
		}

		return " ";
	}

	/**
	 * Retira as opções de forma recursiva do combo aninhado
	 */
	public void removeRecursiveOptions(PlcConfigAggregationPOJO _configAcao,PlcDomainLookupUtil dominioLookupUtil, 
			String campo,PlcConfigNestedCombo[] comboAninhado, Map mapaItens)  {

		for (PlcConfigNestedCombo _aninhado : comboAninhado) {

			if (_aninhado.origemProp().equals(campo)){

				String propDestino = _aninhado.destinyProp();

				if (mapaItens != null){
					propDestino	= mapaItens.get("colecaoNome") + "[" + mapaItens.get("index")  +"]"  + propDestino;   
				}

				String nomeSubDominio = dominioLookupUtil.createDomainNameToConversation(_configAcao.entity(), propDestino);
				//Coloca a nova lista no escopo de conversacao	
				dominioLookupUtil.addDomainConversation(nomeSubDominio, new ArrayList<Object> ());
				removeRecursiveOptions(_configAcao, dominioLookupUtil, propDestino, comboAninhado, mapaItens);

			}

		}
	}



	public Object retrieveAggregateByProperties(Map<String, Object> propriedades, Object vinculado) {

		log.info("retrieveAggregateByProperties - Recupera o Agregado pelas Proriedades");

		boolean verificaMapVazio = chaveNaturalUtil.checkEmptyPropsMap(propriedades);

		Object entityLookup = null;

		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());

		PlcBaseContextVO context = getContext(contextUtil.getRequest());

		if(null == context){
			context = contextMontaUtil.createContextParam(plcControleConversacao);
		}

		if (verificaMapVazio) {
			context.setEntityClassPlc(vinculado.getClass());
			entityLookup = getServiceFacade(url).findAggregateLookup(context, vinculado, propriedades);
			return entityLookup;
		}

		return entityLookup;

	}


	public Object createAggregateByProperties(Map<String, Object> propriedades, Object agregado) {
		try {
			PlcEntityInstance agregadoInstance = metamodelUtil.createEntityInstance(agregado);
			if (agregadoInstance.getIdNaturalDinamico() != null) {
				BeanUtils.populate(agregadoInstance.getIdNaturalDinamico(), propriedades);
			}

			BeanUtils.populate(agregado, propriedades);

		} catch (Exception e) {
			log.warn( "Falha ao atribuir propriedades "+propriedades+" no bean "+agregado.getClass(), e);
		}
		return agregado;
	}

	/**
	 * Efetua a auto recuperação para o id ou Chaves Naturais Informados no componente vinculado.
	 * A Propriedade autoRecuperacaoClasse deve ser informada para que o valueChangeListener do componete seje configurado automaticamente pelo jCompany.
	 */
	@SuppressWarnings("unchecked")
	public void autofindAggregate(ValueChangeEvent valueChangeEvent)  {

		//	Se o valor não está null e não é igual ao valor que já foi recuperado 'oldValue' recupera
		if (valueChangeEvent.getNewValue()!=null && StringUtils.isNotBlank((String)valueChangeEvent.getNewValue())
				&& !valueChangeEvent.getNewValue().equals(valueChangeEvent.getOldValue())){

			Map<String, Object> propsChaveNatural 	= (Map<String, Object>)valueChangeEvent.getComponent().getAttributes().get(PlcJsfConstantes.PROPRIEDADES.PROPS_CHAVE_NATURAL_PLC);

			if (propsChaveNatural == null){
				log.info( "Entrou para Auto Recuperação de Vinculado");
				try {
					PropertyKey key = null; 
					// Registrando que foi autoRecuperaçao


					HttpServletRequest request 	= contextUtil.getRequest();
					String propLookupPesquisa 	= (String)valueChangeEvent.getComponent().getAttributes().get(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_KEY);

					// Se for chave Natural verifica se o NewValue não é igual à chave Natural que já tem no componente
					try{
						if (propsChaveNatural != null && !propsChaveNatural.keySet().isEmpty() && metamodelUtil.isEntityClass(valueChangeEvent.getOldValue().getClass())){
							Object vinculado 	=  valueChangeEvent.getOldValue();

							PlcEntityInstance vinculadoInstance = metamodelUtil.createEntityInstance(vinculado);


							Object keyVinculado 	=  vinculadoInstance.getIdNaturalDinamico();
							String chaveNatural 	= (String)propsChaveNatural.keySet().toArray()[0];
							Object value 			= propertyUtilsBean.getProperty(keyVinculado, chaveNatural);
							DateFormat dataFormat 	= DateFormat.getDateInstance(DateFormat.SHORT, contextUtil.getRequest().getLocale());

							if (value instanceof Date)
								value = dataFormat.format((Date)value);
							// Se for igual não tem a necessidade de recuperar novamente
							if (valueChangeEvent.getNewValue().equals(value.toString())){
								return;
							}
						}
					}catch(Exception e){ // Caso tenha alguma exceção segue com a autoRecuperação novamente. Não é motivo de ecuar a exeção. 
					}

					Map<String, Object> propsLookup			= null;
					if (propsChaveNatural == null)
						propsLookup	= new TreeMap<String, Object>();
					else
						propsLookup	= new TreeMap<String, Object>(propsChaveNatural);

					boolean isOID 	= propsLookup == null || propsLookup.keySet().isEmpty();

					if(isOID){

						// Se não foi informado uma propriedade específica, recupera pelo Id, caso não seja Chave Natural porque assim serão as chaves 
						if (StringUtils.isBlank(propLookupPesquisa)){
							if ( !NumberUtils.isNumber((String)valueChangeEvent.getNewValue()))
								throw new PlcException(PlcBeanMessages.JCOMPANY_ERROR_FIND_AGGREGATE_OID_NOT_NUMERIC, null, log);

							propsLookup.put("id", new Long((String)valueChangeEvent.getNewValue()));
						}
						else
							propsLookup.put(propLookupPesquisa, valueChangeEvent.getNewValue());

					}

					String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());

					// A propriedade 'autoRecuperacaoClasse' deve ser informada no componente vinculado
					String classeLookupCorrentePlc = (String)valueChangeEvent.getComponent().getAttributes().get(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_CLASSE_KEY);
					if (classeLookupCorrentePlc != null || StringUtils.isNotBlank(classeLookupCorrentePlc)){
						// Instancia a Entity de Lookup, para enviá-lo como referência para IoC da camada Modelo.
						Object entityLookup;
						entityLookup = Class.forName(classeLookupCorrentePlc).newInstance();
						entityLookup = getServiceFacade(url).findAggregateLookup(getContext(request), entityLookup, propsLookup);

						// Registrando o valor no componete
						key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.LOOKUP_VALUE_KEY);
						if (key != null){
							if (entityLookup == null ){
								((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key,null);
								if (StringUtils.isNotBlank(propLookupPesquisa)){
									key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_VALUE);
									((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key,null);
								}

							} else {
								((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key,entityLookup.toString());
								String clientId = ((UIXComponent)valueChangeEvent.getComponent()).getClientId(FacesContext.getCurrentInstance());
								request.setAttribute("lookup_"  + clientId, entityLookup.toString());
								// Registra também propLookupPesquisa
								if (StringUtils.isNotBlank(propLookupPesquisa)){
									key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_VALUE);
									Object propLookupPesquisaValue = propertyUtilsBean.getProperty(entityLookup, propLookupPesquisa);
									((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key,propLookupPesquisaValue);
									request.setAttribute(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_KEY + clientId , propLookupPesquisaValue);
								}
							}
						}

						// Tem que atualizar o id, pois foi recuperado por uma propriedade que não é o OID

						if (StringUtils.isNotBlank(propLookupPesquisa) && entityLookup != null){
							PlcEntityInstance entityLookupInstance = metamodelUtil.createEntityInstance(entityLookup);

							key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.VALUE_KEY);						
							((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key,entityLookupInstance.getIdAux());
							key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_VALUE);
							((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key,propertyUtilsBean.getProperty(entityLookup, propLookupPesquisa));
						}
						contextUtil.getRequest().setAttribute(PlcJsfConstantes.ACAO.PLC_OBJ_AUTO_RECUPERACAO,entityLookup);

					}
				} catch(PlcException plcE){
					throw plcE;						
				} catch (Exception e) {
					throw new PlcException("PlcEntityUtil", "autofindAggregate", e, log, "");
				}
			}
		} else {
			PropertyKey key = null;
			key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.VALUE_KEY);
			((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key, "");
			key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.LOOKUP_VALUE_KEY);
			((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key, "");
			key = ((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().getType().findKey(PlcJsfConstantes.PROPRIEDADES.AUTO_RECUPERACAO_PROPRIEDADE_VALUE);
			((UIXComponent)valueChangeEvent.getComponent()).getFacesBean().setProperty(key, "");
		}

	}

	/**
	 * Método que percorre todos os detalhes verificando se existe algum marcado para excluir.
	 * @param configAcao configuração de dominio da colaboração atual.
	 * @param entidade.
	 * @return retorna true se existir algum detalhe para ser excluido.
	 *  
	 *  
	 */
	public boolean checkDetailToDelete(PlcConfigAggregationPOJO configAcao, Object entidade) {
		if (configAcao==null) {
			configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
		}
		int i=0;
		PlcConfigDetail[] detalhes = configAcao.details();
		if (detalhes!=null && detalhes.length>0) {
			for (PlcConfigDetail configDetalhe : detalhes) {
				if (configDetalhe!=null) {
					if (checkListToDelete(entidade, configDetalhe.clazz(), configDetalhe.collectionName(), configUtil.getConfigDomain(configDetalhe.clazz()).despiseProperty(), configDetalhe.subDetail()))
						return true;	
				}
			}
		}
		return false;
	}

	public boolean checkFileToDelete(PlcConfigAggregationPOJO configAcao, Object entidade) {

		try {

			Boolean retorno = false;

			if (configAcao == null){
				configAcao = configUtil.getConfigAggregation(urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest()));
			}

			Field [] camposAnotados = reflectionUtil.findAllFieldsHierarchicallyWithAnnotation(configAcao.entity(), PlcFileAttach.class);

			for (Field field : camposAnotados) {
				PlcFileAttach fileAttach = field.getAnnotation(PlcFileAttach.class);

				String nomePropriedade = field.getName();

				if (fileAttach.multiple()){

					List arquivos = (List)propertyUtilsBean.getProperty(entidade, nomePropriedade );
					if (arquivos != null){
						List arquivosNovos = new ArrayList();
						arquivosNovos.addAll(arquivos);

						for (Object object : arquivos) {
							Object arquivo = object;

							PlcEntityInstance arquivoInstance = metamodelUtil.createEntityInstance(arquivo);

							if ("S".equals(arquivoInstance.getIndExcPlc()) && arquivoInstance.getId() == null) {
								arquivosNovos.remove(object);
							} else if ("S".equals(arquivoInstance.getIndExcPlc())) {
								retorno = true;
							}	

						}
						propertyUtilsBean.setProperty(entidade, nomePropriedade, arquivosNovos );
						return retorno;
					}

				} else {

					IPlcFile arquivo = (IPlcFile) propertyUtilsBean.getProperty(entidade, nomePropriedade );

					if (arquivo != null){

						if ("S".equals(arquivo.getIndExcPlc()) && arquivo.getId() == null) {
							propertyUtilsBean.setProperty(entidade, nomePropriedade, null );
						} else if ("S".equals(arquivo.getIndExcPlc())) {
							return true;
						}	
					}
				}
			}



		}catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

	/**
	 * Verifica se pode excluir
	 */
	protected boolean checkListToDelete(Object entidade, Class classe, String nomeColecao, String propReferenciaDesprezar, PlcConfigSubDetail configSubDetalhe)  {
		Collection<Object> colecao;
		try {
			if (isDetailValid(classe, nomeColecao)) {
				colecao = (Collection<Object>) propertyUtilsBean.getProperty(entidade, nomeColecao);
				if (colecao!=null && !colecao.isEmpty()) {
					//if (!colecao.getClass().isAssignableFrom(TreeSet.class)){
					//Detalhe
					for (Object _entidade : colecao) {
						PlcEntityInstance _entidadeInstance = metamodelUtil.createEntityInstance(_entidade);
						if(!_entidadeInstance.getIndExcPlc().equals("N")){
							return true;
						}
						else{
							//Subdetalhe
							if (configSubDetalhe!=null) {
								if (checkListToDelete(_entidade, configSubDetalhe.clazz(), configSubDetalhe.collectionName(), configUtil.getConfigDomain(configSubDetalhe.clazz()).despiseProperty(), null)){
									return true;
								}
							}
						}
					}

				}
			}
		} catch(PlcException plcE){
			throw plcE;						
		} catch (Exception e) {
			throw new PlcException("checkListToDelete", "checkListToDelete", e, log, "");
		}	
		return false;
	}


	/**
	 * Método que resolve valor de uma propriedade
	 * 
	 * Ocorrencia 30044: Caso valor seja null, o metodo deve retornar uma string vazia  
	 * 
	 */
	public Object resolveField(Object valor)  {

		PlcMetamodelUtil metamodelUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcMetamodelUtil.class, QPlcDefaultLiteral.INSTANCE);

		// Ocorrencia 30044: Caso valor seja null, o metodo deve retornar uma string vazia  
		if (valor == null) {
			return "";
		}

		// Quando for data, fazer tratamento
		if (valor.getClass().isAssignableFrom(Timestamp.class)){
			Date data = dateUtil.stringToDate(((Timestamp)valor).toLocaleString());
			valor = dateUtil.showWithHoursNull(data);
		}

		// Tratamento para enum
		if (valor.getClass().isEnum()){
			valor = ((Enum)valor).ordinal();
		}

		// Quando utilizado combo dinamico
		if (metamodelUtil.isEntityClass(valor.getClass())){
			String dominio = valor.getClass().getSimpleName();
			PlcDomainLookupUtil dominioLookupUtil = PlcCDIUtil.getInstance().getInstanceByType(PlcDomainLookupUtil.class, new PlcNamedLiteral(PlcJsfConstantes.PLC_DOMINIOS));
			List listaDominio = dominioLookupUtil.getDominio(dominio);
			if (listaDominio != null)
				valor = listaDominio.indexOf(valor);
			else
				log.warn( "Classe " + dominio + " não foi definida como lookup para essa seleção");
		}
		return valor;
	}


}


